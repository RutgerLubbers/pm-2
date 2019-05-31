package nl.hoepsch.pm.electricity.actual.model;

import java.util.List;

/**
 * A list of electricity actual readouts for a certain period.
 */
public class ElectricityActualPeriodReadout {

    /**
     * The values for the period.
     */
    private List<ElectricityActualReadout> values;

    @SuppressWarnings("PMD.CommentRequired")
    public List<ElectricityActualReadout> getValues() {
        return values;
    }

    @SuppressWarnings("PMD.CommentRequired")
    public void setValues(final List<ElectricityActualReadout> values) {
        this.values = values;
    }

}
