package ch.akuhn.util;


/**
 * A utility class to realize universally quantified checks of an entire
 * collection.
 * 
 * @author Adrian Kuhn
 * @author Yossi Gil, 21/06/2008
 * 
 */
public class All {

    public static <T> boolean notNull(Iterable<T> iter) {
        assert iter != null;
        for (T t: iter) if (t == null) return false;
        return true;
    }

    public static boolean notNull(Object... values) {
        assert values != null;
        for (Object each: values) if (each == null) return false;
        return true;
    }

}
