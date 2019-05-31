package nl.hoepsch.pm.dsmr.extractor;

import nl.hoepsch.pm.dsmr.model.DSMR5Datagram;
import nl.hoepsch.pm.dsmr.model.ObisTag;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static nl.hoepsch.pm.dsmr.Crc16.calculateCrc;

/**
 * Utility to parse a DSMR5 datagram.
 */
public final class DSMR5Parser {

    /**
     * Utility constructor.
     */
    private DSMR5Parser() {
        // Do nothing.
    }

    /**
     * Parse a datagram from the source. Will thrown a {@link ChecksumMismatchError} in case the checksum is not correct.
     *
     * @param raw      The raw datagram.
     * @param checksum The checksum.
     * @return the datagram.
     */
    public static DSMR5Datagram parse(final String raw, final String checksum) {
        final int calculated = calculateCrc(raw);
        if (Integer.parseInt(checksum, 16) != calculated) {
            throw new ChecksumMismatchError();
        }
        final List<ObisTag> tags = new ArrayList<>();
        final String[] lines = raw.split("\r\n");
        for (final String line : lines) {
            if (StringUtils.isBlank(line)) {
                continue;
            }
            final Collection<ObisTag> parsed = ObisTagValueParser.parse(line);
            addParsedTags(tags, parsed);
        }

        return new DSMR5Datagram(raw, calculated, tags);
    }

    private static void addParsedTags(final List<ObisTag> tags, final Collection<ObisTag> parsed) {
        if (parsed == null || parsed.isEmpty()) {
            return;
        }

        tags.addAll(parsed);
    }
}
