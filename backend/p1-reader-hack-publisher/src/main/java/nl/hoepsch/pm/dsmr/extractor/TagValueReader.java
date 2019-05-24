package nl.hoepsch.pm.dsmr.extractor;

import nl.hoepsch.pm.dsmr.model.ObisTagType;

/**
 * Utility to read a tag's value, without parenthesis and unit(s).
 */
public final class TagValueReader {

    /**
     * Utility constructor.
     */
    private TagValueReader() {
        // Do nothing.
    }

    /**
     * Extract the tag's value, without parenthesis and without (optional) unit.
     *
     * @param tagType The tag's  type.
     * @param value   The tag's value.
     * @return the extracted value, without parenthesis and without (optional) unit.
     */
    public static String extractValue(final ObisTagType tagType, final String value) {
        final int unitLength = getUnitLength(tagType);
        return value.substring(1, value.length() - 1 - unitLength);
    }

    private static int getUnitLength(final ObisTagType tag) {
        int unitLength = tag.getValueUnitLength();
        if (unitLength > 0) {
            unitLength++;
        }
        return unitLength;
    }
}
