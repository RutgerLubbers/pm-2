package nl.hoepsch.pm.config;

/**
 * The electricity meter's usage and supply for one rate, i.e. for day rate or night rate.
 */
public class ElectricityRateMeterConfigurationProperties {

    /**
     * The meter's usage value.
     */
    private Long usage;

    /**
     * The meter's supply value.
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
