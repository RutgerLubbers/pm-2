package nl.hoepsch.pm.electricity.actual.model;

import java.time.ZonedDateTime;

/**
 * The actual electricity consumption / production in kilo Watt.
 */
public class ElectricityActualReadout {

    /**
     * The readout timestamp.
     */
    private ZonedDateTime timestamp;

    /**
     * The electricity (kWh) delivered to the client.
     */
    private Double deliveredToClientMeter;

    /**
     * The electricity (kWh) delivered by the client.
     */
    private Double deliveredByClientMeter;

    @SuppressWarnings("PMD.CommentRequired")
    public ZonedDateTime getTimestamp() {
        return timestamp;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public void setTimestamp(final ZonedDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public Double getDeliveredToClientMeter() {
        return deliveredToClientMeter;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public void setDeliveredToClientMeter(final Double deliveredToClientMeter) {
        this.deliveredToClientMeter = deliveredToClientMeter;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public Double getDeliveredByClientMeter() {
        return deliveredByClientMeter;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public void setDeliveredByClientMeter(final Double deliveredByClientMeter) {
        this.deliveredByClientMeter = deliveredByClientMeter;
    }
}
