package nl.hoepsch.pm.dsmr.model;

import nl.hoepsch.pm.dsmr.extractor.ObisTagGasDoubleValueExtractor;
import nl.hoepsch.pm.dsmr.extractor.ObisTagGasTimestampValueExtractor;
import nl.hoepsch.pm.dsmr.extractor.ObisTagRawStringValueExtractor;
import nl.hoepsch.pm.dsmr.extractor.ObisTagStringValueExtractor;
import nl.hoepsch.pm.dsmr.extractor.ObisValueExtractor;

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

public class ObisTagValueExtractorRepository {

    private static final Map<ObisTagType, ObisValueExtractor<String>> EXTRACTORS = new HashMap<>();

    static {
        EXTRACTORS.put(EQUIPMENT_IDENTIFICATION, ObisTagStringValueExtractor.instance());
        EXTRACTORS.put(VERSION_INFO, ObisTagStringValueExtractor.instance());
        EXTRACTORS.put(TIME_STAMP, ObisTagStringValueExtractor.instance());
        EXTRACTORS.put(EQUIPMENT_IDENTIFIER, ObisTagStringValueExtractor.instance());
        EXTRACTORS.put(ELECTRICITY_DELIVERED_TO_CLIENT_METER_1, ObisTagStringValueExtractor.instance());
        EXTRACTORS.put(ELECTRICITY_DELIVERED_TO_CLIENT_METER_2, ObisTagStringValueExtractor.instance());
        EXTRACTORS.put(ELECTRICITY_DELIVERED_BY_CLIENT_METER_1, ObisTagStringValueExtractor.instance());
        EXTRACTORS.put(ELECTRICITY_DELIVERED_BY_CLIENT_METER_2, ObisTagStringValueExtractor.instance());
        EXTRACTORS.put(TARIFF_INDICATOR, ObisTagStringValueExtractor.instance());
        EXTRACTORS.put(ELECTRICITY_DELIVERED_TO_CLIENT_ACTUAL, ObisTagStringValueExtractor.instance());
        EXTRACTORS.put(ELECTRICITY_DELIVERED_BY_CLIENT_ACTUAL, ObisTagStringValueExtractor.instance());
        EXTRACTORS.put(NUMBER_OF_POWER_FAILURES_IN_ANY_PHASE, ObisTagStringValueExtractor.instance());
        EXTRACTORS.put(NUMBER_OF_LONG_POWER_FAILURES_IN_ANY_PHASE, ObisTagStringValueExtractor.instance());
        EXTRACTORS.put(POWER_FAILURE_EVENT_LOG, ObisTagRawStringValueExtractor.instance());
        EXTRACTORS.put(NUMBER_OF_VOLTAGE_SAGS_L1, ObisTagStringValueExtractor.instance());
        EXTRACTORS.put(NUMBER_OF_VOLTAGE_SAGS_L2, ObisTagStringValueExtractor.instance());
        EXTRACTORS.put(NUMBER_OF_VOLTAGE_SAGS_L3, ObisTagStringValueExtractor.instance());
        EXTRACTORS.put(NUMBER_OF_VOLTAGE_SWELLS_L1, ObisTagStringValueExtractor.instance());
        EXTRACTORS.put(NUMBER_OF_VOLTAGE_SWELLS_L2, ObisTagStringValueExtractor.instance());
        EXTRACTORS.put(NUMBER_OF_VOLTAGE_SWELLS_L3, ObisTagStringValueExtractor.instance());
        EXTRACTORS.put(TEXT_MESSAGE, ObisTagStringValueExtractor.instance());
        EXTRACTORS.put(INSTANTANEOUS_VOLTAGE_L1, ObisTagStringValueExtractor.instance());
        EXTRACTORS.put(INSTANTANEOUS_VOLTAGE_L2, ObisTagStringValueExtractor.instance());
        EXTRACTORS.put(INSTANTANEOUS_VOLTAGE_L3, ObisTagStringValueExtractor.instance());
        EXTRACTORS.put(INSTANTANEOUS_CURRENT_L1, ObisTagStringValueExtractor.instance());
        EXTRACTORS.put(INSTANTANEOUS_CURRENT_L2, ObisTagStringValueExtractor.instance());
        EXTRACTORS.put(INSTANTANEOUS_CURRENT_L3, ObisTagStringValueExtractor.instance());
        EXTRACTORS.put(INSTANTANEOUS_POWER_DELIVERED_TO_CLIENT_L1, ObisTagStringValueExtractor.instance());
        EXTRACTORS.put(INSTANTANEOUS_POWER_DELIVERED_TO_CLIENT_L2, ObisTagStringValueExtractor.instance());
        EXTRACTORS.put(INSTANTANEOUS_POWER_DELIVERED_TO_CLIENT_L3, ObisTagStringValueExtractor.instance());
        EXTRACTORS.put(INSTANTANEOUS_POWER_DELIVERED_BY_CLIENT_L1, ObisTagStringValueExtractor.instance());
        EXTRACTORS.put(INSTANTANEOUS_POWER_DELIVERED_BY_CLIENT_L2, ObisTagStringValueExtractor.instance());
        EXTRACTORS.put(INSTANTANEOUS_POWER_DELIVERED_BY_CLIENT_L3, ObisTagStringValueExtractor.instance());
        EXTRACTORS.put(GAS_METER_CHANNEL, ObisTagStringValueExtractor.instance());
        EXTRACTORS.put(GAS_METER_IDENTIFIER, ObisTagStringValueExtractor.instance());
        EXTRACTORS.put(GAS_METER_TIME_STAMP, ObisTagGasTimestampValueExtractor.instance());
        EXTRACTORS.put(GAS_DELIVERED_TO_CLIENT, ObisTagGasDoubleValueExtractor.instance());
    }

    public static Collection<ObisTag> parse(final String line) {
       final List<ObisTag> results = new ArrayList<>();
        for (ObisTagType tag : ObisTagType.values()) {
            if (tag.isUsedBy(line)) {
                final ObisValueExtractor<String> extractor = EXTRACTORS.get(tag);
                results.add(new ObisTag(tag, extractor.apply(tag, line)));
            }
        }

        return results;
    }
}
