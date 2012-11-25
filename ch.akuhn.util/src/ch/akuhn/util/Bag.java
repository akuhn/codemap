
package ch.akuhn.util;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

/**
 * A collection that contains unordered, duplicate elements.
 * 
 */
public class Bag<T> extends AbstractCollection<T> {

    public static class Count<E> implements Comparable<Count<E>> {
        public final int count;
        public final E element;

        private Count(E element, int count) {
            this.element = element;
            this.count = count;
        }

        public int compareTo(Count<E> o) {
            int diff = o.count - count;
            return diff == 0 ? compareElements(o) : diff;
        }
        
        @SuppressWarnings("unchecked")
        private int compareElements(Count<E> o) {
            if (!(element instanceof Comparable)) return 0;
            return ((Comparable<E>) element).compareTo(o.element);
        }

        public String toString() {
            return count + " x " + element;
        }
    }

    private static class Int {

        public int value;

        @Override
        public boolean equals(Object obj) {
            return (obj instanceof Int) && value == ((Int) obj).value;
        }

        @Override
        public int hashCode() {
            return value;
        }

        @Override
        public String toString() {
            return Integer.toString(value);
        }

    }

    private class Iter extends Providable<T> {

        private int count;
        private T curr;
        private Iterator<T> iter;

        public void initialize() {
            iter = values.keySet().iterator();
        }

        public T provide() {
            while (count <= 0) {
                if (!iter.hasNext()) return done();
                curr = iter.next();
                count = values.get(curr).value;
            }
            count--;
            return curr;
        }

    }

    private Map<T,Int> values = new HashMap<T,Int>();

    /**
     * Adds the specified element to this bag.
     * <p>
     * If, as a result of this call, the occurrences of the specified element
     * exceed <tt>Integer.MAX_VALUE</tt>, the number of occurrences is set to
     * <tt>Integer.MAX_VALUE</tt>.
     * 
     * @param e
     *            the element to be added to this bag.
     * @return <tt>true</tt> if the bag changes as a result of this call
     */
    @Override
    public boolean add(T e) {
        Int i = values.get(e);
        if (i == null) values.put(e, i = new Int());
        if (i.value == Integer.MAX_VALUE) return false;
        i.value++;
        return true;
    }

    /**
     * Adds <tt>n</tt> occurrences of the specified element to this bag.
     * <p>
     * If, as a result of this call, the occurrences of the specified element
     * exceed <tt>Integer.MAX_VALUE</tt>, the number of occurrences is set to
     * <tt>Integer.MAX_VALUE</tt>.
     * 
     * @param e
     *            the element to be added <tt>n</tt> times to this bag.
     * @param n
     *            the number of times element <tt>e</tt> is to be added.
     * @return <tt>true</tt> if the bag changes as a result of this call
     */
    public boolean add(T e, int n) {
        if (n < 0) throw new IllegalArgumentException();
        if (n == 0) return false;
        Int i = values.get(e);
        if (i == null) values.put(e, i = new Int());
        if (i.value == Integer.MAX_VALUE) return false;
        if (i.value > Integer.MAX_VALUE - n) i.value = Integer.MAX_VALUE;
        else i.value += n;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends T> coll) {
        if (!(coll instanceof Bag<?>)) return super.addAll(coll);
        Bag<? extends T> bag = (Bag<? extends T>) coll;
        boolean changed = false;
        for (Count<? extends T> each : bag.counts()) {
            if (this.add(each.element, each.count)) changed = true;
        }
        return changed;
    }

    /**
     * Removes all of the elements from this bag. The bag will be empty after
     * this method returns.
     */
    @Override
    public void clear() {
        values.clear();
    }

    /**
     * Returns <tt>true</tt> if this bag contains the specified element.
     * 
     * @param o
     *            element whose presence in this collection is to be tested
     * @return <tt>true</tt> if this bag contains the specified element
     */
    @Override
    public boolean contains(Object o) {
        return values.containsKey(o);
    }

