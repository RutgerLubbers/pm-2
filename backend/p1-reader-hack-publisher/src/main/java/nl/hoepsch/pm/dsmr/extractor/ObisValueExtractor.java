package nl.hoepsch.pm.dsmr.extractor;

import nl.hoepsch.pm.dsmr.model.ObisTagType;

import java.io.Serializable;
import java.util.function.BiFunction;

/**
 * Value extractor for an OBIS tag.
 *
 * @param <T> The type to return.
 */
public interface ObisValueExtractor<T extends Serializable> extends BiFunction<ObisTagType, String, T> {

    /**
     * Extract the tag's value.
     *
     * @param tag  The tag to extract the value for.
     * @param line The line to extract the value from.
     * @return The extracted value.
     */
    @Override
    T apply(ObisTagType tag, String line);
}
