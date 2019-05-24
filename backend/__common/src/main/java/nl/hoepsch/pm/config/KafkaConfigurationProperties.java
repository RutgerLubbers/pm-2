package nl.hoepsch.pm.config;

/**
 * Configuration properties for Kafka consumers / producers.
 */
public class KafkaConfigurationProperties {

    /**
     * The bootstrap servers.
     */
    private String bootstrapServers;

    @SuppressWarnings("PMD.CommentRequired")
    public String getBootstrapServers() {
        return bootstrapServers;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public void setBootstrapServers(final String bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
    }


}
