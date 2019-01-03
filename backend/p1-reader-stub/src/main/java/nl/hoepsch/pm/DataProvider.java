package nl.hoepsch.pm;

import nl.hoepsch.pm.model.LogLine;

/**
 * Interface for providing a {@link LogLine} to publish.
 */
public interface DataProvider {

    /**
     * Get the next LogLine.
     */
    @SuppressWarnings("PMD.SignatureDeclareThrowsException")
    LogLine get() throws Exception;
}
