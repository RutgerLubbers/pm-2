package nl.hoepsch.pm.dsmr.extractor.converter;

import nl.hoepsch.pm.dsmr.extractor.ObisTagStringValueExtractor;
import nl.hoepsch.pm.dsmr.extractor.ObisValueExtractor;
import nl.hoepsch.pm.dsmr.model.ObisTagType;

public class ObisTagDoubleValueExtractor implements ObisValueExtractor<Double> {

    private static final ObisValueExtractor<Double> INSTANCE = new ObisTagDoubleValueExtractor();

    public static ObisValueExtractor<Double> instance() {
        return INSTANCE;
    }

    @Override
    public Double apply(final ObisTagType tag, final String line) {
        String value = ObisTagStringValueExtractor.instance().apply(tag, line);
        return Double.valueOf(value);
    }
}
