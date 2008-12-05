package ch.akuhn.hapax;

import java.util.Arrays;

public class SparseVector {

	private int[] keys;
	private int size, used;
	
	private double[] values;
	
	public SparseVector(int size) {
		this(size,10);
	}
	
	public SparseVector(int size, int capacity) {
		assert size >= 0;
		assert capacity >= 0;
		this.size = size;
		this.keys = new int[capacity];
		this.values = new double[capacity];
	}
	
	public double get(int key) {
		if (key < 0 || key >= size) throw new IndexOutOfBoundsException(Integer.toString(key));
		int spot = Arrays.binarySearch(keys, 0, used, key);
		return spot < 0 ? 0.0d : values[spot];
	}
	
	public void resizeTo(int size) {
		if (size < this.size) throw new UnsupportedOperationException();
		this.size = size;
	}
	
	public void set(int key, double value) {
		if (key < 0 || key >= size) throw new IndexOutOfBoundsException(Integer.toString(key));
		int spot = Arrays.binarySearch(keys, 0, used, key);
		if (spot >= 0) values[spot] = value;
		else update(-1-spot, key, value);
	}

    private void update(int spot, int key, double value) {
        // grow if reaching end of capacity
        if (used == keys.length) {
        	int capacity = (keys.length * 3)/2 + 1;
        	keys = Arrays.copyOf(keys, capacity);
        	values = Arrays.copyOf(values, capacity);
        }
        // shift values if not appending
        if (spot < used) { 
        	System.arraycopy(keys, spot, keys, spot+1, used-spot);
        	System.arraycopy(values, spot, values, spot+1, used-spot);
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

    public void add(int key, double value) {
        if (key < 0 || key >= size) throw new IndexOutOfBoundsException(Integer.toString(key));
        int spot = Arrays.binarySearch(keys, 0, used, key);
        if (spot >= 0) values[spot] += value;
        else update(-1-spot, key, value);
    }
	
}
