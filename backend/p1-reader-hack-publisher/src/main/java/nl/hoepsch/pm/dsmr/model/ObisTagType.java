package nl.hoepsch.pm.dsmr.model;

public enum ObisTagType {
    EQUIPMENT_IDENTIFICATION("/"),
    VERSION_INFO("1-3:0.2.8"),
    TIME_STAMP("0-0:1.0.0"),
    EQUIPMENT_IDENTIFIER("0-0:96.1.1"),
    ELECTRICITY_DELIVERED_TO_CLIENT_METER_1("1-0:1.8.1", "kWh"),
    ELECTRICITY_DELIVERED_TO_CLIENT_METER_2("1-0:1.8.2", "kWh"),
    ELECTRICITY_DELIVERED_BY_CLIENT_METER_1("1-0:2.8.1", "kWh"),
    ELECTRICITY_DELIVERED_BY_CLIENT_METER_2("1-0:2.8.2", "kWh"),
    TARIFF_INDICATOR("0-0:96.14.0"),
    ELECTRICITY_DELIVERED_TO_CLIENT_ACTUAL("1-0:1.7.0", "kW"),
    ELECTRICITY_DELIVERED_BY_CLIENT_ACTUAL("1-0:2.7.0", "kW"),
    NUMBER_OF_POWER_FAILURES_IN_ANY_PHASE("0-0:96.7.21"),
    NUMBER_OF_LONG_POWER_FAILURES_IN_ANY_PHASE("0-0:96.7.9"),
    POWER_FAILURE_EVENT_LOG("1-0:99.97.0"),
    NUMBER_OF_VOLTAGE_SAGS_L1("1-0:32.32.0"),
    NUMBER_OF_VOLTAGE_SAGS_L2("1-0:52.32.0"),
    NUMBER_OF_VOLTAGE_SAGS_L3("1-0:72.32.0"),
    NUMBER_OF_VOLTAGE_SWELLS_L1("1-0:32.36.0"),
    NUMBER_OF_VOLTAGE_SWELLS_L2("1-0:52.36.0"),
    NUMBER_OF_VOLTAGE_SWELLS_L3("1-0:72.36.0"),
    TEXT_MESSAGE("0-0:96.13.0"),
    INSTANTANEOUS_VOLTAGE_L1("1-0:32.7.0", "V"),
    INSTANTANEOUS_VOLTAGE_L2("1-0:52.7.0", "V"),
    INSTANTANEOUS_VOLTAGE_L3("1-0:72.7.0", "V"),
    INSTANTANEOUS_CURRENT_L1("1-0:31.7.0", "A"),
    INSTANTANEOUS_CURRENT_L2("1-0:51.7.0", "A"),
    INSTANTANEOUS_CURRENT_L3("1-0:71.7.0", "A"),
    INSTANTANEOUS_POWER_DELIVERED_TO_CLIENT_L1("1-0:21.7.0", "kW"),
    INSTANTANEOUS_POWER_DELIVERED_TO_CLIENT_L2("1-0:41.7.0", "kW"),
    INSTANTANEOUS_POWER_DELIVERED_TO_CLIENT_L3("1-0:61.7.0", "kW"),
    INSTANTANEOUS_POWER_DELIVERED_BY_CLIENT_L1("1-0:22.7.0", "kW"),
    INSTANTANEOUS_POWER_DELIVERED_BY_CLIENT_L2("1-0:42.7.0", "kW"),
    INSTANTANEOUS_POWER_DELIVERED_BY_CLIENT_L3("1-0:62.7.0", "kW"),
    GAS_METER_CHANNEL("0-1:24.1.0"),
    GAS_METER_IDENTIFIER("0-1:96.1.0"),
    GAS_METER_TIME_STAMP("0-1:24.2.1"),
    GAS_DELIVERED_TO_CLIENT("0-1:24.2.1", "m3"),
    ;

    private final String tag;
    private final String valueUnit;

    ObisTagType(final String value) {
        this(value, "");
    }

    ObisTagType(final String tag, final String valueUnit) {
        this.tag = tag;
        this.valueUnit = valueUnit;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public String getTag() {
        return tag;
    }

    public int getTagLength() {
        return tag.length();
    }

    @SuppressWarnings("PMD.CommentRequired")
    public String getValueUnit() {
        return valueUnit;
    }

    public int getValueUnitLength() {
        return valueUnit.length();
    }

    public boolean isUsedBy(final String line) {
        return line.startsWith(tag);
    }
}
