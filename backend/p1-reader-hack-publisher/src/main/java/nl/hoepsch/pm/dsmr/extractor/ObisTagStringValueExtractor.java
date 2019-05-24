package nl.hoepsch.pm.dsmr.extractor;

import nl.hoepsch.pm.dsmr.model.ObisTagType;

public class ObisTagStringValueExtractor implements ObisValueExtractor<String> {

    private static final ObisValueExtractor<String> INSTANCE = new ObisTagStringValueExtractor();

    public static ObisValueExtractor<String> instance() {
        return INSTANCE;
    }


    @Override
    public String apply(final ObisTagType tag, final String line) {
        int unitLength = tag.getValueUnit().length();
        if (unitLength > 0) {
            unitLength++;
        }
        return line.substring(tag.getTagLength() + 1, line.length() - 1 - unitLength);
    }
}
