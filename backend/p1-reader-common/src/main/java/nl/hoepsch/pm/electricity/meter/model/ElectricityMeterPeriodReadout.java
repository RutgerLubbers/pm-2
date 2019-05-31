package nl.hoepsch.pm.electricity.meter.model;

/**
 * Holds the electricity meter readouts for a given period. The class holds the first (start) and last (end) meter readouts.
 * <p>
 * The start and end can have a different timestamp than the intended period.
 */
public class ElectricityMeterPeriodReadout {

    /**
     * The first readout.
     */
    private ElectricityMeterReadout start;

    /**
     * The last readout.
     */
    private ElectricityMeterReadout end;

    @SuppressWarnings("PMD.CommentRequired")
    public ElectricityMeterReadout getStart() {
        return start;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public void setStart(final ElectricityMeterReadout start) {
        this.start = start;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public ElectricityMeterReadout getEnd() {
        return end;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public void setEnd(final ElectricityMeterReadout end) {
        this.end = end;
    }
}
