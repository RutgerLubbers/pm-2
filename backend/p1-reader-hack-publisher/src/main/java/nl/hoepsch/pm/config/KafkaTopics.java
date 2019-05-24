package nl.hoepsch.pm.config;

/**
 * Class containing constants for the Kafka topics.
 */
public final class KafkaTopics {

    /**
     * The datagram topic.
     */
    public static final String DATAGRAM_TOPIC = "datagram";

    /**
     * The electricity actual topic.
     */
    public static final String ELECTRICITY_ACTUAL_TOPIC = "electricity-actual";

    /**
     * Utility constructor.
     */
    private KafkaTopics() {
        // Do nothing.
    }
}
