package ch.akuhn.util;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;

/** Collection of integer values.
 *<P> 
 * At the moment, this class is limited to growth. I might add shrinking later.
 *<P> 
 * The iterator of this class is weakly consistent, thus the iterator does not throw a {@link ConcurrentModificationException} ever.
 *<P>
 * This class is not thread-safe.
 *<P>  
 * @author Adrian Kuhn
 *
 */
public class IntArray extends AbstractCollection<Integer> {

    private int[] array;

    private int size;
    
    public IntArray() {
        this.array = null;
        this.size = 0;
    }
    
    public IntArray(IntArray list) {
        this.array = list.array == null ? null : list.array.clone();
        this.size = list.size;
    }
    
    public IntArray(Collection<Integer> list) {
        this.array = new int[list.size()];
        this.size = 0;
        this.addAll(list);
    }

    public IntArray(int size) {
        this.array = new int[this.size = size];
    }

    @Override
    public boolean add(Integer element) {
        if (element == null) throw new IllegalArgumentException();
        if (array == null) array = new int[10];
        if (size == array.length) this.grow();
        array[size++] = element.intValue();
        return true;
    }

    public void add(int value) {
        if (array == null) array = new int[10];
        if (size == array.length) this.grow();
        array[size++] = value;
    }

    public int add(int index, int summand) {
        if (index < 0 && index > size) throw new IndexOutOfBoundsException();
        if (index != size) return array[index] += summand;
        this.add(summand);
        return summand;
    }
    
    
    public int[] asIntArray() {
        return array == null ? new int[] {} : Arrays.copyOf(array, size);
    }

    public int get(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        return array[index];
    }

    /** Returns an iterator over the integers in this array in proper sequence. 
     *<P>
     * This iterator is weakly consistent.
     * Thus, it does not throw a {@link ConcurrentModificationException} ever.
     * 
     * @return a weakly consistent iterator. 
     *  
     */
    @Override
    public Iterator<Integer> iterator() {
        return new Iter();
    }

    public void set(int index, int value) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        array[index] = value;
    }

    @Override
    public int size() {
        return size;
    }

    private void grow() {
        int newLength = (array.length * 3)/2 + 1;
        array = Arrays.copyOf(array, newLength);
    }

    private class Iter implements Iterator<Integer> {

        private int index = 0;
        
        @Override
        public boolean hasNext() {
            return index < size;
        }

        @Override
        public Integer next() {
            if (!hasNext()) throw new UnsupportedOperationException();
            return array[size];
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

    }
    
    public IntArray clone() {
        return new IntArray(this);
    }

}
