package ch.akuhn.foreach;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.NoSuchElementException;


public class For {

	public static <E> Iterable<E> each(E... elements) {
		return Arrays.asList(elements);
	}
	
	public static <E> Iterable<E> each(final Iterator<E> elements) {
		return new Iterable<E>() {
			public Iterator<E> iterator() {
				return elements;
			}
		};
	}
	
	public static <E> Iterable<E> each(final Enumeration<E> elements) {
		return new Iterable<E>() {
			public Iterator<E> iterator() {
				return new Iterator<E>() {
					public boolean hasNext() { return elements.hasMoreElements(); }
					public E next() { return elements.nextElement(); }
					public void remove() {	throw new UnsupportedOperationException(); }
				};
			}
		};
	}
	
	public static <E> Iterable<EachXY> matrix(final int m, final int n) {
		return new Iterable<EachXY>() {
			public Iterator<EachXY> iterator() {
				return new Iterator<EachXY>() {
					private EachXY each = new EachXY(m,n);
					private int i = 0, j = 0;
					public boolean hasNext() {
						return i < m && j < n;
					}
					public EachXY next() {
						each.x = i++;
						each.y = j;
						if (i >= m) { i = 0; j++; }
						return each;
					}
					public void remove() {
						throw new UnsupportedOperationException();
					}
				};
			}
		};
	}

	public static final <E> Iterable<Each<E>> withIndex(final E... elements) {
		return new Iterable<Each<E>>() {
			public Iterator<Each<E>> iterator() {
				return new Iterator<Each<E>>() {
					private Each<E> each = new Each<E>();
					private int index;
					public boolean hasNext() {
						return index < elements.length;
					}
					public Each<E> next() {
						if (index >= elements.length) throw new NoSuchElementException();
						each.value = elements[index];
						each.index++;
						return each;
					}
					public void remove() {
						throw new UnsupportedOperationException();
					}
				};
			}
		};
	}
	
	
	public static final <T> Iterable<Each<T>> withIndex(final Iterable<T> elements) {
		return new Iterable<Each<T>>() {
			public Iterator<Each<T>> iterator() {
				return new WithIndex<T>(elements.iterator());
			}
		};
	}
	
	public static final <T> Iterable<Each<T>> withIndex(final Iterator<T> elements) {
		return new Iterable<Each<T>>() {
			public Iterator<Each<T>> iterator() {
				return new WithIndex<T>(elements); // FIXME must run once and only one
			}
		};
	}
	
	static class WithIndex<T> implements Iterator<Each<T>> {

		private Each<T> each = new Each<T>();
		private int index = 0;
		private Iterator<T> iter;

		public WithIndex(Iterator<T> elements) {
			this.iter = elements;
		}

		//@Override
		public boolean hasNext() {
			return iter.hasNext();
		}

		//@Override
		public Each<T> next() {
			each.value = iter.next();
			each.yield = null; // not used
			each.index = index++;
			return each;
		}

		//@Override
		public void remove() {
			iter.remove();
		}

	}

    public static Interval range(int end) {
        return new Interval(0, end, +1);
    }
    public static Interval range(int start, int end) {
        return new Interval(start, end, +1);
    }

    public static Interval range(int start, int end, int step) {
        return new Interval(start, end, step);
    }
	
}
