package nl.hoepsch.pm.dsmr.extractor.converter;

import nl.hoepsch.pm.dsmr.extractor.ObisTagStringValueExtractor;
import nl.hoepsch.pm.dsmr.extractor.ObisValueExtractor;
import nl.hoepsch.pm.dsmr.model.ObisTagType;

/**
 * Value extractor for an OBIS tag.
 */
public class ObisTagIntegerValueExtractor implements ObisValueExtractor<Integer> {

    /**
     * The instance.
     */
    private static final ObisValueExtractor<Integer> INSTANCE = new ObisTagIntegerValueExtractor();

    /**
     * The string value extractor.
     */
    private static final ObisValueExtractor<String> STRING_EXTRACTOR = ObisTagStringValueExtractor.getInstance();

    /**
     * Retrieves the instance.
     *
     * @return The instance.
     */
    public static ObisValueExtractor<Integer> getInstance() {
        return INSTANCE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer apply(final ObisTagType tag, final String line) {
        final String value = STRING_EXTRACTOR.apply(tag, line);
        return Integer.valueOf(value);
    }
}
