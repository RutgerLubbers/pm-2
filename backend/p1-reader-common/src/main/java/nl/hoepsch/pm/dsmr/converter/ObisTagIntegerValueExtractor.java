package nl.hoepsch.pm.dsmr.converter;

import nl.hoepsch.pm.dsmr.model.ObisTag;
import org.springframework.stereotype.Component;

/**
 * Value extractor for an OBIS tag.
 */
@Component
public class ObisTagIntegerValueExtractor implements TagValueExtractor<Integer> {

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer extract(final ObisTag obisTag) {
        return Integer.valueOf(obisTag.getValue());
    }
}
