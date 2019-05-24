package nl.hoepsch.pm.dsmr.model;

/**
 * DSMR readout units.
 */
public enum ObisTagUnit {
    NONE(""),
    AMPERE("A"),
    CUBIC_METER("m3"),
    KILO_WATT("kW"),
    KILO_WATT_HOUR("kWh"),
    VOLT("V");

    /**
     * The tag's unit.
     */
    private final String unit;

    /**
     * The constructor.
     *
     * @param unit The tag's unit.
     */
    ObisTagUnit(final String unit) {
        this.unit = unit;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public String getUnit() {
        return unit;
    }

    /**
     * Returns the (String) length of the tag's unit.
     *
     * @return The length.
     */
    public int length() {
        return unit.length();
    }
}
