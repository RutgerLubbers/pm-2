package nl.hoepsch.pm.model;

/**
 * A gas and electricity meter.
 */
public class Meter {

    /**
     * The meter's id.
     */
    private String id;

    /**
     * The meter's name.
     */
    private String name;

    @SuppressWarnings("PMD.CommentRequired")
    public String getId() {
        return id;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public void setId(final String id) {
        this.id = id;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public String getName() {
        return name;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public void setName(final String name) {
        this.name = name;
    }
}
