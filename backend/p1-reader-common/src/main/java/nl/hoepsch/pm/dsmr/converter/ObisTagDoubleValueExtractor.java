package nl.hoepsch.pm.dsmr.converter;

import nl.hoepsch.pm.dsmr.model.ObisTag;
import org.springframework.stereotype.Component;

/**
 * Value extractor for an OBIS tag.
 */
@Component
public class ObisTagDoubleValueExtractor implements TagValueExtractor<Double> {

    /**
     * {@inheritDoc}
     */
    @Override
    public Double extract(final ObisTag obisTag) {
        return Double.valueOf(obisTag.getValue());
    }
}
