package nl.hoepsch.pm.dsmr.extractor;

import nl.hoepsch.pm.dsmr.model.ObisTagType;

public class ObisTagRawStringValueExtractor implements ObisValueExtractor<String> {

    private static final ObisValueExtractor<String> INSTANCE = new ObisTagRawStringValueExtractor();

    public static ObisValueExtractor<String> instance() {
        return INSTANCE;
    }


    @Override
    public String apply(final ObisTagType tag, final String line) {
        return line.substring(tag.getTagLength());
    }
}
