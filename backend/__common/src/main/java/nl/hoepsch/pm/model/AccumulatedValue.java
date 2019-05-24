package nl.hoepsch.pm.model;

import java.time.LocalDateTime;

/**
 * The accumulated value for the read records.
 *
 * @param <T> The accumulated type, can be a number, or a time series etc.
 */
public class AccumulatedValue<T> {

    /**
     * The timestamp this {@code value} is for.
     */
    private final LocalDateTime timestamp;

    /**
     * The accumulated value.
     */
    private final T value;

    /**
     * Constructor.
     */
    public AccumulatedValue(final LocalDateTime timestamp, final T value) {
        this.timestamp = timestamp;
        this.value = value;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public T getValue() {
        return value;
    }
}
