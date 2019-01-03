package nl.hoepsch.pm;

import nl.hoepsch.pm.config.InitialMeterReadoutsConfigurationProperties;
import nl.hoepsch.pm.config.PowerMeterConfigurationProperties;
import nl.hoepsch.pm.model.AccumulatedGasUsage;
import nl.hoepsch.pm.model.AccumulatedValue;
import nl.hoepsch.pm.model.GasMeterReadout;
import nl.hoepsch.pm.model.GasMeterReadoutAssembler;
import nl.hoepsch.pm.model.InitialMeterReadoutsAssembler;
import nl.hoepsch.pm.model.Meter;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static java.util.Objects.requireNonNull;

/**
 * A {@link RecordConsumer}.
 */
@Component
public class GasDailyUsageAccumulator implements RecordConsumer, InitializingBean {

    /**
     * The logger to use.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(GasDailyUsageAccumulator.class);

    /**
     * Constant for anomalous usage.
     */
    private static final Long ANOMALOUS_USAGE = 10000L;

    /**
     * Day formatter.
     */
    private static final DateTimeFormatter DAY_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * Time formatter.
     */
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    /**
     * The assembler for gas meter readouts.
     */
    private final GasMeterReadoutAssembler gasMeterReadoutAssembler;

    /**
     * The application's configuration properties.
     */
    private final PowerMeterConfigurationProperties powerMeterConfigurationProperties;

    /**
     * The assembler for initial meter readouts.
     */
    private final InitialMeterReadoutsAssembler initialMeterReadoutsAssembler;

    /**
     * The map of initial readouts.
     */
    private final Map<String, Deque<GasMeterReadout>> initialReadouts = new HashMap<>();


    private final AccumulatedGasUsageRepository repository;

    /**
     * The current accumulated value.
     */
    private AccumulatedValue<Long> accumulatedValue;

    /**
     * The last event's meter readout.
     */
    private GasMeterReadout lastMeterReadout;

    private AccumulatedGasUsage toStore;

