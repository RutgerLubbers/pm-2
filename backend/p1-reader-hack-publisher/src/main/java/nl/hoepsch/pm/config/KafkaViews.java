package nl.hoepsch.pm.config;

public enum KafkaViews {
    YEAR_START("year_start"),
    YEAR_END("year_end")
        ;
    private final String name;

    KafkaViews(final String name) {
        this.name = name;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public String getName() {
        return name;
    }
}
