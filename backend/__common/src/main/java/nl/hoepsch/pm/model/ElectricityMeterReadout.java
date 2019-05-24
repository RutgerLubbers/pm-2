package nl.hoepsch.pm.model;

import java.time.LocalDateTime;

/**
 * The electricity meter's readout.
 */
public class ElectricityMeterReadout {

    /**
     * The meter that was read.
     */
    private Meter meter;

    /**
     * The timestamp of the event.
     */
    private LocalDateTime timestamp;

    /**
     * The timestamp of the actual readout.
     */
    private LocalDateTime readoutTimestamp;

    /**
     * The day rate meter's value.
     */
    private ElectricityReadout dayRateValue;

    /**
     * The night rate meter's value.
     */
    private ElectricityReadout nightRateValue;

    @SuppressWarnings("PMD.CommentRequired")
    public Meter getMeter() {
        return meter;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public void setMeter(final Meter meter) {
        this.meter = meter;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public void setTimestamp(final LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public LocalDateTime getReadoutTimestamp() {
        return readoutTimestamp;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public void setReadoutTimestamp(final LocalDateTime readoutTimestamp) {
        this.readoutTimestamp = readoutTimestamp;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public ElectricityReadout getDayRateValue() {
        return dayRateValue;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public void setDayRateValue(final ElectricityReadout dayRateValue) {
        this.dayRateValue = dayRateValue;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public ElectricityReadout getNightRateValue() {
        return nightRateValue;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public void setNightRateValue(final ElectricityReadout nightRateValue) {
        this.nightRateValue = nightRateValue;
    }
}
