package nl.hoepsch.pm.dsmr.extractor.converter;

import nl.hoepsch.pm.dsmr.extractor.ObisTagStringValueExtractor;
import nl.hoepsch.pm.dsmr.extractor.ObisValueExtractor;
import nl.hoepsch.pm.dsmr.model.ObisTagType;

import java.time.LocalDateTime;

public class ObisTagTimestampValueExtractor implements ObisValueExtractor<LocalDateTime> {

    private static final ObisValueExtractor<LocalDateTime> INSTANCE = new ObisTagTimestampValueExtractor();
    private static final ObisValueExtractor<String> STRING_EXTRACTOR = ObisTagStringValueExtractor.instance();

    public static ObisValueExtractor<LocalDateTime> instance() {
        return INSTANCE;
    }

    @Override
    public LocalDateTime apply(final ObisTagType tag, final String line) {
        return DsmrTimestampToLocalDateTimeConverter.toDateTime(STRING_EXTRACTOR.apply(tag, line));
    }
}
