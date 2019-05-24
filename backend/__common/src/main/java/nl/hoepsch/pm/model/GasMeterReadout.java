package nl.hoepsch.pm.model;

import java.time.LocalDateTime;

/**
 * The gas meter's readout for a given time.
 */
public class GasMeterReadout {

    /**
     * The meter that's read.
     */
    private Meter meter;

    /**
     * The event's timestamp.
     */
    private LocalDateTime timestamp;

    /**
     * The timestamp the measurement was done.
     */
    private LocalDateTime readoutTimestamp;

    /**
     * The gas meter's value.
     */
    private Long value;

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
    public Long getValue() {
        return value;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public void setValue(final Long value) {
        this.value = value;
    }
}
