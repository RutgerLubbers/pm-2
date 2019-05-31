package nl.hoepsch.pm.electricity.meter;

import nl.hoepsch.pm.electricity.meter.dto.ElectricityMeterReadoutDto;
import nl.hoepsch.pm.electricity.meter.model.ElectricityMeterReadout;
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
public class ElectricityMeterReadoutToDayTransformer implements
    Transformer<String, ElectricityMeterReadoutDto, KeyValue<String, ElectricityMeterReadoutDto>> {

    /**
     * The date time formatter.
     */
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * The mapper to convert electricity actual readouts to and from model and dto.
     */
    private final ElectricityMeterReadoutMapper readoutMapper;

    /**
     * The constructor.
     *
     * @param readoutMapper The mapper to convert electricity actual readouts to and from model and dto.
     */
    @Autowired
    public ElectricityMeterReadoutToDayTransformer(final ElectricityMeterReadoutMapper readoutMapper) {
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
    public KeyValue<String, ElectricityMeterReadoutDto> transform(final String ignored, final ElectricityMeterReadoutDto value) {
        return new KeyValue<>(getKey(readoutMapper.map(value)), value);
    }

    private String getKey(final ElectricityMeterReadout readout) {
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
