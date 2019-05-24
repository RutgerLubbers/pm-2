package nl.hoepsch.pm.dsmr.converter;

import nl.hoepsch.pm.dsmr.model.DSMR5Datagram;
import nl.hoepsch.pm.dsmr.model.ObisTag;
import nl.hoepsch.pm.dsmr.model.ObisTagType;

/**
 * Utility to extract a tag's value from a datagram.
 *
 * @param <T> The tag's value type.
 */
public interface TagValueExtractor<T> {

    /**
     * Extract the value from the given tag.
     *
     * @param obisTag The tag to extract the value from.
     * @return The tag's value.
     */
    T extract(ObisTag obisTag);

    /**
     * Extract the tag's value for the given tag type.
     *
     * @param datagram The datagram to get the tag's value from.
     * @param tagType  The tag type to get the value for.
     * @return The tag's value.
     */
    default T extract(DSMR5Datagram datagram, ObisTagType tagType) {
        return extract(datagram.getTag(tagType));
    }
}
