package nl.hoepsch.pm.dsmr.dto_input;

import java.util.List;

public class DSMR5DatagramDto {

    private List<ObisTagDto> values;

    private int checkSum;

    @SuppressWarnings("PMD.CommentRequired")
    public List<ObisTagDto> getValues() {
        return values;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public void setValues(final List<ObisTagDto> values) {
        this.values = values;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public int getCheckSum() {
        return checkSum;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public void setCheckSum(final int checkSum) {
        this.checkSum = checkSum;
    }
}
