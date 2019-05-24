package nl.hoepsch.pm;

import nl.hoepsch.pm.config.ConsumerConfigurationProperties;
import nl.hoepsch.pm.config.KafkaConfigurationProperties;
import nl.hoepsch.pm.config.PowerMeterConfigurationProperties;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import java.util.Collections;
import java.util.Properties;

import static java.util.Objects.requireNonNull;

/**
 * The subscription to a Kafka topic.
 * <p>
 * This subscription delegates to {@link RecordConsumer} for consuming the records.
 */
public class KafkaSubscription implements InitializingBean {

    /**
     * The logger to use.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaSubscription.class);

    /**
     * The configuration from {@code application.yml}.
     */
    private final PowerMeterConfigurationProperties configurationProperties;

    /**
     * The record consumer to use.
     */
    private final RecordConsumer recordConsumer;

    /**
     * The constructor.
     */
    public KafkaSubscription(final PowerMeterConfigurationProperties configurationProperties, final RecordConsumer recordConsumer) {
        this.configurationProperties = requireNonNull(configurationProperties);
        this.recordConsumer = requireNonNull(recordConsumer);
    }

    /**
     * Start polling the topic the {@code kafkaConsumer} is configured for.
     * <p>
     * See {@link KafkaSubscription#consume(ConsumerRecords)}.
     */
    protected void poll(final Consumer<Long, String> kafkaConsumer) {
        while (true) {
            final ConsumerRecords<Long, String> records = kafkaConsumer.poll(1000);
            consume(records);
        }
    }

    /**
     * Consume the records.
     */
    protected void consume(final ConsumerRecords<Long, String> records) {
        records.forEach(record -> {
            LOGGER.trace("Received record.");
            recordConsumer.consume(record, false);
        });
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void afterPropertiesSet() {
        LOGGER.debug("Creating consumer.");

        poll(createConsumer());
    }

    private Consumer<Long, String> createConsumer() {
        LOGGER.debug("Creating consumer with name '{}'.", recordConsumer.getName());

        final ConsumerConfigurationProperties consumerConfigurationProperties =
                configurationProperties.getConsumer(recordConsumer.getName());
        final KafkaConfigurationProperties kafka = configurationProperties.getKafka();


        final Properties props = createProperties(consumerConfigurationProperties, kafka);

        // Create the consumer using props.
        final KafkaConsumer<Long, String> kafkaConsumer = new KafkaConsumer<>(props);
        subscribe(kafkaConsumer, consumerConfigurationProperties);

        return kafkaConsumer;

    }

    @SuppressWarnings("PMD.LawOfDemeter")
    private Properties createProperties(final ConsumerConfigurationProperties consumerConfigurationProperties,
            final KafkaConfigurationProperties kafka) {
        final Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, consumerConfigurationProperties.getGroupId());
        props.put(ConsumerConfig.CLIENT_ID_CONFIG, consumerConfigurationProperties.getClientId());
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, consumerConfigurationProperties.getEnableAutoCommit());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class.getName());

        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        return props;
    }

    private void subscribe(final KafkaConsumer<Long, String> kafkaConsumer,
            final ConsumerConfigurationProperties consumerConfigurationProperties) {
        subscribe(kafkaConsumer, consumerConfigurationProperties.getTopicName());
    }

    private void subscribe(final KafkaConsumer<Long, String> kafkaConsumer, final String topicName) {
        LOGGER.debug("Subscribing to '{}'.", topicName);
        if (topicName == null || topicName.startsWith("$")) {
            LOGGER.error("Topic '{}' is invalid!", topicName);
        }
        kafkaConsumer.subscribe(Collections.singleton(topicName));
    }
}
