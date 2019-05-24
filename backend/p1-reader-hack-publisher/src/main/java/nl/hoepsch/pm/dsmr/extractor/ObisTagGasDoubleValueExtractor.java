package nl.hoepsch.pm.dsmr.extractor;

import nl.hoepsch.pm.dsmr.model.ObisTagType;

/**
 * Value extractor for an OBIS tag.
 */
public class ObisTagGasDoubleValueExtractor implements ObisValueExtractor<String> {

    /**
     * The instance.
     */
    private static final ObisValueExtractor<String> INSTANCE = new ObisTagGasDoubleValueExtractor();

    /**
     * Retrieves the instance.
     *
     * @return the instance.
     */
    public static ObisValueExtractor<String> getInstance() {
        return INSTANCE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String apply(final ObisTagType tag, final String line) {
        return line.substring(line.lastIndexOf('(') + 1, line.lastIndexOf(')'));
    }
}
