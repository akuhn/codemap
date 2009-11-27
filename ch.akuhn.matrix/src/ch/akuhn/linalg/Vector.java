package ch.akuhn.linalg;

import java.util.Iterator;
import java.util.NoSuchElementException;

/** An ordered list of floating point numbers.
 * 
 * @author Adrian Kuhn
 *
 */
public abstract class Vector {

	public double add(int index, double value) {
		return put(index, get(index) + value);
	}

	public double density() {
		return ((double) used()) / size();
	}

	/** Iterates over all entries. Some vectors omit zero-valued entries.
	 * 
	 * @return value and index of each entry.
	 */
    public Iterable<Entry> entries() {
        return new Iterable<Entry>() {
			@Override
			public Iterator<Entry> iterator() {
				return new Iterator<Entry>() {
			    	
			        private int index = 0;

			        @Override
			        public boolean hasNext() {
			            return index < size();
			        }

			        @Override
			        public Entry next() {
			        	if (!hasNext()) throw new NoSuchElementException();
			            return new Entry(index, get(index++));
			        }

			        @Override
			        public void remove() {
			            throw new UnsupportedOperationException();
			        }

			    };
			}
		};
    }

	public abstract double get(int index);

	public double norm() {
		double sum = 0;
		for (Entry each: entries()) sum += each.value * each.value;
		return Math.sqrt(sum);
	}

	public abstract double put(int index, double value);

	public abstract int size();

	public double sum() {
		double sum = 0;
		for (Entry each: entries()) sum += each.value;
		return sum;
	}

	/** Returns number of non-zero-valued entries.
	 * 
	 * @return a positive integer.
	 */
	public int used() {
		int count = 0;
		for (Entry each: entries()) if (each.value != 0) count++;
		return count;
	}

	public final class Entry {
		public final int index;
		public final double value;
		public Entry(int index, double value) {
			this.index = index;
			this.value = value;
		}
	}

	public static Vector from(double... values) {
		return new DenseVector(values.clone());
	}
	
	public static Vector wrap(double... values) {
		return new DenseVector(values);
	}
	
	public static Vector dense(int size) {
		return new DenseVector(size);
	}

	public static Vector sparse(int size) {
		return new SparseVector(size);
	}

	/** Returns the dot/scalar product.
	 * 
	 */
	public double dot(Vector x) {
		double product = 0;
		for (Entry each: entries()) product += each.value * x.get(each.index);
		return product;
	}

	/** y = y + a*<code>this</code>. 
	 * 
	 */
	public void scaleAndAddTo(double a, Vector y) {
		for (Entry each: entries()) y.add(each.index, a * each.value);
	}
	
}
