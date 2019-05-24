package nl.hoepsch.pm.dsmr.model;

import static nl.hoepsch.pm.dsmr.model.ObisTagUnit.AMPERE;
import static nl.hoepsch.pm.dsmr.model.ObisTagUnit.CUBIC_METER;
import static nl.hoepsch.pm.dsmr.model.ObisTagUnit.KILO_WATT;
import static nl.hoepsch.pm.dsmr.model.ObisTagUnit.KILO_WATT_HOUR;
import static nl.hoepsch.pm.dsmr.model.ObisTagUnit.NONE;
import static nl.hoepsch.pm.dsmr.model.ObisTagUnit.VOLT;

/**
 * Enumeration for the different OBIS tags used by DSMR.
 */
@SuppressWarnings("checkstyle:MultipleStringLiterals")
public enum ObisTagType {
    EQUIPMENT_IDENTIFICATION("/"),
    VERSION_INFO("1-3:0.2.8"),
    TIME_STAMP("0-0:1.0.0"),
    EQUIPMENT_IDENTIFIER("0-0:96.1.1"),
    ELECTRICITY_DELIVERED_TO_CLIENT_METER_1("1-0:1.8.1", KILO_WATT_HOUR),
    ELECTRICITY_DELIVERED_TO_CLIENT_METER_2("1-0:1.8.2", KILO_WATT_HOUR),
    ELECTRICITY_DELIVERED_BY_CLIENT_METER_1("1-0:2.8.1", KILO_WATT_HOUR),
    ELECTRICITY_DELIVERED_BY_CLIENT_METER_2("1-0:2.8.2", KILO_WATT_HOUR),
    TARIFF_INDICATOR("0-0:96.14.0"),
    ELECTRICITY_DELIVERED_TO_CLIENT_ACTUAL("1-0:1.7.0", KILO_WATT),
    ELECTRICITY_DELIVERED_BY_CLIENT_ACTUAL("1-0:2.7.0", KILO_WATT),
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
    INSTANTANEOUS_VOLTAGE_L1("1-0:32.7.0", VOLT),
    INSTANTANEOUS_VOLTAGE_L2("1-0:52.7.0", VOLT),
    INSTANTANEOUS_VOLTAGE_L3("1-0:72.7.0", VOLT),
    INSTANTANEOUS_CURRENT_L1("1-0:31.7.0", AMPERE),
    INSTANTANEOUS_CURRENT_L2("1-0:51.7.0", AMPERE),
    INSTANTANEOUS_CURRENT_L3("1-0:71.7.0", AMPERE),
    INSTANTANEOUS_POWER_DELIVERED_TO_CLIENT_L1("1-0:21.7.0", KILO_WATT),
    INSTANTANEOUS_POWER_DELIVERED_TO_CLIENT_L2("1-0:41.7.0", KILO_WATT),
    INSTANTANEOUS_POWER_DELIVERED_TO_CLIENT_L3("1-0:61.7.0", KILO_WATT),
    INSTANTANEOUS_POWER_DELIVERED_BY_CLIENT_L1("1-0:22.7.0", KILO_WATT),
    INSTANTANEOUS_POWER_DELIVERED_BY_CLIENT_L2("1-0:42.7.0", KILO_WATT),
    INSTANTANEOUS_POWER_DELIVERED_BY_CLIENT_L3("1-0:62.7.0", KILO_WATT),
    GAS_METER_CHANNEL("0-1:24.1.0"),
    GAS_METER_IDENTIFIER("0-1:96.1.0"),
    GAS_METER_TIME_STAMP("0-1:24.2.1"),
    GAS_DELIVERED_TO_CLIENT("0-1:24.2.1", CUBIC_METER);

    /**
     * The tag.
     */
    private final String tag;

    /**
     * The tag's unit.
     */
    private final ObisTagUnit unit;

    /**
     * Constructor with a tag name.
     *
     * @param tag The tag.
     */
    ObisTagType(final String tag) {
        this(tag, NONE);
    }

    /**
     * Constructor.
     *
     * @param tag  The tag.
     * @param unit The tag's unit.
     */
    ObisTagType(final String tag, final ObisTagUnit unit) {
        this.tag = tag;
        this.unit = unit;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public String getTag() {
        return tag;
    }

    /**
     * Returns the tag's length.
     *
     * @return The length.
     */
    public int getTagLength() {
        return tag.length();
    }

    @SuppressWarnings("PMD.CommentRequired")
    public ObisTagUnit getValueUnit() {
        return unit;
    }

    /**
     * Returns the tag unit's length.
     *
     * @return The length.
     */
    public int getValueUnitLength() {
        return unit.length();
    }

    /**
     * Is the input {@code line} used by this tag?
     *
     * @param line The line.
     * @return true or false
     */
    public boolean isUsedBy(final String line) {
        return line.startsWith(tag);
    }
}
