package nl.hoepsch.pm.model;

/**
 * An initial meter readout.
 * <p>
 * Usually read from the configuration properties.
 */
public class InitialMeterReadouts {

    /**
     * The gas meter readout.
     */
    private GasMeterReadout gasMeterReadout;

    /**
     * The electricity meter readout.
     */
    private ElectricityMeterReadout electricityMeterReadout;

    @SuppressWarnings("PMD.CommentRequired")
    public GasMeterReadout getGasMeterReadout() {
        return gasMeterReadout;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public void setGasMeterReadout(final GasMeterReadout gasMeterReadout) {
        this.gasMeterReadout = gasMeterReadout;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public ElectricityMeterReadout getElectricityMeterReadout() {
        return electricityMeterReadout;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public void setElectricityMeterReadout(final ElectricityMeterReadout electricityMeterReadout) {
        this.electricityMeterReadout = electricityMeterReadout;
    }
}