    public Iterable<Count<T>> counts() {
        return new Providable<Count<T>>() {
            private Iterator<T> iter;
            public void initialize() {
                iter = values.keySet().iterator();
            }
            public Count<T> provide() {
                if (!iter.hasNext()) return this.done();
                T next = iter.next();
                return new Count<T>(next, values.get(next).value);
            }
        };
    }

    public Iterable<T> elements() {
        return IterableIteratorFactory.create(values.keySet().iterator());
    }

    /**
     * Return a <tt>Set</tt> view of the elements contained in this bag. The set
     * is backed by the bag, so changes to the bag are reflected in the set, but
     * <i>not</i> vice-versa.
     * 
     * @return an unmodifiable set view of the elements contained in this bag.
     */
    public Set<T> elementSet() {
        return Collections.unmodifiableSet(values.keySet());
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Bag<?>) && values.equals(((Bag<?>) obj).values);
    }

    @Override
    public int hashCode() {
        return values.hashCode();
    }

    /**
     * Returns <tt>true</tt> if this bag contains no elements.
     * 
     * @return <tt>true</tt> if this bag contains no elements
     */
    @Override
    public boolean isEmpty() {
        return values.isEmpty();
    }

    /**
     * Returns an iterator over the elements in this collection. There are no
     * guarantees concerning the order in which the elements are returned.
     * 
     * @return an <tt>Iterator</tt> over the elements in this collection
     */
    @Override
    public Iterator<T> iterator() {
        return new Iter().iterator();
    }

    /**
     * Returns the number of occurrences of the most occurring element in this
     * bag.
     * 
     * @return the number of occurrences of the most occurring element in this
     *         bag, or <tt>0</tt> if this bag is empty.
     */
    public int maxOccurrences() {
        int max = 0;
        for (Int each : values.values()) {
            max = Math.max(max, each.value);
        }
        return max;
    }

    /**
     * Returns the element with most occurrences in this bag. There are no
     * guarantees concerning the returned element, if there is more than one
     * element that has the same number of most occurrences.
     * 
     * @return any of the elements with most occurrences, or <tt>null</tt> if
     *         the bag is empty.
     */
    public T mostOccurring() {
        int max = 0;
        T most = null;
        for (Entry<T,Int> each : values.entrySet()) {
            if (max < (max = Math.max(max, each.getValue().value))) {
                most = each.getKey();
            }
        }
        return most;
    }

    /**
     * Returns the number of occurrences of the specified element in this bag.
     * 
     * @param o
     *            element whose occurrence in this bag is to be queried.
     * @return the number of occurrences of the specified element, otherwise
     *         <tt>0</tt>.
     */
    public int occurrences(Object o) {
        Int sum = values.get(o);
        return sum == null ? 0 : sum.value;
    }

    /**
     * Removes a single instance of the specified element from this bag, if it
     * is present.
     * 
     * @param o
     *            element to be removed from this bag, if present.
     * @return <tt>true</tt> if an element was removed as a result of this call.
     */
    @Override
    public boolean remove(Object o) {
        Int i = values.get(o);
        if (i == null) return false;
        i.value--;
        if (i.value == 0) values.remove(o);
        return true;
    }

    /**
     * Removes all occurrences of the specified element from this bag.
     * 
     * @param o
     *            elements to be removed from this bag, if present.
     * @return <tt>true</tt> if elements were removed as a result of this call.
     */
    public boolean removeAllOccurrences(Object o) {
        Int i = values.get(o);
        if (i == null) return false;
        values.remove(o);
        return true;
    }

    /**
     * Returns the number of elements in this bag. If this bag contains more
     * than <tt>Integer.MAX_VALUE</tt> elements, returns
     * <tt>Integer.MAX_VALUE</tt>.
     * 
     * @return the number of elements in this bag.
     */
    public int size() {
        int size = 0;
        for (Int i : values.values()) {
            if (size > Integer.MAX_VALUE - i.value) return Integer.MAX_VALUE;
            size += i.value;
        }
        return size;
    }

    public Iterable<Count<T>> sortedCounts() {
        return Get.sorted(counts());
    }

    /**
     * Returns the number of unique elements in this bag.
     * 
     * @return the number of unique elements in this bag.
     */
    public int uniqueSize() {
        return values.size();
    }

}
