package nl.hoepsch.pm.config;

/**
 * Configuration properties for the P1 reader stub.
 */
public class StubConfigurationProperties {

    /**
     * The data file to read.
     */
    private String dataFile;

    /**
     * The meter's id to use.
     */
    private String meterId;

    /**
     * The meter's name to use.
     */
    private String meterName;

    @SuppressWarnings("PMD.CommentRequired")
    public String getDataFile() {
        return dataFile;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public void setDataFile(final String dataFile) {
        this.dataFile = dataFile;
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
    public String getMeterId() {
        return meterId;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public void setMeterId(final String meterId) {
        this.meterId = meterId;
    }
}
