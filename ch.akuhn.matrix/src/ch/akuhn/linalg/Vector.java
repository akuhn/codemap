package ch.akuhn.hapax.linalg;

import java.util.Iterator;
import java.util.NoSuchElementException;

/** An ordered list of floating point numbers.
 * 
 * @author Adrian Kuhn
 *
 */
public abstract class Vector {

	public final class Entry {
		public int index;
		public double value;
	}

	public double add(int index, double value) {
		return put(index, get(index) + value);
	}

	public double density() {
		return ((double) used()) / size();
	}

	public abstract double get(int index);

	public abstract double put(int index, double value);

	public abstract int size();

	public double sum() {
		double sum = 0;
		for (Entry each: entries()) sum += each.value;
		return sum;
	}

	public double length() {
		double sum = 0;
		for (Entry each: entries()) sum += each.value * each.value;
		return Math.sqrt(sum);
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

	/** Iterates over all entries. Some vectors omit zero-valued entries.
	 * 
	 * @return value and index of each entry.
	 */
    public Iterable<Entry> entries() {
        return new Iterable<Entry>() {
			@Override
			public Iterator<Entry> iterator() {
				return new Iterator<Entry>() {
			    	
			    	private Entry each = new Entry();
			        private int index = 0;

			        @Override
			        public boolean hasNext() {
			            return index < size();
			        }

			        @Override
			        public Entry next() {
			        	if (!hasNext()) throw new NoSuchElementException();
			            each.value = get(index);
			            each.index = index++;
			            return each;
			        }

			        @Override
			        public void remove() {
			            throw new UnsupportedOperationException();
			        }

			    };
			}
		};
    }
	
}
