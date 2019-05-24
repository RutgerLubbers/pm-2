package nl.hoepsch.pm.config;

/**
 * The configuration properties for a Kafka Consumer.
 */
public class ConsumerConfigurationProperties {

    /**
     * The consumer's group (id).
     */
    private String groupId;

    /**
     * The consumer's client id.
     */
    private String clientId;

    /**
     * The topic to consumer.
     */
    private String topicName;

    /**
     * Should messages be committed?
     */
    private Boolean enableAutoCommit = true;

    @SuppressWarnings("PMD.CommentRequired")
    public String getClientId() {
        return clientId;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public void setClientId(final String clientId) {
        this.clientId = clientId;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public String getGroupId() {
        return groupId;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public void setGroupId(final String groupId) {
        this.groupId = groupId;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public String getTopicName() {
        return topicName;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public void setTopicName(final String topicName) {
        this.topicName = topicName;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public Boolean getEnableAutoCommit() {
        return enableAutoCommit;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public void setEnableAutoCommit(final Boolean enableAutoCommit) {
        this.enableAutoCommit = enableAutoCommit;
    }

}
