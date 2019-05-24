package nl.hoepsch.pm;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * A {@link RecordConsumer}.
 */
@Component
public class PocAccumulator implements RecordConsumer {

    /**
     * The logger to use.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(PocAccumulator.class);

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return "poc";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void consume(final ConsumerRecord<Long, String> record, final boolean commit) {
        LOGGER.debug("Consumer Record:({}, {}, {}, {})", record.key(), record.value(), record.partition(), record.offset());
    }
}
