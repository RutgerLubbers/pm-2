package nl.hoepsch.pm.kafka;

import nl.hoepsch.pm.config.SharedMapperConfig;
import org.mapstruct.Mapper;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZonedDateTime;

import static org.mapstruct.ReportingPolicy.IGNORE;

/**
 * This mapper contains various mappings of date and time field types.
 */
@Mapper(config = SharedMapperConfig.class, unmappedTargetPolicy = IGNORE)
public interface AvroDateTimeMapper {

    /**
     * Map a local date to an AVRO 'date' type.
     *
     * @param localDate the local date
     * @return the number of days since 1 January 1970.
     */
    default Integer toAvroDate(final LocalDate localDate) {
        final Integer epochDays;
        if (localDate == null) {
            epochDays = null;
        } else {
            epochDays = (int) localDate.toEpochDay();
        }
        return epochDays;
    }

    /**
     * Converts the integer value to a LocalDate.
     *
     * @param epochDays the integer value
     * @return the localdate.
     */
    default LocalDate fromAvroDate(final Integer epochDays) {
        final LocalDate localDate;
        if (epochDays == null) {
            localDate = null;
        } else {
            localDate = LocalDate.ofEpochDay(epochDays);
        }
        return localDate;
    }

    /**
     * Convert a zoned date-time object into an AVRO 'timestamp-millis' value.
     *
     * @param zonedDateTime the zoned date-time value
     * @return number of milliseconds from the unix epoch, 1 January 1970 00:00:00.000 UTC
     */
    default Long toAvroTimeStampMillis(final ZonedDateTime zonedDateTime) {
        final Long result;
        if (zonedDateTime == null) {
            result = null;
        } else {
            result = toAvroTimeStampMillis(zonedDateTime.toInstant());
        }
        return result;
    }

    /**
     * Convert a zoned date-time object into an AVRO 'timestamp-millis' value.
     *
     * @param instant the instant
     * @return number of milliseconds from the unix epoch, 1 January 1970 00:00:00.000 UTC
     */
    default Long toAvroTimeStampMillis(final Instant instant) {
        final Long result;
        if (instant == null) {
            result = null;
        } else {
            result = instant.toEpochMilli();
        }
        return result;
    }

    /**
     * Convert an AVRO 'timestamp-millis' value into a zoned date-time object.
     *
     * @param timeStampMillis the number of milliseconds from the unix epoch, 1 January 1970 00:00:00.000 UTC
     * @return a zoned date-time object
     */
    default ZonedDateTime fromAvroTimeStampMillis(final Long timeStampMillis) {
        if (timeStampMillis == null) {
            return null;
        }
        return ZonedDateTime.from(Instant.ofEpochMilli(timeStampMillis));
    }

}
