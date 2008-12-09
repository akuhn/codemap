package ch.akuhn.util;

import java.util.Iterator;

public class Cycle {

    /**
     * Iterate indefinitely over <code>iterable</code>.
     * 
     */
    public static final <E> Iterable<E> forever(final Iterable<E> iterable) {
        return new Iterable<E>() {
            public Iterator<E> iterator() {
                return new Iterator<E>() {
                    private Iterator<E> it = iterable.iterator();
    
                    public boolean hasNext() {
                        if (!it.hasNext()) it = iterable.iterator();
                        return it.hasNext();
                    }
    
                    public E next() {
                        if (!it.hasNext()) it = iterable.iterator();
                        return it.next();
                    }
    
                    public void remove() {
                        it.remove();
                    }
                };
            }
        };
    }
    
}
