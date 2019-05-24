package nl.hoepsch.pm.model;

import org.bson.types.ObjectId;

import java.time.LocalDateTime;

public class AccumulatedGasUsage {
    private ObjectId id;
    private Long topicOffset;
    private LocalDateTime timestamp;
    private String date;
    private Long usage;

    @SuppressWarnings("PMD.CommentRequired")
    public ObjectId getId() {
        return id;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public void setId(final ObjectId id) {
        this.id = id;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public Long getTopicOffset() {
        return topicOffset;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public void setTopicOffset(final Long topicOffset) {
        this.topicOffset = topicOffset;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public void setTimestamp(final LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public String getDate() {
        return date;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public void setDate(final String date) {
        this.date = date;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public Long getUsage() {
        return usage;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public void setUsage(final Long usage) {
        this.usage = usage;
    }
}
