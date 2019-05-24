package nl.hoepsch.pm.dsmr.converter;

import nl.hoepsch.pm.dsmr.model.ObisTag;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * Value extractor for an OBIS tag.
 */
@Component
public class ObisTagTimestampValueExtractor implements TagValueExtractor<ZonedDateTime> {

    /**
     * {@inheritDoc}
     */
    @Override
    public ZonedDateTime extract(final ObisTag obisTag) {
        return DsmrTimestampToZonedDateTimeConverter.toDateTime(obisTag.getValue(), ZoneId.of("Europe/Amsterdam"));
    }
}
