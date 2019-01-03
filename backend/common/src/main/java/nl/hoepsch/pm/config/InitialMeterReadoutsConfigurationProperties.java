package nl.hoepsch.pm.config;

import java.time.LocalDateTime;

/**
 * Configuration for a meter's initial settings / readout.
 */
public class InitialMeterReadoutsConfigurationProperties {

    /**
     * The meter's id.
     */
    private String meterId;

    /**
     * The meter's name.
     */
    private String meterName;

    /**
     * The timestamp of the initial setting. (When is this readout done?)
     */
    private String timestamp;

    /**
     * The electricity day rate readout.
     */
    private ElectricityRateMeterConfigurationProperties electricityDayRateMeter;

    /**
     * The electricity night rate readout.
     */
    private ElectricityRateMeterConfigurationProperties electricityNightRateMeter;

    /**
     * The gas readout.
     */
    private Long gasMeter;

    @SuppressWarnings("PMD.CommentRequired")
    public String getMeterId() {
        return meterId;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public void setMeterId(final String meterId) {
        this.meterId = meterId;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public String getMeterName() {
        return meterName;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public void setMeterName(final String meterName) {
        this.meterName = meterName;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public String getTimestamp() {
        return timestamp;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public void setTimestamp(final String timestamp) {
        this.timestamp = timestamp;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public ElectricityRateMeterConfigurationProperties getElectricityDayRateMeter() {
        return electricityDayRateMeter;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public void setElectricityDayRateMeter(final ElectricityRateMeterConfigurationProperties electricityDayRateMeter) {
        this.electricityDayRateMeter = electricityDayRateMeter;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public ElectricityRateMeterConfigurationProperties getElectricityNightRateMeter() {
        return electricityNightRateMeter;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public void setElectricityNightRateMeter(final ElectricityRateMeterConfigurationProperties electricityNightRateMeter) {
        this.electricityNightRateMeter = electricityNightRateMeter;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public Long getGasMeter() {
        return gasMeter;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public void setGasMeter(final Long gasMeter) {
        this.gasMeter = gasMeter;
    }

    /**
     * Returns the {@link LocalDateTime} for the timestamp property.
     */
    public LocalDateTime getParsedTimestamp() {
        return LocalDateTime.parse(timestamp);
    }
}
