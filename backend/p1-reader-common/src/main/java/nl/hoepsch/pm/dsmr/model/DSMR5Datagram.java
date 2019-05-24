package nl.hoepsch.pm.dsmr.model;

import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Locale;

/**
 * A DSMR 5 datagram.
 */
public class DSMR5Datagram {

    /**
     * The raw input string.
     */
    private final String raw;

    /**
     * The parsed tags.
     */
    private final List<ObisTag> tags;

    /**
     * The calculated checksum.
     */
    private final int checkSum;

    /**
     * The constructor.
     *
     * @param datagram The raw datagram.
     * @param checksum The checksum.
     * @param tags     The parsed tags.
     */
    public DSMR5Datagram(final String datagram, final int checksum, final List<ObisTag> tags) {
        this.raw = datagram;
        this.checkSum = checksum;
        this.tags = tags;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public String getRaw() {
        return raw;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public List<ObisTag> getTags() {
        return tags;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public int getCheckSum() {
        return checkSum;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return raw + StringUtils.upperCase(Integer.toHexString(checkSum), Locale.ENGLISH);
    }

    /**
     * Get a value for a specified tag.
     *
     * @param tagType The tag to retrieve.
     * @return The tag's value, or {@code null}.
     */
    public String getValue(final ObisTagType tagType) {
        for (final ObisTag tag : tags) {
            if (tag.hasType(tagType)) {
                return tag.getValue();
            }
        }
        return null;
    }
}
