package ch.akuhn.hapax.linalg;

import java.util.Arrays;
import java.util.Iterator;

public class SparseVector {

    // public static int tally$grow = 0;
    // public static int tally$insert = 0;
    // public static int tally$total = 0;

    private int[] keys;
    private int size, used;

    private float[] values;

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

    public SparseVector(float[] values) {
        this(values.length);
        for (int n = 0; n < values.length; n++) {
            if (values[n] != 0) put(n, values[n]);
        }
    }

    public float get(int key) {
        if (key < 0 || key >= size) throw new IndexOutOfBoundsException(Integer
                .toString(key));
        int spot = Arrays.binarySearch(keys, 0, used, key);
        return spot < 0 ? 0 : values[spot];
    }

    public void resizeTo(int size) {
        if (size < this.size) throw new UnsupportedOperationException();
        this.size = size;
    }

    public void put(int key, float value) {
        if (key < 0 || key >= size) throw new IndexOutOfBoundsException(Integer
                .toString(key));
        int spot = Arrays.binarySearch(keys, 0, used, key);
        if (spot >= 0) values[spot] = value;
        else update(-1 - spot, key, value);
    }

    private void update(int spot, int key, float value) {
        // grow if reaching end of capacity
        if (used == keys.length) {
            // tally$grow++;
            int capacity = (keys.length * 3) / 2 + 1;
            keys = Arrays.copyOf(keys, capacity);
            values = Arrays.copyOf(values, capacity);
        }
        // shift values if not appending
        if (spot < used) {
            // tally$insert++;
            System.arraycopy(keys, spot, keys, spot + 1, used - spot);
            System.arraycopy(values, spot, values, spot + 1, used - spot);
        }
        keys[spot] = key;
        values[spot] = value;
        used++;
    }

    public int size() {
        return size;
    }

    public int used() {
        return used;
    }

    public double add(int key, float value) {
        if (key < 0 || key >= size) throw new IndexOutOfBoundsException(Integer
                .toString(key));
        // tally$total++;
        int spot = Arrays.binarySearch(keys, 0, used, key);
        if (spot >= 0) return values[spot] += value;
        update(-1 - spot, key, value);
        return value;
    }

    public boolean isUsed(int key) {
        return 0 <= Arrays.binarySearch(keys, 0, used, key);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof SparseVector && this.equals((SparseVector) obj);
    }

    public boolean equals(SparseVector v) {
        return size == v.size && used == v.used && Arrays.equals(keys, v.keys)
                && Arrays.equals(values, values);
    }

    @Override
    public int hashCode() {
        // TODO Auto-generated method stub
        return super.hashCode();
    }

    public Iterable<Entry> entries() {
        return new Entry().iterable();
    }

    public final class Entry {

        public int index;
        public double value;

        private Iterable<Entry> iterable() {
            return new Iter();
        }

        private final class Iter implements Iterable<Entry>, Iterator<Entry> {

            private int spot = 0;

            @Override
            public Iterator<Entry> iterator() {
                return this;
            }

            @Override
            public boolean hasNext() {
                return spot < used;
            }

            @Override
            public Entry next() {
                Entry.this.index = keys[spot];
                Entry.this.value = values[spot];
                spot++;
                return Entry.this;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }

        }

    }

}
