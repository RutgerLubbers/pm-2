package nl.hoepsch.pm.dsmr.extractor.converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Utility to convert a DSMR timestamp into a date time.
 */
public final class DsmrTimestampToLocalDateTimeConverter {

    /**
     * The logger to use.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(DsmrTimestampToLocalDateTimeConverter.class);

    /**
     * The date time format used by DSMR.
     */
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyMMddHHmmss");

    /**
     * Utility constructor.
     */
    private DsmrTimestampToLocalDateTimeConverter() {
        // Do nothing.
    }

    /**
     * Converts a DSMR timestamp into a local date time.
     *
     * @param timestamp The timestamp to convert.
     * @return The local date time.
     */
    public static LocalDateTime toDateTime(final String input) {
        final String timestamp = input.substring(0, input.length() - 1);
        final String daylightSavingsTimeIndicator = input.substring(input.length() - 1);
        if (("S".equals(daylightSavingsTimeIndicator) || "W".equals(daylightSavingsTimeIndicator)) && !"632525252525".equals(timestamp)) {
            return getLocalDateTime(timestamp, daylightSavingsTimeIndicator);
        } else {
            return null;
        }
    }

    private static LocalDateTime getLocalDateTime(final String timestamp, final String daylightSavingsTimeIndicator) {
        try {
            // TODO Summer / Winter time
            return LocalDateTime.parse(timestamp, FORMATTER);
        } catch (DateTimeParseException e) {
            LOGGER.error("Could not parse timestamp '{}' with DTS '{}'.", timestamp, daylightSavingsTimeIndicator, e);
            return null;
        }
    }
}
