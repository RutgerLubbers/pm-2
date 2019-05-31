package nl.hoepsch.pm.electricity.meter.kafka;

import nl.hoepsch.pm.electricity.meter.ElectricityMeterPeriodReadoutMapper;
import nl.hoepsch.pm.electricity.meter.dto.ElectricityMeterPeriodReadoutDto;
import nl.hoepsch.pm.electricity.meter.model.ElectricityMeterPeriodReadout;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static nl.hoepsch.pm.config.KafkaTopics.ELECTRICITY_METER_PER_DAY_VIEW;

/**
 * Easy accessor for a store defined in the kafka streams.
 */
@Component
public class ElectricityMeterPerDayView {

    /**
     * The Kafka Streams configured.
     */
    private final KafkaStreams streams;

    /**
     * The mapper to and from dto.
     */
    private final ElectricityMeterPeriodReadoutMapper mapper;

    /**
     * The constructor.
     *
     * @param streams The Kafka Streams configured.
     * @param mapper  The mapper to and from dto.
     */
    @Autowired
    public ElectricityMeterPerDayView(final KafkaStreams streams,
        final ElectricityMeterPeriodReadoutMapper mapper) {
        this.streams = streams;
        this.mapper = mapper;
    }

    /**
     * Get the electricity meter readout for the given {@code day}.
     *
     * @param day The day to retrieve.
     * @return The readout for that day, or {@code null}.
     */
    public ElectricityMeterPeriodReadout get(final String day) {
        final KafkaStreams.State state = streams.state();
        if (state == KafkaStreams.State.RUNNING) {
            final ReadOnlyKeyValueStore<String, ElectricityMeterPeriodReadoutDto> store =
                streams.store(ELECTRICITY_METER_PER_DAY_VIEW, QueryableStoreTypes.keyValueStore());

            return mapper.map(store.get(day));
        }
        return null;
    }
}
