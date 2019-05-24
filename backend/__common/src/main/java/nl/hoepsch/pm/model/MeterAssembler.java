package nl.hoepsch.pm.model;

import org.springframework.stereotype.Component;

/**
 * Assembles a {@link Meter} from {@link Fields}.
 */
@Component
public class MeterAssembler {

    /**
     * Converts the {@code fields} into a {@link Meter}.
     */
    public Meter convert(final Fields fields) {
        final Meter meter = new Meter();
        meter.setId(fields.getMeterId());
        meter.setName(fields.getMeterName());
        return meter;
    }
}
