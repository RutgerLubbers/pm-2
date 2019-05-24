package nl.hoepsch.pm.dsmr.extractor;

import nl.hoepsch.pm.dsmr.model.ObisTagType;

import java.io.Serializable;
import java.util.function.BiFunction;

public interface ObisValueExtractor<T extends Serializable> extends BiFunction<ObisTagType, String, T> {

    T apply(ObisTagType tag, String line);
}
