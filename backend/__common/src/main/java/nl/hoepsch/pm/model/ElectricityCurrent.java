package nl.hoepsch.pm.model;

import java.time.LocalDateTime;

/**
 * The current electricity usage / supply.
 */
public class ElectricityCurrent {

    /**
     * The meter that's been read.
     */
    private Meter meter;

    /**
     * The event's timestamp.
     */
    private LocalDateTime timestamp;

    /**
     * The readout timestamp.
     */
    private LocalDateTime readoutTimestamp;

    /**
     * The value's read.
     */
    private ElectricityReadout current;

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
    public ElectricityReadout getCurrent() {
        return current;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public void setCurrent(final ElectricityReadout current) {
        this.current = current;
    }
}
