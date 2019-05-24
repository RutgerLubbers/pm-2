package nl.hoepsch.pm.dsmr.extractor;

import nl.hoepsch.pm.dsmr.model.ObisTagType;

import static nl.hoepsch.pm.dsmr.extractor.TagValueReader.extractValue;

/**
 * Value extractor for an OBIS tag.
 */
public class ObisTagStringValueExtractor implements ObisValueExtractor<String> {

    /**
     * The instance.
     */
    private static final ObisValueExtractor<String> INSTANCE = new ObisTagStringValueExtractor();

    /**
     * Retrieves the instance.
     *
     * @return the instance.
     */
    public static ObisValueExtractor<String> getInstance() {
        return INSTANCE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String apply(final ObisTagType tagType, final String line) {
        return extractValue(tagType, line.substring(tagType.getTagLength()));
    }


}
