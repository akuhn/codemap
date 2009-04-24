package ch.akuhn.util;

import java.util.Enumeration;
import java.util.Iterator;

/** Returns the size of iterables, useful for testing.
 * 
 * @author Adrian Kuhn
 *
 */
public class Size {

    public static final <T> int of(Iterable<T> values) {
        int count = 0;
        for (@SuppressWarnings("unused") T each: values) count++;
        return count;
    }
    
    public static final <T> int of(Iterator<T> iterator) {
        int count = 0;
        while (iterator.hasNext()) count++;
        return count;
    }
    
    public static final <T> int of(Enumeration<T> enumeration) {
        int count = 0;
        while (enumeration.hasMoreElements()) count++;
        return count;
    }
    
}
