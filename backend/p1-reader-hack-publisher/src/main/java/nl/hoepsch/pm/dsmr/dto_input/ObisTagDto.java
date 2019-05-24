package nl.hoepsch.pm.dsmr.dto_input;

public class ObisTagDto {

    private ObisTagType tag;
    private String value;

    @SuppressWarnings("PMD.CommentRequired")
    public ObisTagType getTag() {
        return tag;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public void setTag(final ObisTagType tag) {
        this.tag = tag;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public String getValue() {
        return value;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public void setValue(final String value) {
        this.value = value;
    }
}
