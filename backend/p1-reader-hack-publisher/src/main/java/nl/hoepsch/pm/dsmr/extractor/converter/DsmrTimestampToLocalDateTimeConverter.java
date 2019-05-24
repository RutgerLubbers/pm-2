package nl.hoepsch.pm.dsmr.extractor.converter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DsmrTimestampToLocalDateTimeConverter {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyMMddHHmmss");

    public static LocalDateTime toDateTime(final String timestamp) {
        final String ts = timestamp.substring(0, timestamp.length() - 1);
        final String daylightSavingsTimeIndicator = timestamp.substring(timestamp.length() - 1);
        if (("S".equals(daylightSavingsTimeIndicator) || "W".equals(daylightSavingsTimeIndicator)) && !"632525252525".equals(ts)) {
            return getLocalDateTime(ts, daylightSavingsTimeIndicator);
        } else{
            return null;
        }
    }

    private static LocalDateTime getLocalDateTime(final String ts, final String daylightSavingsTimeIndicator) {
        try {
            // TODO Summer / Winter time
            return LocalDateTime.parse(ts, FORMATTER);
        } catch (DateTimeParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
