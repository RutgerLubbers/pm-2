package nl.hoepsch.pm.dsmr.extractor;

import nl.hoepsch.pm.dsmr.model.ObisTagType;

import java.time.LocalDateTime;

public class ObisTagGasTimestampValueExtractor implements ObisValueExtractor<String> {

    private static final ObisValueExtractor<String> INSTANCE = new ObisTagGasTimestampValueExtractor();

    public static ObisValueExtractor<String> instance() {
        return INSTANCE;
    }

    @Override
    public String apply(final ObisTagType tag, final String line) {
        return line.substring(line.indexOf('(') + 1, line.indexOf(')'));
    }
}
