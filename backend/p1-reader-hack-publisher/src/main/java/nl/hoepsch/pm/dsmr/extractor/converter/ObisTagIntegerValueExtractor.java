package nl.hoepsch.pm.dsmr.extractor.converter;

import nl.hoepsch.pm.dsmr.extractor.ObisTagStringValueExtractor;
import nl.hoepsch.pm.dsmr.extractor.ObisValueExtractor;
import nl.hoepsch.pm.dsmr.model.ObisTagType;

public class ObisTagIntegerValueExtractor implements ObisValueExtractor<Integer> {

    private static final ObisValueExtractor<Integer> INSTANCE = new ObisTagIntegerValueExtractor();

    public static ObisValueExtractor<Integer> instance() {
        return INSTANCE;
    }

    @Override
    public Integer apply(final ObisTagType tag, final String line) {
        String value = ObisTagStringValueExtractor.instance().apply(tag, line);
        return Integer.valueOf(value);
    }
}
