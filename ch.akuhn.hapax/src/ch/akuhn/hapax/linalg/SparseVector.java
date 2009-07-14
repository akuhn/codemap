package ch.akuhn.hapax.linalg;

import static ch.akuhn.util.Interval.range;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

import ch.akuhn.io.chunks.ChunkInput;
import ch.akuhn.io.chunks.ChunkOutput;
import ch.akuhn.io.chunks.ReadFromChunk;
import ch.akuhn.io.chunks.WriteOnChunk;

public class SparseVector extends Vector {

      private final class Iter implements Iterable<Entry>, Iterator<Entry> {

        private int spot = 0;

        @Override
        public boolean hasNext() {
            return spot < used;
        }

        @Override
        public Iterator<Entry> iterator() {
            return this;
        }

        @Override
        public Entry next() {
            return new Entry(keys[spot], values[spot++]);
        }

        @Override
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
        int spot = Arrays.binarySearch(keys, 0, used, key);
        if (spot >= 0) return values[spot] += value;
        return update(-1 - spot, key, value);
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
        int spot = Arrays.binarySearch(keys, 0, used, key);
        return spot < 0 ? 0 : values[spot];
    }

    @Override
    public int hashCode() {
        // TODO Auto-generated method stub
        return super.hashCode();
    }

    public boolean isUsed(int key) {
        return 0 <= Arrays.binarySearch(keys, 0, used, key);
    }

    @Override
    public double put(int key, double value) {
        if (key < 0 || key >= size) throw new IndexOutOfBoundsException(Integer.toString(key));
        int spot = Arrays.binarySearch(keys, 0, used, key);
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
            int capacity = (keys.length * 3) / 2 + 1;
            keys = Arrays.copyOf(keys, capacity);
            values = Arrays.copyOf(values, capacity);
        }
        // shift values if not appending
        if (spot < used) {
            System.arraycopy(keys, spot, keys, spot + 1, used - spot);
            System.arraycopy(values, spot, values, spot + 1, used - spot);
        }
        used++;
        keys[spot] = key;
        return values[spot] = (float) value;
    }

    @Override
    public int used() {
        return used;
    }
    
    public void trim() {
        keys = Arrays.copyOf(keys, used);
        values = Arrays.copyOf(values, used);
    }

    @ReadFromChunk("SVEC")
    public SparseVector(ChunkInput chunk) throws IOException {
    	size = chunk.readInt();
    	used = chunk.readInt();
    	keys = chunk.readIntArray(used);
    	values = chunk.readFloatArray(used);
    }
    
    @WriteOnChunk("SVEC")
    public void storeOn(ChunkOutput chunk) throws IOException {
    	chunk.write(size);
    	chunk.write(used);
    	chunk.write(keys, 0, used);
    	chunk.write(values, 0, used);
    }
    
}
