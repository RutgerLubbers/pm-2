package nl.hoepsch.pm.dsmr.extractor;

import nl.hoepsch.pm.dsmr.model.ObisTag;
import nl.hoepsch.pm.dsmr.model.ObisTagType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static nl.hoepsch.pm.dsmr.model.ObisTagType.ELECTRICITY_DELIVERED_BY_CLIENT_ACTUAL;
import static nl.hoepsch.pm.dsmr.model.ObisTagType.ELECTRICITY_DELIVERED_BY_CLIENT_METER_1;
import static nl.hoepsch.pm.dsmr.model.ObisTagType.ELECTRICITY_DELIVERED_BY_CLIENT_METER_2;
import static nl.hoepsch.pm.dsmr.model.ObisTagType.ELECTRICITY_DELIVERED_TO_CLIENT_ACTUAL;
import static nl.hoepsch.pm.dsmr.model.ObisTagType.ELECTRICITY_DELIVERED_TO_CLIENT_METER_1;
import static nl.hoepsch.pm.dsmr.model.ObisTagType.ELECTRICITY_DELIVERED_TO_CLIENT_METER_2;
import static nl.hoepsch.pm.dsmr.model.ObisTagType.EQUIPMENT_IDENTIFICATION;
import static nl.hoepsch.pm.dsmr.model.ObisTagType.EQUIPMENT_IDENTIFIER;
import static nl.hoepsch.pm.dsmr.model.ObisTagType.GAS_DELIVERED_TO_CLIENT;
import static nl.hoepsch.pm.dsmr.model.ObisTagType.GAS_METER_CHANNEL;
import static nl.hoepsch.pm.dsmr.model.ObisTagType.GAS_METER_IDENTIFIER;
import static nl.hoepsch.pm.dsmr.model.ObisTagType.GAS_METER_TIME_STAMP;
import static nl.hoepsch.pm.dsmr.model.ObisTagType.INSTANTANEOUS_CURRENT_L1;
import static nl.hoepsch.pm.dsmr.model.ObisTagType.INSTANTANEOUS_CURRENT_L2;
import static nl.hoepsch.pm.dsmr.model.ObisTagType.INSTANTANEOUS_CURRENT_L3;
import static nl.hoepsch.pm.dsmr.model.ObisTagType.INSTANTANEOUS_POWER_DELIVERED_BY_CLIENT_L1;
import static nl.hoepsch.pm.dsmr.model.ObisTagType.INSTANTANEOUS_POWER_DELIVERED_BY_CLIENT_L2;
import static nl.hoepsch.pm.dsmr.model.ObisTagType.INSTANTANEOUS_POWER_DELIVERED_BY_CLIENT_L3;
import static nl.hoepsch.pm.dsmr.model.ObisTagType.INSTANTANEOUS_POWER_DELIVERED_TO_CLIENT_L1;
import static nl.hoepsch.pm.dsmr.model.ObisTagType.INSTANTANEOUS_POWER_DELIVERED_TO_CLIENT_L2;
import static nl.hoepsch.pm.dsmr.model.ObisTagType.INSTANTANEOUS_POWER_DELIVERED_TO_CLIENT_L3;
import static nl.hoepsch.pm.dsmr.model.ObisTagType.INSTANTANEOUS_VOLTAGE_L1;
import static nl.hoepsch.pm.dsmr.model.ObisTagType.INSTANTANEOUS_VOLTAGE_L2;
import static nl.hoepsch.pm.dsmr.model.ObisTagType.INSTANTANEOUS_VOLTAGE_L3;
import static nl.hoepsch.pm.dsmr.model.ObisTagType.NUMBER_OF_LONG_POWER_FAILURES_IN_ANY_PHASE;
import static nl.hoepsch.pm.dsmr.model.ObisTagType.NUMBER_OF_POWER_FAILURES_IN_ANY_PHASE;
import static nl.hoepsch.pm.dsmr.model.ObisTagType.NUMBER_OF_VOLTAGE_SAGS_L1;
import static nl.hoepsch.pm.dsmr.model.ObisTagType.NUMBER_OF_VOLTAGE_SAGS_L2;
import static nl.hoepsch.pm.dsmr.model.ObisTagType.NUMBER_OF_VOLTAGE_SAGS_L3;
import static nl.hoepsch.pm.dsmr.model.ObisTagType.NUMBER_OF_VOLTAGE_SWELLS_L1;
import static nl.hoepsch.pm.dsmr.model.ObisTagType.NUMBER_OF_VOLTAGE_SWELLS_L2;
import static nl.hoepsch.pm.dsmr.model.ObisTagType.NUMBER_OF_VOLTAGE_SWELLS_L3;
import static nl.hoepsch.pm.dsmr.model.ObisTagType.POWER_FAILURE_EVENT_LOG;
import static nl.hoepsch.pm.dsmr.model.ObisTagType.TARIFF_INDICATOR;
import static nl.hoepsch.pm.dsmr.model.ObisTagType.TEXT_MESSAGE;
import static nl.hoepsch.pm.dsmr.model.ObisTagType.TIME_STAMP;
import static nl.hoepsch.pm.dsmr.model.ObisTagType.VERSION_INFO;

