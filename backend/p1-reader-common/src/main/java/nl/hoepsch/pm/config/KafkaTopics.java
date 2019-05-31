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
     *
     * This topic contains the values of the current actual usage (kW) and supply (kW) over time.
     */
    public static final String ELECTRICITY_ACTUAL_TOPIC = "electricity-actual";

    /**
     * The electricity meter topic.
     *
     * This topic contains the values of the total usage (kWh) and supply (kWh) over time.
     */
    public static final String ELECTRICITY_METER_TOPIC = "electricity-meter";

    /**
     * The electricity meter topic.
     *
     * This topic contains the values of the total usage (kWh) and supply (kWh) over time.
     */
    public static final String ELECTRICITY_METER_PER_DAY_TOPIC = "electricity-meter-per-day";

    /**
     * The electricity meter view.
     *
     * This view contains the values of the total usage (kWh) and supply (kWh) over time.
     */
    public static final String ELECTRICITY_METER_PER_DAY_VIEW = "vw-electricity-meter-per-day";

    /**
     * Utility constructor.
     */
    private KafkaTopics() {
        // Do nothing.
    }
}
