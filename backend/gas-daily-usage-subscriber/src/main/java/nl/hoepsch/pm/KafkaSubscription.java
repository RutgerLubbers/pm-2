package nl.hoepsch.pm;

import nl.hoepsch.pm.config.ConsumerConfigurationProperties;
import nl.hoepsch.pm.config.KafkaConfigurationProperties;
import nl.hoepsch.pm.config.PowerMeterConfigurationProperties;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import java.util.Collections;
import java.util.Properties;
import java.util.Set;

import static java.util.Objects.requireNonNull;
import static nl.hoepsch.pm.ExitStatus.INVALID_TOPIC_NAME;
import static nl.hoepsch.pm.ExitStatus.SUBSCRIBED_TO_TOO_MANY_TOPICS;
import static nl.hoepsch.pm.ExitStatus.exit;
import static org.apache.kafka.clients.consumer.ConsumerConfig.AUTO_OFFSET_RESET_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.CLIENT_ID_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG;

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
        LOGGER.debug("Received '{}' records.", records.count());
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
        final Consumer<Long, String> consumer = createConsumer();
        resetConsumerOffset(consumer);
        poll(consumer);
    }

    private void resetConsumerOffset(final Consumer<Long, String> consumer) {
        final Long offset = recordConsumer.getLastComittedOffset();
        final Set<TopicPartition> topicPartitions = consumer.assignment();
        if (topicPartitions.size() != 1) {
            LOGGER.error("The number of topics subscribed to is not '1' but '{}'.", topicPartitions.size());
            if (!topicPartitions.isEmpty()) {
                for (TopicPartition topicPartition : topicPartitions) {
                    LOGGER.error("Subscribed to partition '{} {}'.", topicPartition.topic(), topicPartition.partition());
                }
            }
            exit(SUBSCRIBED_TO_TOO_MANY_TOPICS);
        }

        if (offset == null) {
            LOGGER.debug("Seeking to beginning of topic.");
            consumer.seekToBeginning(topicPartitions);
        } else {
            LOGGER.debug("Seeking to offset '{}' within the topic.", offset);
            topicPartitions.forEach(topicPartition -> consumer.seek(topicPartition, offset));
        }
    }

    private Consumer<Long, String> createConsumer() {
        LOGGER.debug("Creating consumer with name '{}'.", recordConsumer.getName());

        final ConsumerConfigurationProperties consumerConfigurationProperties =
                configurationProperties.getConsumer(recordConsumer.getName());

        final KafkaConfigurationProperties kafka = configurationProperties.getKafka();

        // Create the consumer using props.
        final KafkaConsumer<Long, String> kafkaConsumer = new KafkaConsumer<>(createProperties(consumerConfigurationProperties, kafka));
        subscribe(kafkaConsumer, consumerConfigurationProperties);

        return kafkaConsumer;
    }

    @SuppressWarnings("PMD.LawOfDemeter")
    private Properties createProperties(final ConsumerConfigurationProperties consumerConfigurationProperties,
            final KafkaConfigurationProperties kafka) {
        final Properties properties = new Properties();
        properties.put(BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers());
        properties.put(GROUP_ID_CONFIG, consumerConfigurationProperties.getGroupId());
        properties.put(CLIENT_ID_CONFIG, consumerConfigurationProperties.getClientId());
        properties.put(ENABLE_AUTO_COMMIT_CONFIG, consumerConfigurationProperties.getEnableAutoCommit());
        properties.put(AUTO_OFFSET_RESET_CONFIG, "earliest");
        properties.put(KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class.getName());
        properties.put(VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        return properties;
    }

    private void subscribe(final KafkaConsumer<Long, String> kafkaConsumer,
            final ConsumerConfigurationProperties consumerConfigurationProperties) {
        subscribe(kafkaConsumer, consumerConfigurationProperties.getTopicName());
    }

    private void subscribe(final KafkaConsumer<Long, String> kafkaConsumer, final String topicName) {
        LOGGER.debug("Subscribing to '{}'.", topicName);
        if (topicName == null || topicName.startsWith("$")) {
            LOGGER.error("Topic '{}' is invalid!", topicName);
            exit(INVALID_TOPIC_NAME);
        }
        kafkaConsumer.subscribe(Collections.singleton(topicName));
    }
}
