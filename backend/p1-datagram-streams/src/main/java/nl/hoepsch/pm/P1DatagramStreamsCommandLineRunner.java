package nl.hoepsch.pm;

import nl.hoepsch.pm.electricity.meter.dto.ElectricityMeterPeriodReadoutDto;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.errors.InvalidStateStoreException;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import static nl.hoepsch.pm.config.KafkaTopics.ELECTRICITY_METER_PER_DAY_VIEW;

/**
 * The command line runner for the p1 reader.
 */
@Component
public class P1DatagramStreamsCommandLineRunner implements CommandLineRunner {

    /**
     * The logger to use.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(P1DatagramStreamsCommandLineRunner.class);

    /**
     * The Kafka Streams to run.
     */
    private final KafkaStreams streams;

    /**
     * The constructor.
     *
     * @param streams The Kafka Streams to run.
     */
    @Autowired
    public P1DatagramStreamsCommandLineRunner(final KafkaStreams streams) {
        this.streams = streams;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run(final String... args) {
        LOGGER.info("P1 Datagram Streams App Started.");

        while (true) {
            try {

                final ElectricityMeterPeriodReadoutDto dto = get("2019-01-04");
                if (dto != null) {
                    LOGGER.warn("Got '{}' - '{}'.", dto.getStart().getTimestamp(), dto.getEnd().getTimestamp());
                }
                Thread.sleep(5_000);
            } catch (InterruptedException | InvalidStateStoreException ignored) {
                // Do nothing.
            }
        }
    }

    private ElectricityMeterPeriodReadoutDto get(final String key) {
        final KafkaStreams.State state = streams.state();
        if (state == KafkaStreams.State.RUNNING) {
            final ReadOnlyKeyValueStore<String, ElectricityMeterPeriodReadoutDto> store =
                streams.store(ELECTRICITY_METER_PER_DAY_VIEW, QueryableStoreTypes.keyValueStore());

            try (KeyValueIterator<String, ElectricityMeterPeriodReadoutDto> all = store.all()) {
                while (all.hasNext()) {
                    final KeyValue<String, ElectricityMeterPeriodReadoutDto> value = all.next();
                    LOGGER.info("Got index (day) '{}'.", value.key);
                }
            }
            return store.get(key);
        }
        return null;
    }
}
