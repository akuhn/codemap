package ch.akuhn.util;

public class Is {

    public static boolean equal(Object a, Object b) {
        return a == null ? b == null : a.equals(b);
    }

    /**
     * Checks if <tt>iterable</tt> has no elements.
     */
    public static final boolean empty(final Iterable<?> iterable) {
        return !iterable.iterator().hasNext();
    }

}
