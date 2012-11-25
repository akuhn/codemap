package ch.akuhn.util;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class IteratorAsList<E> extends AbstractList<E> {

    private class Iter implements ListIterator<E> {

        private ListIterator<E> delegate;

        public Iter(int index) {
            delegate = list.listIterator(upTo(index));
        }

        public void add(E e) {
            delegate.add(e);
        }

        public boolean hasNext() {
            return delegate.hasNext() || iter.hasNext();
        }

        public boolean hasPrevious() {
            return delegate.hasPrevious();
        }

        public E next() {
            if (delegate.hasNext()) return delegate.next();
            E next = iter.next();
            delegate.add(next);
            // step back and forth to enable subsequent #remove
            delegate.previous();
            return delegate.next();
        }

        public int nextIndex() {
            return delegate.nextIndex();
        }

        public E previous() {
            return delegate.previous();
        }

        public int previousIndex() {
            return delegate.previousIndex();
        }

        public void remove() {
            delegate.remove();
        }

        public void set(E e) {
            delegate.set(e);
        }

    }

    @SuppressWarnings("unchecked")
    private static final Iterator NULL = new Iterator() {
        public boolean hasNext() {
            return false;
        }

        public Object next() {
            throw new NoSuchElementException();
        }

        public void remove() {
            throw new IllegalStateException();
        }
    };
    private Iterator<E> iter;

    private ArrayList<E> list;

    public IteratorAsList(Iterable<E> iter) {
        this(iter.iterator());
    }

    public IteratorAsList(Iterator<E> iter) {
        this.iter = iter;
        this.list = new ArrayList<E>();
    }

    @SuppressWarnings("unchecked")
    public void clear() {
        list.clear();
        iter = (Iterator<E>) NULL;
    }

    public E get(int index) {
        return list.get(upTo(index));
    }

    public boolean isEmpty() {
        return list.size() == 0 && !iter.hasNext();
    }

    public Iterator<E> iterator() {
        return listIterator();
    }

    public ListIterator<E> listIterator() {
        return listIterator(0);
    }

    public ListIterator<E> listIterator(int index) {
        upTo(index == 0 ? 0 : index - 1);
        return iter.hasNext() 
                ? new Iter(index) 
                : super.listIterator(index);
    }

    public E remove(int index) {
        return list.remove(upTo(index));
    }

    public E set(int index, E element) {
        return list.set(upTo(index), element);
    }

    /** Return the number of elements read so far from the backing iterator.
     * 
     * @return the number of elements read so far from the backing iterator.
     * @see #upTo(int)
     * @see #upToEnd()
     */
    public int size() {
        return list.size();
    }

    public List<E> subList(int fromIndex, int toIndex) {
        if (fromIndex > toIndex) throw new IllegalArgumentException();
        return super.subList(upTo(fromIndex), upTo(toIndex - 1) + 1);
    }

    /** Read elements from the backing iterator, up to and including the specified position.
     * 
     * @param index of the specified position
     * @return the specified position
     * @throws IndexOutOfBoundsException if the index is out of range
     *         (<tt>index &lt; 0 || index &gt;= upToEnd()</tt>)
     * @see #size()
     * @see #upToEnd()        
     */
    public int upTo(int index) {
        if (index < 0) throw new IndexOutOfBoundsException();
        if (index < list.size()) return index;
        while (list.size() <= index) {
            if (!iter.hasNext()) throw new IndexOutOfBoundsException();
            list.add(iter.next());
        }
        return index;
    };

    /** Read all remaining elements from the backing iterator.
     * 
     * @return the total number of elements in this list.
     * @see #size()
     * @see #upTo(int)
     */
    public int upToEnd() {
        while (iter.hasNext()) list.add(iter.next());
        return size();
    }

    @Override
    public int lastIndexOf(Object o) {
        upToEnd(); // super uses #size, must not stay lazy!
        return super.lastIndexOf(o);
    }
    
    @Override
    public String toString() {
        if (isEmpty()) return "[]";
        upTo(0);
        String str = list.toString();
        if (iter.hasNext()) str = str.substring(0, str.length() - 1) + ", ...]";
        return str;
    }    
    
}
