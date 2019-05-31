package nl.hoepsch.pm.electricity.actual.kafka;

import nl.hoepsch.pm.electricity.actual.ElectricityActualReadoutMapper;
import nl.hoepsch.pm.electricity.actual.dto.ElectricityActualReadoutDto;
import nl.hoepsch.pm.electricity.actual.model.ElectricityActualReadout;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Transformer;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

/**
 * Transformer to create a key per day for a stream of electricity meter readout values.
 * <p>
 * Each readout will be returned as is, the original key will be replaced by the readout's timestamp (on day basis).
 */
@Component
public class ElectricityActualReadoutToDayTransformer implements
    Transformer<String, ElectricityActualReadoutDto, KeyValue<String, ElectricityActualReadoutDto>> {

    /**
     * The date time formatter.
     */
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * The mapper to convert electricity actual readouts to and from model and dto.
     */
    private final ElectricityActualReadoutMapper readoutMapper;

    /**
     * The constructor.
     *
     * @param readoutMapper The mapper to convert electricity actual readouts to and from model and dto.
     */
    @Autowired
    public ElectricityActualReadoutToDayTransformer(final ElectricityActualReadoutMapper readoutMapper) {
        this.readoutMapper = readoutMapper;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void init(final ProcessorContext context) {
        // Do nothing.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public KeyValue<String, ElectricityActualReadoutDto> transform(final String ignored, final ElectricityActualReadoutDto value) {
        return new KeyValue<>(getKey(readoutMapper.map(value)), value);
    }

    private String getKey(final ElectricityActualReadout readout) {
        return DATE_FORMATTER.format(readout.getTimestamp());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() {
        // Do nothing.
    }

}
