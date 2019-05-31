package nl.hoepsch.pm.util;

/**
 * Utility for various calculations.
 */
@SuppressWarnings({"PMD.ShortMethodName", "PMD.ShortVariable"})
public final class CalculationUtil {

    /**
     * Utility constructor.
     */
    private CalculationUtil() {
        // Do nothing.
    }

    /**
     * Subtract two doubles, {@code d2-d1}.
     * The result has a precision of 4 digits.
     *
     * @param d1 The double to subtract.
     * @param d2 The double to subtract from.
     * @return {@code d2 - d1}.
     */
    public static Double subtract(final Double d1, final Double d2) {
        return subtract(d1, d2, 4);
    }

    /**
     * Subtract two doubles, {@code d2-d1}.
     * The result has a precision of {@code precision} digits.
     *
     * @param d1        The double to subtract.
     * @param d2        The double to subtract from.
     * @param precision The precision.
     * @return {@code d2 - d1}.
     */
    public static Double subtract(final Double d1, final Double d2, final int precision) {
        if (d1 == null || d2 == null) {
            return null;
        }

        return round(d2 - d1, precision);
    }

    /**
     * Round the value with the {@code precision} number of digits.
     *
     * @param value     The value to round.
     * @param precision The number of digits.
     * @return The rounded value with the correct precision.
     */
    public static double round(final double value, final int precision) {
        final double scale = Math.pow(10, precision);
        return Math.round(value * scale) / scale;
    }
}
