package nl.hoepsch.pm;

import nl.hoepsch.pm.model.LogLine;

/**
 * Interface for publishing a {@link LogLine} to a Kafka topic.
 */
public interface DataPublisher {

    /**
     * Publish the {@link LogLine} to the publisher's topic.
     */
    @SuppressWarnings("PMD.SignatureDeclareThrowsException")
    void publish(LogLine logLine) throws Exception;
}
