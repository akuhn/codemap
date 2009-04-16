package ch.akuhn.hapax.linalg;

import static ch.akuhn.util.Interval.range;

import java.util.Arrays;
import java.util.Iterator;

public class SparseVector extends Vector {

    // public static int tally$grow = 0;
    // public static int tally$insert = 0;
    // public static int tally$total = 0;

    private final class Iter implements Iterable<Entry>, Iterator<Entry> {

        private int spot = 0;

        //@Override
        public boolean hasNext() {
            return spot < used;
        }

        //@Override
        public Iterator<Entry> iterator() {
            return this;
        }

        //@Override
        public Entry next() {
            return new Entry(keys[spot], values[spot++]);
        }

        //@Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

    }

    private int[] keys;
    private int size, used;

    private float[] values;

    public SparseVector(double[] values) {
        this(values.length);
        for (int n: range(values.length)) {
            if (values[n] != 0) put(n, values[n]);
        }
    }

    public SparseVector(int size) {
        this(size, 10);
    }

    public SparseVector(int size, int capacity) {
        assert size >= 0;
        assert capacity >= 0;
        this.size = size;
        this.keys = new int[capacity];
        this.values = new float[capacity];
    }

    @Override
    public double add(int key, double value) {
        if (key < 0 || key >= size) throw new IndexOutOfBoundsException(Integer.toString(key));
        // tally$total++;
//        not supported in 1.5?
//        int spot = Arrays.binarySearch(keys, 0, used, key);        
        int spot = Arrays.binarySearch(keys, key);
        if (spot >= 0) return values[spot] += value;
        return update(-1 - spot, key, value);
    }

    @Override
    public double density() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Iterable<Entry> entries() {
        return new Iter();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof SparseVector && this.equals((SparseVector) obj);
    }

    public boolean equals(SparseVector v) {
        return size == v.size && used == v.used && Arrays.equals(keys, v.keys) && Arrays.equals(values, values);
    }

    @Override
    public double get(int key) {
        if (key < 0 || key >= size) throw new IndexOutOfBoundsException(Integer.toString(key));
        int spot = Arrays.binarySearch(keys, key);
        return spot < 0 ? 0 : values[spot];
    }

    @Override
    public int hashCode() {
        // TODO Auto-generated method stub
        return super.hashCode();
    }

    public boolean isUsed(int key) {
        return 0 <= Arrays.binarySearch(keys, key);
    }

    @Override
    public double put(int key, double value) {
        if (key < 0 || key >= size) throw new IndexOutOfBoundsException(Integer.toString(key));
        int spot = Arrays.binarySearch(keys, key);
        if (spot >= 0) return values[spot] = (float) value;
        else return update(-1 - spot, key, value);
    }

    public void resizeTo(int size) {
        if (size < this.size) throw new UnsupportedOperationException();
        this.size = size;
    }

    @Override
    public int size() {
        return size;
    }

    private double update(int spot, int key, double value) {
        // grow if reaching end of capacity
        if (used == keys.length) {
            // tally$grow++;
            int capacity = (keys.length * 3) / 2 + 1;            
            keys = arrayCopyOf(keys, capacity);
            values = arrayCopyOf(values, capacity);
        }
        // shift values if not appending
        if (spot < used) {
            // tally$insert++;
            System.arraycopy(keys, spot, keys, spot + 1, used - spot);
            System.arraycopy(values, spot, values, spot + 1, used - spot);
        }
        used++;
        keys[spot] = key;
        return values[spot] = (float) value;
    }

    public static int[] arrayCopyOf(int[] original, int newLength) {
        int[] copy = new int[newLength];
        System.arraycopy(original, 0, copy, 0,
                         Math.min(original.length, newLength));
        return copy;
    }
    
    public static float[] arrayCopyOf(float[] original, int newLength) {
        float[] copy = new float[newLength];
        System.arraycopy(original, 0, copy, 0,
                         Math.min(original.length, newLength));
        return copy;
    }
    
    @Override
    public int used() {
        return used;
    }
    
    public void trim() {
        keys = arrayCopyOf(keys, used);
        values = arrayCopyOf(values, used);
    }
    

}
