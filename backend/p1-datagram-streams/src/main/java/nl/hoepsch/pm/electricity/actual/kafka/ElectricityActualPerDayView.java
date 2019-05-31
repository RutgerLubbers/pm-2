package nl.hoepsch.pm.electricity.actual.kafka;

import nl.hoepsch.pm.electricity.actual.ElectricityActualPeriodReadoutMapper;
import nl.hoepsch.pm.electricity.actual.dto.ElectricityActualPeriodReadoutDto;
import nl.hoepsch.pm.electricity.actual.model.ElectricityActualPeriodReadout;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static nl.hoepsch.pm.config.KafkaTopics.ELECTRICITY_ACTUAL_PER_DAY_VIEW;

/**
 * Easy accessor for a store defined in the kafka streams.
 */
@Component
public class ElectricityActualPerDayView {

    /**
     * The Kafka Streams configured.
     */
    private final KafkaStreams streams;

    /**
     * The mapper to and from dto.
     */
    private final ElectricityActualPeriodReadoutMapper mapper;

    /**
     * The constructor.
     *
     * @param streams The Kafka Streams configured.
     * @param mapper  The mapper to and from dto.
     */
    @Autowired
    public ElectricityActualPerDayView(final KafkaStreams streams,
        final ElectricityActualPeriodReadoutMapper mapper) {
        this.streams = streams;
        this.mapper = mapper;
    }

    /**
     * Get the electricity actual readouts for the given {@code day}.
     *
     * @param day The day to retrieve.
     * @return The readouts for that day, or {@code null}.
     */
    public ElectricityActualPeriodReadout get(final String day) {
        final KafkaStreams.State state = streams.state();
        if (state == KafkaStreams.State.RUNNING) {
            final ReadOnlyKeyValueStore<String, ElectricityActualPeriodReadoutDto> store =
                streams.store(ELECTRICITY_ACTUAL_PER_DAY_VIEW, QueryableStoreTypes.keyValueStore());

            return mapper.map(store.get(day));
        }
        return null;
    }
}
