package nl.hoepsch.pm.dsmr.extractor;

import nl.hoepsch.pm.dsmr.model.ObisTagType;
/**
 * Value extractor for an OBIS tag.
 */
public class ObisTagRawStringValueExtractor implements ObisValueExtractor<String> {

    /**
     * The instance.
     */
    private static final ObisValueExtractor<String> INSTANCE = new ObisTagRawStringValueExtractor();

    /**
     * Retrieves the instance.
     *
     * @return The instance.
     */
    public static ObisValueExtractor<String> getInstance() {
        return INSTANCE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String apply(final ObisTagType tag, final String line) {
        return line.substring(tag.getTagLength());
    }
}
