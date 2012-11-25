
package ch.akuhn.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A class for constructing lists by appending elements. Modelled after
 * java.lang.StringBuffer.
 * 
 */
public class ListBuffer<A> implements Collection<A> {

    public static <T> ListBuffer<T> lb() {
        return new ListBuffer<T>();
    }

    /**
     * The number of element in this buffer.
     */
    public int count;

    /**
     * The list of elements of this buffer.
     */
    public List<A> elems;

    /**
     * A pointer pointing to the last, sentinel element of `elems'.
     */
    public List<A> last;

    /**
     * Has a list been created from this buffer yet?
     */
    public boolean shared;

    /**
     * Create a new initially empty list buffer.
     */
    public ListBuffer() {
        clear();
    }

    public boolean add(A a) {
        throw new UnsupportedOperationException();
    }

    public boolean addAll(Collection<? extends A> c) {
        throw new UnsupportedOperationException();
    }

    /**
     * Append an element to buffer.
     */
    public ListBuffer<A> append(A x) {
        if (shared) copy();
        last.head = x;
        last.setTail(new List<A>(null, null));
        last = last.tail;
        count++;
        return this;
    }

    /**
     * Append all elements in an array to buffer.
     */
    public ListBuffer<A> appendArray(A[] xs) {
        for (int i = 0; i < xs.length; i++) {
            append(xs[i]);
        }
        return this;
    }

    /**
     * Append all elements in a list to buffer.
     */
    public ListBuffer<A> appendList(List<A> xs) {
        while (xs.nonEmpty()) {
            append(xs.head);
            xs = xs.tail;
        }
        return this;
    }

    /**
     * Append all elements in a list to buffer.
     */
    public ListBuffer<A> appendList(ListBuffer<A> xs) {
        return appendList(xs.toList());
    }

    public final void clear() {
        this.elems = new List<A>(null, null);
        this.last = this.elems;
        count = 0;
        shared = false;
    }

    /**
     * Does the list contain the specified element?
     */
    public boolean contains(Object x) {
        return elems.contains(x);
    }

    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    /**
     * Copy list and sets last.
     */
    private void copy() {
        List<A> p = elems = new List<A>(elems.head, elems.tail);
        while (true) {
            List<A> tail = p.tail;
            if (tail == null) break;
            tail = new List<A>(tail.head, tail.tail);
            p.setTail(tail);
            p = tail;
        }
        last = p;
        shared = false;
    }

    /**
     * The first element in this buffer.
     */
    public A first() {
        return elems.head;
    }

    /**
     * Is buffer empty?
     */
    public boolean isEmpty() {
        return count == 0;
    }

    /**
     * An enumeration of all elements in this buffer.
     */
    public Iterator<A> iterator() {
        return new Iterator<A>() {
            List<A> elems = ListBuffer.this.elems;

            public boolean hasNext() {
                return elems != last;
            }

            public A next() {
                if (elems == last) throw new NoSuchElementException();
                A elem = elems.head;
                elems = elems.tail;
                return elem;
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    /**
     * Return the number of elements in this buffer.
     */
    public int length() {
        return count;
    }

    /**
     * Return first element in this buffer and remove
     */
    public A next() {
        A x = elems.head;
        remove();
        return x;
    }

    /**
     * Is buffer not empty?
     */
    public boolean nonEmpty() {
        return count != 0;
    }

    /**
     * Prepend an element to buffer.
     */
    public ListBuffer<A> prepend(A x) {
        elems = elems.prepend(x);
        count++;
        return this;
    }

    /**
     * Remove the first element in this buffer.
     */
    public void remove() {
        if (elems != last) {
            elems = elems.tail;
            count--;
        }
    }

    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    public int size() {
        return count;
    }

    public Object[] toArray() {
        return toArray(new Object[size()]);
    }

    /**
     * Convert buffer to an array
     */
    public <T> T[] toArray(T[] vec) {
        return elems.toArray(vec);
    }

    /**
     * Convert buffer to a list of all its elements.
     */
    public List<A> toList() {
        shared = true;
        return elems;
    }
}
