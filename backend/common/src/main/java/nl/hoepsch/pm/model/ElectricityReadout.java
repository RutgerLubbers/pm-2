package nl.hoepsch.pm.model;

/**
 * The meter's value for the usage an supply.
 *
 * Used for the different rate meters (day rate / night rate) and for the current readout.
 */
public class ElectricityReadout {

    /**
     * The usage.
     */
    private Long usage;

    /**
     * The supply.
     */
    private Long supply;

    @SuppressWarnings("PMD.CommentRequired")
    public Long getUsage() {
        return usage;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public void setUsage(final Long usage) {
        this.usage = usage;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public Long getSupply() {
        return supply;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public void setSupply(final Long supply) {
        this.supply = supply;
    }
}
