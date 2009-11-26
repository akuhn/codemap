package edu.berkeley.guir.prefuse.collections;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * An iterator over a single element.
 *
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public class SingleElementIterator implements Iterator {

    private Object object;
    
    public SingleElementIterator(Object o) {
        object = o;
    } //
    
    public void remove() {
        throw new UnsupportedOperationException();
    } //

    public boolean hasNext() {
        return (object != null);
    } //

    public Object next() {
        if (object != null) {
            Object rv = object;
            object = null;
            return rv;
        } else {
            throw new NoSuchElementException();
        }
    } //

} // end of class SingleElementIterator
