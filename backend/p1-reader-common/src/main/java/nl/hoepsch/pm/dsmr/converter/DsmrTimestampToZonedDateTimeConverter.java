package nl.hoepsch.pm.dsmr.converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.zone.ZoneOffsetTransition;
import java.time.zone.ZoneRules;
import java.util.Collection;
import java.util.List;

/**
 * Utility to convert a DSMR timestamp into a date time.
 */
public final class DsmrTimestampToZonedDateTimeConverter {

    /**
     * The logger to use.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(DsmrTimestampToZonedDateTimeConverter.class);

    /**
     * The date time format used by DSMR.
     */
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyMMddHHmmss");

    /**
     * Utility constructor.
     */
    private DsmrTimestampToZonedDateTimeConverter() {
        // Do nothing.
    }

    /**
     * Converts a DSMR timestamp into a local date time.
     *
     * @param input The timestamp to convert.
     * @return The local date time.
     */
    @SuppressWarnings("PMD.LawOfDemeter")
    public static ZonedDateTime toDateTime(final String input, final ZoneId zoneId) {
        final String timestamp = input.substring(0, input.length() - 1);
        final String daylightSavingsTimeIndicator = input.substring(input.length() - 1);
        if ((isSummerTime(daylightSavingsTimeIndicator) || isWinterTime(daylightSavingsTimeIndicator)) && !"632525252525"
            .equals(timestamp)) {
            final LocalDateTime localDateTime = getLocalDateTime(timestamp, daylightSavingsTimeIndicator);

            final ZoneRules rules = zoneId.getRules();
            final List<ZoneOffset> validOffsets = rules.getValidOffsets(localDateTime);
            ZoneOffset zoneOffset = null;
            if (hasOneElement(validOffsets)) {
                zoneOffset = validOffsets.get(0);
            } else {
                // Gap or Overlap: determine what to do from transition (which will be non-null)
                final ZoneOffsetTransition offsetTransition = rules.getTransition(localDateTime);
                if (offsetTransition.isOverlap()) {
                    if (isWinterTime(daylightSavingsTimeIndicator)) {
                        zoneOffset = validOffsets.get(1);
                    } else {
                        zoneOffset = validOffsets.get(0);
                    }
                } else {
                    LOGGER.error("Could not transform date time '{}' for zone '{}'.", input, zoneId);
                    // HUH?
                }
            }
            return ZonedDateTime.from(localDateTime.atOffset(zoneOffset));
        } else {
            return null;
        }
    }

    @SuppressWarnings("PMD.AvoidLiteralsInIfCondition")
    private static boolean hasOneElement(final Collection<?> collection) {
        return collection.size() == 1;
    }

    private static boolean isWinterTime(final String daylightSavingsTimeIndicator) {
        return "W".equals(daylightSavingsTimeIndicator);
    }

    private static boolean isSummerTime(final String daylightSavingsTimeIndicator) {
        return "S".equals(daylightSavingsTimeIndicator);
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
