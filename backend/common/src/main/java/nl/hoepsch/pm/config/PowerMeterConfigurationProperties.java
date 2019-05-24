package nl.hoepsch.pm.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Configuration properties for all Power Meter applications.
 */
@Configuration
@ConfigurationProperties(prefix = "powerMeter")
public class PowerMeterConfigurationProperties {

    /**
     * The logger to use.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(PowerMeterConfigurationProperties.class);

    /**
     * The gas meter topic. This topic is used to publish the gas meter's readouts.
     */
    private String gasMeterTopic;

    /**
     * The electricity meter topic. This topic is used to publish the electricity meter's readouts.
     */
    private String electricityMeterTopic;

    /**
     * The electricity usage topic. This topic is used to publish the current electricity usage.
     */
    private String electricityUsageTopic;

    /**
     * The kafka configuration properties.
     */
    private KafkaConfigurationProperties kafka;

    /**
     * The stub configuration properties.
     */
    private StubConfigurationProperties stub;

    /**
     * The configured consumers.
     */
    private Map<String, ConsumerConfigurationProperties> consumers = new HashMap<>();

    /**
     * The list of initial meter readouts.
     */
    private List<InitialMeterReadoutsConfigurationProperties> initialMeterReadouts = new ArrayList<>();

    @SuppressWarnings("PMD.CommentRequired")
    public String getGasMeterTopic() {
        return gasMeterTopic;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public void setGasMeterTopic(final String gasMeterTopic) {
        this.gasMeterTopic = gasMeterTopic;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public String getElectricityUsageTopic() {
        return electricityUsageTopic;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public void setElectricityUsageTopic(final String electricityUsageTopic) {
        this.electricityUsageTopic = electricityUsageTopic;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public String getElectricityMeterTopic() {
        return electricityMeterTopic;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public void setElectricityMeterTopic(final String electricityMeterTopic) {
        this.electricityMeterTopic = electricityMeterTopic;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public KafkaConfigurationProperties getKafka() {
        return kafka;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public void setKafka(final KafkaConfigurationProperties kafka) {
        this.kafka = kafka;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public Map<String, ConsumerConfigurationProperties> getConsumers() {
        return consumers;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public void setConsumers(final Map<String, ConsumerConfigurationProperties> consumers) {
        this.consumers = consumers;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public StubConfigurationProperties getStub() {
        return stub;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public void setStub(final StubConfigurationProperties stub) {
        this.stub = stub;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public List<InitialMeterReadoutsConfigurationProperties> getInitialMeterReadouts() {
        return initialMeterReadouts;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public void setInitialMeterReadouts(final List<InitialMeterReadoutsConfigurationProperties> initialMeterReadouts) {
        this.initialMeterReadouts = initialMeterReadouts;
    }

    /**
     * Returns the consumer for {@code name}, or {@code null} if the consumer is not configured.
     */
    @SuppressWarnings("PMD.LawOfDemeter")
    public ConsumerConfigurationProperties getConsumer(final String name) {
        final ConsumerConfigurationProperties consumerConfigurationProperties = getConsumers().get(name);

        if (consumerConfigurationProperties == null) {
            LOGGER.error("No consumer '{}' defined in 'application.yml'.", name);
        }
        return consumerConfigurationProperties;
    }
}
