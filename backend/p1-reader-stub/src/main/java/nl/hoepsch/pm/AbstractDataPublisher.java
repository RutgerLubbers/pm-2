package nl.hoepsch.pm;

import nl.hoepsch.pm.config.PowerMeterConfigurationProperties;
import nl.hoepsch.pm.model.LogLine;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.util.Objects.requireNonNull;

/**
 * Base class for the various {@link DataPublisher}s.
 */
public abstract class AbstractDataPublisher implements DataPublisher {

    /**
     * The logger to use.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractDataPublisher.class);

    /**
     * The kafka producer.
     */
    private final Producer<Long, String> kafkaProducer;

    /**
     * The application's configuration properties.
     */
    private final PowerMeterConfigurationProperties powerMeterConfigurationProperties;

    /**
     * The constructor.
     */
    public AbstractDataPublisher(final Producer<Long, String> kafkaProducer,
            final PowerMeterConfigurationProperties configurationProperties) {
        this.kafkaProducer = requireNonNull(kafkaProducer);
        this.powerMeterConfigurationProperties = requireNonNull(configurationProperties);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("PMD.LawOfDemeter")
    @Override
    public void publish(final LogLine logLine) throws Exception {
        LOGGER.trace("{} sending data to '{}'.", this.getClass().getSimpleName(), getTopic());

        final Long index = System.currentTimeMillis();
        final ProducerRecord<Long, String> record = new ProducerRecord<>(getTopic(), index, getPayload(logLine));

        final RecordMetadata metadata = kafkaProducer.send(record).get();
        final long elapsedTime = System.currentTimeMillis() - index;
        LOGGER.trace("sent record(key={} value={}) meta(partition={}, offset={}) time={}", record.key(), record.value(),
                metadata.partition(), metadata.offset(), elapsedTime);
    }

    /**
     * Factory method to create the payload to publish based on the {@code logline}.
     */
    protected abstract String getPayload(LogLine logline);

    /**
     * Return the configured topic to publish to.
     */
    protected abstract String getTopic();

    @SuppressWarnings("PMD.CommentRequired")
    protected PowerMeterConfigurationProperties getConfigurationProperties() {
        return powerMeterConfigurationProperties;
    }
}
