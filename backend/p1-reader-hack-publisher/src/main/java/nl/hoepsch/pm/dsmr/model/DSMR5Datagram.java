package nl.hoepsch.pm.dsmr.model;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import static nl.hoepsch.pm.dsmr.Crc16.calculateCrc;

public class DSMR5Datagram {

    private final String raw;

    private List<ObisTag> tags = new ArrayList<>();

    private final int checkSum;

    public DSMR5Datagram(final String datagram, String checksum) {
        this.raw = datagram;
        checkSum = Integer.parseInt(checksum, 16);
        parse();
    }

    private void parse() {
        if (this.checkSum != calculateCrc(raw)) {
            throw new ChecksumMismatchError();
        }

        final String[] lines = raw.split("\r\n");
        for (String line : lines) {
            if (StringUtils.isBlank(line)) {
                continue;
            }
            final Collection<ObisTag > tags = ObisTagValueExtractorRepository.parse(line);
            if (tags == null || tags.isEmpty()) {
                continue;
            } else {
                this.tags.addAll(tags);
            }
        }
    }


    public String getRaw() {
        return raw;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public List<ObisTag> getTags() {
        return tags;
    }

    public String getValue(final ObisTagType tagType) {
        for (ObisTag tag : tags) {
            if (tag.hasType(tagType)) {
                return tag.getValue();
            }
        }
        return null;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public int getCheckSum() {
        return checkSum;
    }

    @Override
    public String toString() {
        return raw + Integer.toHexString(checkSum).toUpperCase(Locale.ENGLISH);
    }

}
