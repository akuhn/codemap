package ch.akuhn.util;

/**
 * Replaces a null value with some default. Writing
 * <code>String guess = Defaults.to(answer,"A")</code> will assign the String
 * "A" to variable guess in the case that answer is null. If however, answer is
 * not null then guess's value will be that of answer.
 * 
 * @author Yossi Gil, 2008/06/20
 */
public final class Defaults {

    public static int to(final Integer v, final int defaultValue) {
        return v != null ? v.intValue() : defaultValue;
    }

    public static <T> T to(final T v, final T defaultValue) {
        return v != null ? v : defaultValue;
    }

    private Defaults() {
        throw new RuntimeException("Cannot instantiate.");
    }
}
