package ch.akuhn.matrix;

import static ch.akuhn.foreach.For.range;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class SparseVector extends Vector {

	/* default */int[] keys;
	/* default */int size, used;
	/* default */double[] values;

	protected SparseVector(double[] values) {
		this(values.length);
		for (int n: range(values.length)) {
			if (values[n] != 0) put(n, values[n]);
		}
	}

	protected SparseVector(int size) {
		this(size, 10);
	}

	public SparseVector(int size, int capacity) {
		assert size >= 0;
		assert capacity >= 0;
		this.size = size;
		this.keys = new int[capacity];
		this.values = new double[capacity];
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
		return new Iterable<Entry>() {

			@Override
			public Iterator<Entry> iterator() {
				return new Iterator<Entry>() {

					private int spot = 0;

					@Override
					public boolean hasNext() {
						return spot < used;
					}

					@Override
					public Entry next() {
						if (!hasNext()) throw new NoSuchElementException();
						return new Entry(keys[spot], values[spot++]);
					}

					@Override
					public void remove() {
						throw new UnsupportedOperationException();
					}

				};
			}
		};
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
		return size ^ Arrays.hashCode(keys) ^ Arrays.hashCode(values);
	}

	public boolean isUsed(int key) {
		return 0 <= Arrays.binarySearch(keys, 0, used, key);
	}

	@Override
	public double put(int key, double value) {
		if (key < 0 || key >= size) throw new IndexOutOfBoundsException(Integer.toString(key));
		int spot = Arrays.binarySearch(keys, 0, used, key);
		if (spot >= 0)
			return values[spot] = (float) value;
		else
			return update(-1 - spot, key, value);
	}

	public void resizeTo(int newSize) {
		if (newSize < this.size) throw new UnsupportedOperationException();
		this.size = newSize;
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

	@Override
	public double dot(Vector x) {
		double product = 0;
		for (int k = 0; k < used; k++)
			product += x.get(keys[k]) * values[k];
		return product;
	}

	@Override
	public void scaleAndAddTo(double a, Vector y) {
		for (int k = 0; k < used; k++)
			y.add(keys[k], a * values[k]);
	}

	@Override
	public boolean equals(Vector v, double epsilon) {
		throw new Error("not yet implemented");
	}

	@Override
	public Vector times(double scalar) {
		SparseVector y = new SparseVector(size);
		y.keys = Arrays.copyOf(keys, size);
		y.values = Arrays.copyOf(values, size);
		for (int i = 0; i < values.length; i++)
			values[i] *= scalar;
		return y;
	}

	@Override
	public void apply(Function f) {
		assert f.apply(0) == 0; // assume zero is fixpoint
		for (int n = 0; n < used; n++)
			values[n] = f.apply(values[n]);
	}

}
