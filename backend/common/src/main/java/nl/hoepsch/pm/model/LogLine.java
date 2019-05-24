package nl.hoepsch.pm.model;

import java.time.LocalDateTime;

/**
 * A model for the stored Log Line(s).
 * Format:
 * <pre>
 * id
 * log_date
 * ts
 * usage_high
 * usage_low
 * current_usage
 * supply_high
 * supply_low
 * current_supply
 * gas
 * gas_ts
 * </pre>
 */
public class LogLine {

    /**
     * The meter's id.
     */
    private String meterId;

    /**
     * The meter's name.
     */
    private String meterName;

    /**
     * The log line's id.
     */
    private long id;

    /**
     * The log date (yyyy-mm-dd).
     */
    private String logDate;

    /**
     * The read-out timestamp.
     */
    private LocalDateTime timestamp;

    /**
     * The usage for "high cost" electricity. (daytime)
     */
    private int usageHigh;

    /**
     * The usage for "low cost" electricity. (night / weekend)
     */
    private int usageLow;

    /**
     * The current electricity usage.
     */
    private int currentUsage;

    /**
     * The supply for "high cost" electricity. (daytime)
     */
    private int supplyHigh;

    /**
     * The supply for "low cost" electricity. (night / weekend)
     */
    private int supplyLow;

    /**
     * The current electricity supply.
     */
    private int currentSupply;

    /**
     * The gas meter's read-out.
     */
    private int gas;

    /**
     * The timestamp of the gas meter's last read-out.
     */
    private int gasTimestamp;

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
    public long getId() {
        return id;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public void setId(final long id) {
        this.id = id;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public String getLogDate() {
        return logDate;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public void setLogDate(final String logDate) {
        this.logDate = logDate;
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
    public int getUsageHigh() {
        return usageHigh;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public void setUsageHigh(final int usageHigh) {
        this.usageHigh = usageHigh;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public int getUsageLow() {
        return usageLow;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public void setUsageLow(final int usageLow) {
        this.usageLow = usageLow;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public int getCurrentUsage() {
        return currentUsage;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public void setCurrentUsage(final int currentUsage) {
        this.currentUsage = currentUsage;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public int getSupplyHigh() {
        return supplyHigh;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public void setSupplyHigh(final int supplyHigh) {
        this.supplyHigh = supplyHigh;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public int getSupplyLow() {
        return supplyLow;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public void setSupplyLow(final int supplyLow) {
        this.supplyLow = supplyLow;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public int getCurrentSupply() {
        return currentSupply;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public void setCurrentSupply(final int currentSupply) {
        this.currentSupply = currentSupply;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public int getGas() {
        return gas;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public void setGas(final int gas) {
        this.gas = gas;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public int getGasTimestamp() {
        return gasTimestamp;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public void setGasTimestamp(final int gasTimestamp) {
        this.gasTimestamp = gasTimestamp;
    }
}
