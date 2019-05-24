package nl.hoepsch.pm.dsmr.extractor.converter;

import nl.hoepsch.pm.dsmr.extractor.ObisTagStringValueExtractor;
import nl.hoepsch.pm.dsmr.extractor.ObisValueExtractor;
import nl.hoepsch.pm.dsmr.model.ObisTagType;

/**
 * Value extractor for an OBIS tag.
 */
public class ObisTagDoubleValueExtractor implements ObisValueExtractor<Double> {

    /**
     * The instance.
     */
    private static final ObisValueExtractor<Double> INSTANCE = new ObisTagDoubleValueExtractor();

    /**
     * The string value extractor.
     */
    private static final ObisValueExtractor<String> STRING_EXTRACTOR = ObisTagStringValueExtractor.getInstance();

    /**
     * Retrieves the instance.
     *
     * @return The instance.
     */
    public static ObisValueExtractor<Double> getInstance() {
        return INSTANCE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(final ObisTagType tag, final String line) {
        final String value = STRING_EXTRACTOR.apply(tag, line);
        return Double.valueOf(value);
    }
}