    /**
     * The constructor.
     */
    @Autowired
    public GasDailyUsageAccumulator(final AccumulatedGasUsageRepository repository, final GasMeterReadoutAssembler gasMeterReadoutAssembler,
            final InitialMeterReadoutsAssembler initialMeterReadoutsAssembler,
            final PowerMeterConfigurationProperties powerMeterConfigurationProperties) {
        this.repository = requireNonNull(repository);
        this.gasMeterReadoutAssembler = requireNonNull(gasMeterReadoutAssembler);
        this.initialMeterReadoutsAssembler = requireNonNull(initialMeterReadoutsAssembler);
        this.powerMeterConfigurationProperties = requireNonNull(powerMeterConfigurationProperties);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void afterPropertiesSet() {
        createLastValues(powerMeterConfigurationProperties.getInitialMeterReadouts());
    }

    @SuppressWarnings({"PMD.LawOfDemeter", "PMD.AvoidInstantiatingObjectsInLoops"})
    private void createLastValues(final List<InitialMeterReadoutsConfigurationProperties> initialValues) {
        requireNonNull(initialValues);

        for (final InitialMeterReadoutsConfigurationProperties initialValue : initialValues) {
            final String key = initialValue.getMeterId();
            final Deque<GasMeterReadout> deque = initialReadouts.computeIfAbsent(key, _key -> new ArrayDeque<>());
            deque.addLast(createGasMeterReadout(initialValue));
        }

    }

    @SuppressWarnings("PMD.LawOfDemeter")
    private GasMeterReadout createGasMeterReadout(final InitialMeterReadoutsConfigurationProperties initialValue) {
        return initialMeterReadoutsAssembler.convert(initialValue).getGasMeterReadout();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return "gasDailyUsage";
    }



    /**
     * <pre>
     *  record -> meterReadout
     *
     *  -- For the delta between record and last record, the AV uses the last record's timestamp.
     *  AV accumulatedValue = handleRecord(meterReadout, lastMeterReadout, this.accumulatedValue)
     *
     *  -- we must commit if record is a new day wrt last record;
     *  -- we must commit if record is more than X duration after last committed value
     *  if (mustCommit(record, lastRecord, lastCommittedValue)) {
     *      commit(accumulatedValue)
     *
     *      lastCommittedValue = accumulatedValue;
     *  }
     *
     *
     *  lastMeterReadout = meterReadout
     *  this.accumulatedValue = accumulatedValue
     * </pre>
     * <p>
     * {@inheritDoc}
     */
    @SuppressWarnings("PMD.LawOfDemeter")
    @Override
    public void consume(final ConsumerRecord<Long, String> record, final boolean commit) {
        LOGGER.trace("Consume record '{}'.", record.value());
        GasMeterReadout meterReadout = gasMeterReadoutAssembler.convert(record);

        /*
         * Find latest stored value with timestamp before meter readout timestamp.
         */
        final AccumulatedValue<Long> lastStoredValueBeforeReadout = findLatestStoredBefore(meterReadout);
        if (lastStoredValueBeforeReadout == null) {
            LOGGER.error("No initial values found, returning.");
            return;
        }
        if (lastMeterReadout == null || accumulatedValue == null) {
            // First record, set default values.
            this.accumulatedValue = lastStoredValueBeforeReadout;
            this.lastMeterReadout = meterReadout;
        } else {
            meterReadout = sanitize(meterReadout, lastMeterReadout);
            if (lastMeterReadout.getReadoutTimestamp().isBefore(meterReadout.getReadoutTimestamp())) {
                final AccumulatedValue<Long> accumulatedValue =
                        consumeRecord(meterReadout, lastMeterReadout, this.accumulatedValue, lastStoredValueBeforeReadout);

                final LocalDateTime timestamp = accumulatedValue.getTimestamp();
                LOGGER.debug("Accumulated gas usage for '{}' at '{}' is '{}'.", timestamp.format(DAY_FORMATTER),
                        timestamp.format(TIME_FORMATTER), accumulatedValue.getValue());
                /*
                 * Optionally store the accumulated value.
                 */
                store(meterReadout, lastMeterReadout, accumulatedValue);
                this.accumulatedValue = createNewAccumulatedValue(meterReadout, lastMeterReadout, accumulatedValue);
                this.lastMeterReadout = meterReadout;
            }
        }

    }

    @SuppressWarnings("PMD.LawOfDemeter")
    private GasMeterReadout sanitize(final GasMeterReadout meterReadout, final GasMeterReadout lastMeterReadout) {
        /*
         * <pre>
         *  19418    2013-06-27    2013-06-27 23:58:06    1725    3491    60    866    413    0    3420118    230000
         *
         *  19419    2013-06-28    2013-06-28 00:00:06    1725    3491    60    866    413    0    3420118    230000
         * </pre>
         */

        final long durationInHours = Duration.between(lastMeterReadout.getTimestamp(), meterReadout.getTimestamp()).toHours();
        final long readoutDurationInHours =
                Duration.between(lastMeterReadout.getReadoutTimestamp(), meterReadout.getReadoutTimestamp()).toHours();

        if (durationInHours == 0L && readoutDurationInHours == 24L) {
            LOGGER.debug("Gas readout of previous day detected.");
            final GasMeterReadout gasMeterReadout = new GasMeterReadout();
            gasMeterReadout.setMeter(meterReadout.getMeter());
            gasMeterReadout.setReadoutTimestamp(lastMeterReadout.getReadoutTimestamp());
            gasMeterReadout.setTimestamp(meterReadout.getTimestamp());
            gasMeterReadout.setValue(meterReadout.getValue());
            return gasMeterReadout;
        }


        return meterReadout;
    }

    @SuppressWarnings("PMD.LawOfDemeter")
    private AccumulatedValue<Long> createNewAccumulatedValue(final GasMeterReadout meterReadout, final GasMeterReadout lastMeterReadout,
            final AccumulatedValue<Long> accumulatedValue) {
        if (isDifferentDay(meterReadout, lastMeterReadout)) {
            LOGGER.debug("Start of new day, resetting accumulated value.");
            return new AccumulatedValue<>(meterReadout.getReadoutTimestamp(), 0L);
        } else {
            return accumulatedValue;
        }
    }

    @SuppressWarnings("PMD.LawOfDemeter")
    private boolean isDifferentDay(final GasMeterReadout meterReadout, final GasMeterReadout lastMeterReadout) {
        return meterReadout.getReadoutTimestamp().getDayOfYear() != lastMeterReadout.getReadoutTimestamp().getDayOfYear();
    }

    private void store(final GasMeterReadout meterReadout, final GasMeterReadout lastMeterReadout,
            final AccumulatedValue<Long> accumulatedValue) {
        /**
         * Suppose meterReadout has topicOffset.
         *
         * Which one should we use upon restart from offset? This depends on whether the id with the offset is returned or the first id > offset.
         *
         * One can commit the last consumed record, setting the offset to that record's offset. So the next poll will receive records with offset > last offset.
         *
         *
         */
        if (isDifferentDay(meterReadout, lastMeterReadout) || shouldCommit(meterReadout)) {
            LOGGER.debug("Should store...");

            final LocalDateTime timestamp = accumulatedValue.getTimestamp();
            final String date = timestamp.format(DAY_FORMATTER);
            if (toStore == null || !toStore.getDate().equals(date)) {
                toStore = new AccumulatedGasUsage();
                toStore.setTimestamp(timestamp);
                toStore.setDate(date);
            }
            toStore.setUsage(accumulatedValue.getValue());
            toStore = repository.store(toStore);
        }
    }

    private boolean shouldCommit(final GasMeterReadout meterReadout) {
        final int hour = meterReadout.getTimestamp().getHour();
        return hour % 4 == 0;
    }

    @SuppressWarnings("PMD.LawOfDemeter")
    private AccumulatedValue<Long> consumeRecord(final GasMeterReadout meterReadout, final GasMeterReadout lastMeterReadout,
            final AccumulatedValue<Long> accumulatedValue, final AccumulatedValue<Long> lastStoredValueBeforeReadout) {
        requireNonNull(meterReadout);
        requireNonNull(lastMeterReadout);
        requireNonNull(accumulatedValue);
        requireNonNull(lastStoredValueBeforeReadout);

        /*
         * TODO Detect time jumps...
         */
        if (lastStoredValueBeforeReadout.getTimestamp().isAfter(accumulatedValue.getTimestamp())) {
            LOGGER.debug("Jump in time!");
        }

        /*
         * TODO Detect time jump in meter readouts.
         */

        final LocalDateTime lastTimeStamp = lastMeterReadout.getReadoutTimestamp();
        final long diff = meterReadout.getValue() - lastMeterReadout.getValue();
        if (Math.abs(diff) > ANOMALOUS_USAGE) {
            final LocalDateTime readoutTimestamp = meterReadout.getReadoutTimestamp();

            LOGGER.debug("Ignoring anomaly of '{}' between '{} {}' and '{} {}'.", diff,
                    lastTimeStamp.format(DAY_FORMATTER), lastTimeStamp.format(TIME_FORMATTER),
                    readoutTimestamp.format(DAY_FORMATTER), readoutTimestamp.format(TIME_FORMATTER));

            return new AccumulatedValue<>(lastTimeStamp, accumulatedValue.getValue());
        }

        return new AccumulatedValue<>(lastTimeStamp, accumulatedValue.getValue() + diff);
    }

    @SuppressWarnings("PMD.LawOfDemeter")
    private AccumulatedValue<Long> findLatestStoredBefore(final GasMeterReadout meterReadout) {
        final LocalDateTime timestamp = meterReadout.getReadoutTimestamp();
        final Meter meter = meterReadout.getMeter();
        final Deque<GasMeterReadout> gasMeterReadouts = initialReadouts.get(meter.getId());

        if (gasMeterReadouts != null) {
            final Iterator<GasMeterReadout> iterator = gasMeterReadouts.descendingIterator();
            while (iterator.hasNext()) {
                final GasMeterReadout storedReadout = iterator.next();
                final LocalDateTime storedTimestamp = storedReadout.getReadoutTimestamp();
                if (storedTimestamp.isEqual(timestamp) || storedTimestamp.isBefore(timestamp)) {
                    return new AccumulatedValue<>(storedTimestamp, meterReadout.getValue() - storedReadout.getValue());
                }
            }
        }
        LOGGER.error("No initial values set for meter with id '{}'.", meter.getId());
        return null;
    }

    /**
     * Protected accessor for testing purposes.
     */
    protected AccumulatedValue<Long> getAccumulatedValue() {
        return accumulatedValue;
    }
}
