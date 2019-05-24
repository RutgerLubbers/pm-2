package nl.hoepsch.pm.dsmr.extractor;

import nl.hoepsch.pm.dsmr.model.ObisTagType;

public class ObisTagGasDoubleValueExtractor implements ObisValueExtractor<String> {

    private static final ObisValueExtractor<String> INSTANCE = new ObisTagGasDoubleValueExtractor();

    public static ObisValueExtractor<String> instance() {
        return INSTANCE;
    }

    @Override
    public String apply(final ObisTagType tag, final String line) {
        return line.substring(line.lastIndexOf('(') + 1, line.lastIndexOf(')'));
    }
}
