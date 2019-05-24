package nl.hoepsch.pm.model;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static java.util.Objects.requireNonNull;

/**
 * Assembles a {@link GasMeterReadout} from {@link Fields}.
 */
@Component
public class GasMeterReadoutAssembler {

    /**
     * The assembler for a {@link Meter}.
     */
    private final MeterAssembler meterAssembler;

    /**
     * The constructor.
     */
    @Autowired
    public GasMeterReadoutAssembler(final MeterAssembler meterAssembler) {
        this.meterAssembler = requireNonNull(meterAssembler);
    }

    /**
     * Converts the {@code record} into a {@link GasMeterReadout}.
     */
    public GasMeterReadout convert(final ConsumerRecord<Long, String> record) {
        return convert(new Fields(record.value()));
    }

    /**
     * Converts the {@code fields} into a {@link GasMeterReadout}.
     */
    public GasMeterReadout convert(final Fields fields) {
        final GasMeterReadout readout = new GasMeterReadout();
        final Meter meter = meterAssembler.convert(fields);
        readout.setMeter(meter);
        readout.setTimestamp(fields.getTimestamp());
        readout.setReadoutTimestamp(fields.getReadoutTimestamp());
        readout.setValue(fields.getGasMeterValue());
        return readout;
    }
}
