package nl.hoepsch.pm.model;

import java.time.LocalDateTime;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

/**
 * Class that holds the field numbers for the record read from the meter.
 *
 * line ::=  "timestamp" "meter_id" "meter_name" value
 * value :== gas_meter_value || electricity_meter_value || electricity_current_readout
 * <p>
 * gas_meter_value ::= "logdate" gas_logtime "gas meter value"
 * gas_logtime ::= HHMMSS, mostly HH0000
 * <p>
 * electricity_meter_value ::= day_rate night_rate
 * day_rate ::= rate
 * night_rate ::= rate
 * rate ::= "usage" "supply"
 * <p>
 * electricity_current_readout :== rate
 */
@SuppressWarnings("PMD.UnusedPrivateField")
public class Fields {

    /**
     * The readout's timestamp.
     */
    private static final int TIMESTAMP = 0;

    /**
     * The meter's id.
     */
    private static final int METER_ID = 1;

    /**
     * The meter's name.
     */
    private static final int METER_NAME = 2;

    /*
     * Fields for 'gas_meter_value'.
     */
    /**
     * The gas meter's readout date.
     */
    private static final int GAS_READ_DATE = 3;

    /**
     * The gas meter's readout time.
     */
    private static final int GAS_READ_TIME = 4;

    /**
     * The gas meter's read value.
     */
    private static final int GAS_METER_READOUT = 5;

    /*
     * Fields for 'electricity_meter_value'.
     */
    /**
     * The electricity meter's day rate usage.
     */
    private static final int DAY_RATE_USAGE = 3;

    /**
     * The electricity meter's day rate supply.
     */
    private static final int DAY_RATE_SUPPLY = 4;

    /**
     * The electricity meter's night rate usage.
     */
    private static final int NIGHT_RATE_USAGE = 5;

    /**
     * The electricity meter's night rate usage.
     */
    private static final int NIGHT_RATE_SUPPLY = 6;

    /*
     * Fields for 'electricity_current_readout'.
     */
    /**
     * The electricity meter's current usage.
     */
    private static final int CURRENT_USAGE = 3;

    /**
     * The electricity meter's current supply.
     */
    private static final int CURRENT_SUPPLY = 4;

    /**
     * The values read from the record.
     */
    private final String[] values;

    /**
     * Constructor for a tab separated set of fields in one string.
     */
    public Fields(final String record) {
        this(requireNonNull(record).split("\t"));
    }

    /**
     * Constructor for a array of values.
     */
    public Fields(final String... values) {
        this.values = requireNonNull(values);
    }

    /**
     * Return the event's timestamp.
     */
    @SuppressWarnings("PMD.LawOfDemeter")
    public LocalDateTime getTimestamp() {
        return LocalDateTime.parse(values[TIMESTAMP]);
    }

    /**
     * Returns the meter's id.
     */
    public String getMeterId() {
        return values[METER_ID];
    }

    /**
     * Returns the meter's name.
     */
    public String getMeterName() {
        return values[METER_NAME];
    }

    /**
     * Returns the readout timestamp.
     */
    @SuppressWarnings("PMD.LawOfDemeter")
    public LocalDateTime getReadoutTimestamp() {
        final String readTime = normalizeTime(values[GAS_READ_TIME]);

        final String timestamp = format("%sT%s:%s:%s", values[GAS_READ_DATE], readTime.subSequence(0, 2), readTime.subSequence(2, 4),
                readTime.subSequence(4, 6));
        return LocalDateTime.parse(timestamp);
    }

    /*
     * Transformation:
     * <pre>
     *          0   000000
     *      10000   010000
     *     230000   230000
     * </pre>
     */
    @SuppressWarnings("PMD.LawOfDemeter")
    private String normalizeTime(final String input) {
        final String rightPadded = format("%-5s", input);
        final String leftPadded = format("%1$6s", rightPadded);
        return leftPadded.replace(' ', '0');
    }

    /**
     * Returns the gas meter's value.
     */
    @SuppressWarnings("PMD.LawOfDemeter")
    public Long getGasMeterValue() {
        return Long.parseLong(values[GAS_METER_READOUT]);
    }


}
