package nl.hoepsch.pm.electricity.meter.web.resource;

/**
 * The total electricity consumption / production in kilo Watt hours.
 * <p>
 * The readout contains values for both (possible) meters, meter 1 and meter 2. These meters are in place in case the power contract
 * has different tariffs.
 */
public class ElectricityMeterUsageResource {


    /**
     * The electricity (kWh) delivered to the client, meter 1.
     */
    private Double deliveredToClientMeter1;

    /**
     * The electricity (kWh) delivered to the client, meter 2.
     */
    private Double deliveredToClientMeter2;

    /**
     * The electricity (kWh) delivered by the client, meter 1.
     */
    private Double deliveredByClientMeter1;

    /**
     * The electricity (kWh) delivered by the client, meter 2.
     */
    private Double deliveredByClientMeter2;

    @SuppressWarnings("PMD.CommentRequired")
    public Double getDeliveredToClientMeter1() {
        return deliveredToClientMeter1;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public void setDeliveredToClientMeter1(final Double deliveredToClientMeter1) {
        this.deliveredToClientMeter1 = deliveredToClientMeter1;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public Double getDeliveredToClientMeter2() {
        return deliveredToClientMeter2;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public void setDeliveredToClientMeter2(final Double deliveredToClientMeter2) {
        this.deliveredToClientMeter2 = deliveredToClientMeter2;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public Double getDeliveredByClientMeter1() {
        return deliveredByClientMeter1;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public void setDeliveredByClientMeter1(final Double deliveredByClientMeter1) {
        this.deliveredByClientMeter1 = deliveredByClientMeter1;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public Double getDeliveredByClientMeter2() {
        return deliveredByClientMeter2;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public void setDeliveredByClientMeter2(final Double deliveredByClientMeter2) {
        this.deliveredByClientMeter2 = deliveredByClientMeter2;
    }
}