/**
 * Parses a line into a set of {@link ObisTag}.
 */
@SuppressWarnings({"checkstyle:ExecutableStatementCount", "PMD.ExcessiveImports"})
public final class ObisTagValueParser {

    /**
     * The map with the extractors per OBIS tag.
     */
    private static final Map<ObisTagType, ObisValueExtractor<String>> EXTRACTORS = new HashMap<>();

    /**
     * Utility constructor.
     */
    private ObisTagValueParser() {
        // Do nothing.
    }

    static {
        EXTRACTORS.put(EQUIPMENT_IDENTIFICATION, ObisTagStringValueExtractor.getInstance());
        EXTRACTORS.put(VERSION_INFO, ObisTagStringValueExtractor.getInstance());
        EXTRACTORS.put(TIME_STAMP, ObisTagStringValueExtractor.getInstance());
        EXTRACTORS.put(EQUIPMENT_IDENTIFIER, ObisTagStringValueExtractor.getInstance());
        EXTRACTORS.put(ELECTRICITY_DELIVERED_TO_CLIENT_METER_1, ObisTagStringValueExtractor.getInstance());
        EXTRACTORS.put(ELECTRICITY_DELIVERED_TO_CLIENT_METER_2, ObisTagStringValueExtractor.getInstance());
        EXTRACTORS.put(ELECTRICITY_DELIVERED_BY_CLIENT_METER_1, ObisTagStringValueExtractor.getInstance());
        EXTRACTORS.put(ELECTRICITY_DELIVERED_BY_CLIENT_METER_2, ObisTagStringValueExtractor.getInstance());
        EXTRACTORS.put(TARIFF_INDICATOR, ObisTagStringValueExtractor.getInstance());
        EXTRACTORS.put(ELECTRICITY_DELIVERED_TO_CLIENT_ACTUAL, ObisTagStringValueExtractor.getInstance());
        EXTRACTORS.put(ELECTRICITY_DELIVERED_BY_CLIENT_ACTUAL, ObisTagStringValueExtractor.getInstance());
        EXTRACTORS.put(NUMBER_OF_POWER_FAILURES_IN_ANY_PHASE, ObisTagStringValueExtractor.getInstance());
        EXTRACTORS.put(NUMBER_OF_LONG_POWER_FAILURES_IN_ANY_PHASE, ObisTagStringValueExtractor.getInstance());
        EXTRACTORS.put(POWER_FAILURE_EVENT_LOG, ObisTagRawStringValueExtractor.getInstance());
        EXTRACTORS.put(NUMBER_OF_VOLTAGE_SAGS_L1, ObisTagStringValueExtractor.getInstance());
        EXTRACTORS.put(NUMBER_OF_VOLTAGE_SAGS_L2, ObisTagStringValueExtractor.getInstance());
        EXTRACTORS.put(NUMBER_OF_VOLTAGE_SAGS_L3, ObisTagStringValueExtractor.getInstance());
        EXTRACTORS.put(NUMBER_OF_VOLTAGE_SWELLS_L1, ObisTagStringValueExtractor.getInstance());
        EXTRACTORS.put(NUMBER_OF_VOLTAGE_SWELLS_L2, ObisTagStringValueExtractor.getInstance());
        EXTRACTORS.put(NUMBER_OF_VOLTAGE_SWELLS_L3, ObisTagStringValueExtractor.getInstance());
        EXTRACTORS.put(TEXT_MESSAGE, ObisTagStringValueExtractor.getInstance());
        EXTRACTORS.put(INSTANTANEOUS_VOLTAGE_L1, ObisTagStringValueExtractor.getInstance());
        EXTRACTORS.put(INSTANTANEOUS_VOLTAGE_L2, ObisTagStringValueExtractor.getInstance());
        EXTRACTORS.put(INSTANTANEOUS_VOLTAGE_L3, ObisTagStringValueExtractor.getInstance());
        EXTRACTORS.put(INSTANTANEOUS_CURRENT_L1, ObisTagStringValueExtractor.getInstance());
        EXTRACTORS.put(INSTANTANEOUS_CURRENT_L2, ObisTagStringValueExtractor.getInstance());
        EXTRACTORS.put(INSTANTANEOUS_CURRENT_L3, ObisTagStringValueExtractor.getInstance());
        EXTRACTORS.put(INSTANTANEOUS_POWER_DELIVERED_TO_CLIENT_L1, ObisTagStringValueExtractor.getInstance());
        EXTRACTORS.put(INSTANTANEOUS_POWER_DELIVERED_TO_CLIENT_L2, ObisTagStringValueExtractor.getInstance());
        EXTRACTORS.put(INSTANTANEOUS_POWER_DELIVERED_TO_CLIENT_L3, ObisTagStringValueExtractor.getInstance());
        EXTRACTORS.put(INSTANTANEOUS_POWER_DELIVERED_BY_CLIENT_L1, ObisTagStringValueExtractor.getInstance());
        EXTRACTORS.put(INSTANTANEOUS_POWER_DELIVERED_BY_CLIENT_L2, ObisTagStringValueExtractor.getInstance());
        EXTRACTORS.put(INSTANTANEOUS_POWER_DELIVERED_BY_CLIENT_L3, ObisTagStringValueExtractor.getInstance());
        EXTRACTORS.put(GAS_METER_CHANNEL, ObisTagStringValueExtractor.getInstance());
        EXTRACTORS.put(GAS_METER_IDENTIFIER, ObisTagStringValueExtractor.getInstance());
        EXTRACTORS.put(GAS_METER_TIME_STAMP, ObisTagGasTimestampValueExtractor.getInstance());
        EXTRACTORS.put(GAS_DELIVERED_TO_CLIENT, ObisTagGasDoubleValueExtractor.getInstance());
    }

    /**
     * Parses the line into a set of {@link ObisTag}.
     *
     * @param line The line to parse.
     * @return The collection of tags.
     */
    @SuppressWarnings({"PMD.LawOfDemeter", "PMD.AvoidInstantiatingObjectsInLoops"})
    public static Collection<ObisTag> parse(final String line) {
        final List<ObisTag> results = new ArrayList<>();
        for (final ObisTagType tag : ObisTagType.values()) {
            if (tag.isUsedBy(line)) {
                final ObisValueExtractor<String> extractor = EXTRACTORS.get(tag);
                results.add(new ObisTag(tag, extractor.apply(tag, line)));
            }
        }

        return results;
    }
}
