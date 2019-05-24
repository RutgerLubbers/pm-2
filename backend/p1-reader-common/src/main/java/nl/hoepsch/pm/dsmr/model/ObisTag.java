package nl.hoepsch.pm.dsmr.model;

import static java.lang.String.format;

/**
 * An obis tag value.
 */
public class ObisTag {

    /**
     * The tag.
     */
    private final ObisTagType tag;

    /**
     * The value.
     */
    private final String value;

    /**
     * The constructor.
     *
     * @param tag   The tag.
     * @param value The tag's value.
     */
    public ObisTag(final ObisTagType tag, final String value) {
        this.tag = tag;
        this.value = value;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public ObisTagType getTag() {
        return tag;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public String getValue() {
        return value;
    }

    /**
     * Method to check if this' obis tag has the type {@code tagType}.
     *
     * @param tagType The tag type to compare this' type against.
     * @return {@code true} if this' tag type equals the given one.
     */
    public boolean hasType(final ObisTagType tagType) {
        return this.tag == tagType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return format("%s: '%s'", tag, value);
    }
}
