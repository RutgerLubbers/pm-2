package nl.hoepsch.pm.dsmr.model;

import static java.lang.String.format;

public class ObisTag {
    private final ObisTagType tag;

    private final String value;

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
