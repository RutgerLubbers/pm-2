package nl.hoepsch.pm;

public enum ExitStatus {
    INVALID_TOPIC_NAME(-1000),
    SUBSCRIBED_TO_TOO_MANY_TOPICS(-1001);

    private int exitCode;

    ExitStatus(int exitCode) {
        this.exitCode = exitCode;
    }

    public void exit() {
        System.exit(exitCode);
    }

    public static void exit(final ExitStatus status) {
        status.exit();
    }
}
