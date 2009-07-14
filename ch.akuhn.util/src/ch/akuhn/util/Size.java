package ch.akuhn.util;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;

/** Returns the size of iterables, useful for testing.
 * 
 * @author Adrian Kuhn
 *
 */
public class Size {

    @SuppressWarnings("unchecked")
	public static final <T> int of(Iterable<T> collection) {
    	if (collection instanceof Collection) return ((Collection) collection).size();
        int count = 0;
        for (@SuppressWarnings("unused") T each: collection) count++;
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
