package ch.akuhn.util;

/**
 * Separates elements of a collection while printing. Does not require special
 * case treatment of first or last element. For example, the following program
 * prints a list of its arguments separated by commas, without using any
 * conditionals.
 * 
 * <pre>
 * Separator s = new Separator(&quot;, &quot;);
 * or (String a : args) {
 *    System.out.println(s + a);
 * 
 * </pre>
 * 
 * The implementation of class <code>Separator</code> is straight forward. It
 * wraps a string that is returned on every call of toString() except for the
 * first call, which returns an empty string.
 * 
 * @author Yossi Gil
 * 
 */
public class Separator {

    private boolean omitNext;
    private final String value;

    public Separator() {
        this(", ");
    }

    public Separator(String value) {
        this.value = value;
        this.omitNext = true;
    }

    public void reset() {
        omitNext = true;
    }

    public String toString() {
        String $ = omitNext ? "" : value;
        omitNext = false;
        return $;
    }

}
