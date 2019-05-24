package nl.hoepsch.pm.dsmr.extractor.converter;

import nl.hoepsch.pm.dsmr.extractor.ObisTagStringValueExtractor;
import nl.hoepsch.pm.dsmr.extractor.ObisValueExtractor;
import nl.hoepsch.pm.dsmr.model.ObisTagType;

import java.time.LocalDateTime;

/**
 * Value extractor for an OBIS tag.
 */
public class ObisTagTimestampValueExtractor implements ObisValueExtractor<LocalDateTime> {

    /**
     * The instance.
     */
    private static final ObisValueExtractor<LocalDateTime> INSTANCE = new ObisTagTimestampValueExtractor();

    /**
     * The string value extractor.
     */
    private static final ObisValueExtractor<String> STRING_EXTRACTOR = ObisTagStringValueExtractor.getInstance();

    /**
     * Retrieves the instance.
     *
     * @return The instance.
     */
    public static ObisValueExtractor<LocalDateTime> getInstance() {
        return INSTANCE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocalDateTime apply(final ObisTagType tag, final String line) {
        return DsmrTimestampToLocalDateTimeConverter.toDateTime(STRING_EXTRACTOR.apply(tag, line));
    }
}
