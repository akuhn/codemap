package ch.akuhn.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Static methods that operate on or return Iterables.
 * 
 * @author Adrian Kuhn
 * 
 */
public abstract class Iter {

	/** Returns first element or fail.
	 * 
	 * @throws NoSuchElementException if <code>iterable</code> has no elements
	 */
	public static final <E> E head(final Iterable<E> iterable) {
		return iterable.iterator().next();
	}

	/** Returns first element or default value.
	 */
	public static final <E> E headOrDefault(final Iterable<E> iterable, final E defaultValue) {
		Iterator<E> it = iterable.iterator();
		return it.hasNext() ? it.next() : defaultValue;
	}

	/** Returns first element or null.
	 */
	public static final <E> E headOrNull(final Iterable<E> iterable) {
		return headOrDefault(iterable, null);
	}

	/** Checks if <code>iterable</code> has no elements. 
	 */
	public static final boolean isEmpty(final Iterable<?> iterable) {
		return !iterable.iterator().hasNext();
	}

	/** Iterate over all consecutive pairs of <code>iterable</code>. 
	 * @return if <code>iterable</code> has less than two elements, the returned iterable is empty.
	 */
	public static final <E> Iterable<Pair<E, E>> pairs(final Iterable<E> iterable) {
		return new Iterable<Pair<E, E>>() {
			public Iterator<Pair<E, E>> iterator() {
				return new Iterator<Pair<E, E>>() {
					private final Iterator<E> it = iterable.iterator();
					private E prev = it.hasNext() ? it.next() : null;

					public boolean hasNext() {
						return it.hasNext();
					}

					public Pair<E, E> next() {
						return Pair.of(prev, prev = it.next());
					}

					public void remove() {
						throw new UnsupportedOperationException();
					}
				};
			}
		};
	}

	/** Iterate over integers 0 to <code>n</code>, excluding <code>n</code>.
	 * 
	 * @param n any integer.
	 * @return if <code>n</code> is negative, the returned iterable is empty.
	 */
	public static final Iterable<Integer> range(final int n) {
		return new Iterable<Integer>() {
			public Iterator<Integer> iterator() {
				return new Iterator<Integer>() {
					private int current = 0;

					public boolean hasNext() {
						return current < n;
					}

					public Integer next() {
						if (!(current < n)) throw new NoSuchElementException();
						return current++;
					}

					public void remove() {
						throw new UnsupportedOperationException();
					}
				};
			}
		};
	}

	/** Iterate over all elements of <code>iterable</code>, except the first one.
	 * 
	 */
	public static final <E> Iterable<E> tail(final Iterable<E> iterable) {
		return new Iterable<E>() {
			public Iterator<E> iterator() {
				return new Iterator<E>() {
					private final Iterator<E> it = iterable.iterator();
					{
						if (it.hasNext()) it.next();
					};

					public boolean hasNext() {
						return it.hasNext();
					}

					public E next() {
						return it.next();
					}

					public void remove() {
						it.remove();
					}
				};
			}
		};
	}
	
	/**
	 * Iterate indefinitely over <code>iterable</code>.
	 * 
	 */
	public static final <E> Iterable<E> cycle(final Iterable<E> iterable) {
		return new Iterable<E>() {
			public Iterator<E> iterator() {
				return new Iterator<E>() {
					private Iterator<E> it = iterable.iterator();
					
					public boolean hasNext() {
						if (!it.hasNext()) it = iterable.iterator();
						return it.hasNext();
					}

					public E next() {
						if (!it.hasNext()) it = iterable.iterator();
						return it.next();
					}

					public void remove() {
						it.remove();
					}
				};
			}
		};
	}

	public static <E> Iterable<E> iter(final E... elements) {
		return new Iterable<E>() {
			public Iterator<E> iterator() {
				return new Iterator<E>() {
					private int index = 0;
					
					public boolean hasNext() {
						return index < elements.length;
					}
					
					public E next() {
						if (index >= elements.length) throw new NoSuchElementException();
						return elements[index++];
					}
					
					public void remove() {
						throw new UnsupportedOperationException();
					}
				};
			}
			
		};
	}

	
	
	private Iter() {
		// static class
	}

}
