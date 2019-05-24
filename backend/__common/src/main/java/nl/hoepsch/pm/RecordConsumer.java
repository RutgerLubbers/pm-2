package nl.hoepsch.pm;

import org.apache.kafka.clients.consumer.ConsumerRecord;

/**
 * Interface for a Kafka Consumer.
 * <p>
 * This interface only describes the consumption of a {@link ConsumerRecord}.
 */
public interface RecordConsumer {

    /**
     * The consumer name, as configured in the {@code application.yml}.
     */
    String getName();

    /**
     * Perform the reading of the record.
     */
    void consume(ConsumerRecord<Long, String> record, boolean commit);
}
