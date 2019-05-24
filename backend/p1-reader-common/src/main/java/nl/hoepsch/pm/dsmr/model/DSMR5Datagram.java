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
    private String raw;

    /**
     * The parsed tags.
     */
    private List<ObisTag> tags;

    /**
     * The calculated checksum.
     */
    private int checkSum;

    /**
     * The default constructor.
     */
    public DSMR5Datagram() {
        // Do nothing.
    }

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
    public void setRaw(final String raw) {
        this.raw = raw;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public List<ObisTag> getTags() {
        return tags;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public void setTags(final List<ObisTag> tags) {
        this.tags = tags;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public int getCheckSum() {
        return checkSum;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public void setCheckSum(final int checkSum) {
        this.checkSum = checkSum;
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
    public ObisTag getTag(final ObisTagType tagType) {
        for (final ObisTag tag : tags) {
            if (tag.hasType(tagType)) {
                return tag;
            }
        }
        return null;
    }

    /**
     * Returns the tag's value.
     *
     * @param tagType The tag type to get the value for.
     * @return The tag's value.
     */
    public String getTagValue(final ObisTagType tagType) {
        final ObisTag tag = getTag(tagType);
        return getTagValue(tag);
    }

    private String getTagValue(final ObisTag tag) {
        if (tag == null) {
            return null;
        }
        return tag.getValue();
    }
}
