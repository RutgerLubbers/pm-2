package nl.hoepsch.pm.dsmr.extractor;

import nl.hoepsch.pm.dsmr.model.ObisTagType;

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
    public String apply(final ObisTagType tag, final String line) {
        final int unitLength = getUnitLength(tag);
        return line.substring(tag.getTagLength() + 1, line.length() - 1 - unitLength);
    }

    private int getUnitLength(final ObisTagType tag) {
        int unitLength = tag.getValueUnitLength();
        if (unitLength > 0) {
            unitLength++;
        }
        return unitLength;
    }
}
