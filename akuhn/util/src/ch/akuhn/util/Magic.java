//  Copyright (c) 1998-2007 Adrian Kuhn <akuhn(a)iam.unibe.ch>
//  
//  This file is part of "Adrian Kuhn's Utilities for Java".
//  
//  "Adrian Kuhn's Utilities for Java" is free software: you can redistribute
//  it and/or modify it under the terms of the GNU Lesser General Public License
//  as published by the Free Software Foundation, either version 3 of the
//  License, or (at your option) any later version.
//  
//  "Adrian Kuhn's Utilities for Java" is distributed in the hope that it will
//  be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser
//  General Public License for more details.
//  
//  You should have received a copy of the GNU Lesser General Public License
//  along with "Adrian Kuhn's Utilities for Java". If not, see
//  <http://www.gnu.org/licenses/>.
//  

package ch.akuhn.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

/** Static methods that operate on objects, iterables and blocks.
 *  Blocks are <tt>Predicate</tt>, <tt>Function</tt>, and <tt>Procedure</tt>.
 * 
 *
 */
public class Magic {

	public static <T> List<T> asList(T... elements) {
		return Arrays.asList(elements);
	}
	
	public static <T> Set<T> asSet(T... elements) {
		return new HashSet<T>(Arrays.asList(elements));
	}
	
	
	public static <T> Iterable<T> iter(final T... elements) {
		return new Iterable<T>() {

			
			public Iterator<T> iterator() {
				return new Iterator<T>() {
					private int index = 0;
					
					public boolean hasNext() {
						return index < elements.length;
					}

					
					public T next() {
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

	/** Checks if all elements satisfy the specified predicate. Returns <tt>true</tt>
	 * if the collection is empty.
	 * 
	 */
	public static <A> boolean allSatisfy(Iterable<A> coll, Predicate<A> block) {
		for (A each : coll) {
			if (!block.is(each))
				return false;
		}
		return true;
	}

	/** Returns any element from the specified collection.
	 * 
	 * @throws NoSuchElementException if the specified collection is empty.
	 * 
	 */
	public static <T> T any(Iterable<T> coll) {
		return coll.iterator().next();
	}

	/** Checks if at least one elements satisfy the specified predicate. 
	 * Empty collections return <tt>false</tt>.
	 * 
	 */
	public static <A> boolean anySatisfy(Iterable<A> coll, Predicate<A> block) {
		for (A each : coll) {
			if (block.is(each))
				return true;
		}
		return false;
	}

	/** Same as {@link collect}, but returns the results as set. */
	public static <T, A> Set<T> collectAsSet(Iterable<A> coll,
			Function<T, A> block) {
		Set<T> result = new HashSet<T>();
		for (A each : coll) {
			result.add(block.eval(each));
		}
		return result;
	}
	
	/** Returns a new collection whose elements are the result of evaluating
	 * the specified function for each element of the receiving collection.
	 * 
	 */
	public static <T, A> Collection<T> collect(Iterable<A> coll,
			Function<T, A> block) {
		Collection<T> result = new ArrayList<T>();
		for (A each : coll) {
			result.add(block.eval(each));
		}
		return result;
	}

	/** Return the number of elements that satisfy the specified predicate.
	 * 
	 */
	public static <A> int count(Iterable<A> coll, Predicate<A> block) {
		int count = 0;
		for (A each : coll) {
			if (block.is(each))
				count++;
		}
		return count;
	}

	/** Returns the first element that satisfies the specified predicate, 
	 * or <tt>null</tt> if none such element is present
	 * 
	 */
	public static <A> A detect(Iterable<A> coll, Predicate<A> block) {
		for (A each : coll) {
			if (block.is(each))
				return each;
		}
		return null;
	}
	
	/** Checks if the specified array contains the specified element.
	 * 
	 */
	public static <A> boolean contains(A[] array, A a) {
		for (A each : array) {
			if (Magic.isEqual(each, a)) return true;
		}
		return false;
	}

	/** Evaluates a block for each element of a collection.
	 * 
	 */
	public static <A> void forEach(Iterable<A> coll, Procedure<A> block) {
		for (A each : coll) {
			block.run(each);
		}
	}

	public static <T, A> Map<T, Collection<A>> groupedBy(Iterable<A> coll,
			Function<T, A> block) {
		Map<T, Collection<A>> result = new HashMap<T, Collection<A>>();
		for (A each : coll) {
			T key = block.eval(each);
			Collection<A> bucket = result.get(key);
			if (bucket == null)
				result.put(key, bucket = new ArrayList<A>());
			bucket.add(each);
		}
		return result;
	}

	public static <T, A> T inject(Iterable<A> coll, T initialValue,
			BinaryFunction<T, T, A> block) {
		T value = initialValue;
		for (A each : coll) {
			value = block.eval(value, each);
		}
		return value;
	}

	
	/** Returns all elements that do <i>not</i> satisfy the specified predicate.
	 * 
	 */
	public static <A> Collection<A> reject(Iterable<A> coll, Predicate<A> block) {
		return select(coll, Magic.not(block));
	}

	/** Removes all elements from the receiving collection that satisfy the specified predicate.
	 * 
	 * @throws UnsupportedOperationException if the receiving collection is unmodifiable.
	 */
	public static <A> void remove(Iterable<A> coll, Predicate<A> block) {
		for (Iterator<A> iterator = coll.iterator(); iterator.hasNext();) {
			A each = iterator.next();
			if (block.is(each)) {
				iterator.remove();
			}
		}
	}

	/** Returns a list of all subsequences whose elements do <i>not</i> satisfy the specified predicate. 
	 * 
	 */
	public static <A> List<List<A>> runsFailing(Iterable<A> coll,
			Predicate<A> block) {
		return runsSatisfying(coll, Magic.not(block));
	}

	/** Returns a list of all subsequences whose elements satisfy the specified predicate. 
	 * 
	 */
	public static <A> List<List<A>> runsSatisfying(Iterable<A> coll,
			Predicate<A> block) {
		ArrayList<List<A>> result = new ArrayList<List<A>>();
		ArrayList<A> run = null;
		for (A each : coll) {
			if (block.is(each)) {
				if (run == null)
					run = new ArrayList<A>();
				run.add(each);
			} else {
				if (run != null) {
					run.trimToSize();
					result.add(run);
					run = null;
				}
			}
		}
		result.trimToSize();
		return result;
	}

	/** Return all elements that satisfy the specified predicate.
	 * 
	 */
	public static <A> Collection<A> select(Iterable<A> coll, Predicate<A> block) {
		Collection<A> result = new ArrayList<A>();
		for (A each : coll) {
			if (block.is(each)) {
				result.add(each);
			}
		}
		return result;
	}

	/** Same as {@link remove}, but returns the removed elements.
	 * 
	 */
	public static <A> Collection<A> splice(Iterable<A> coll, Predicate<A> block) {
		Collection<A> result = new ArrayList<A>();
		for (Iterator<A> iterator = coll.iterator(); iterator.hasNext();) {
			A each = iterator.next();
			if (block.is(each)) {
				result.add(each);
				iterator.remove();
			}
		}
		return result;
	}

	public static <A> Predicate<A> isEqual(final A expected) {
		if (expected == null) return isNull();
		return new Predicate<A>() {
			
			public boolean is(A a) {
				return expected.equals(a);
			}
		};
	}

	public static <A> Predicate<A> isNull() {
		return new Predicate<A>() {
			
			public boolean is(A a) {
				return null == a;
			}
		};
	}

	public static <A> Predicate<A> instanceOf(final Class<?> jazz) {
		return new Predicate<A>() {
			
			public boolean is(A a) {
				return a == null || jazz.isAssignableFrom(a.getClass());
			}
		};
	}

	public static <A> Predicate<A> notNull() {
		return new Predicate<A>() {
			
			public boolean is(A a) {
				return null != a;
			}
		};
	}

	public static <A> Predicate<A> not(final Predicate<A> block) {
		return new Predicate<A>() {
			
			public boolean is(A a) {
				return !block.is(a);
			}
		};
	}

	/** Combines the specified predicates to a logical <tt>AND</tt> expression.
	 * 
	 */
	public static <A> Predicate<A> and(final Predicate<A>... blocks) {
		return new Predicate<A>() {
			
			public boolean is(A a) {
				boolean bool = true;
				for (int n = 0; n < blocks.length; n++) {
					bool = bool && blocks[n].is(a);
					if (!bool) return false;
				}
				return true;
			}
		};
	}

	/** Combines the specified predicates to a logical <tt>OR</tt> expression.
	 * 
	 */
	public static <A> Predicate<A> or(final Predicate<A>... blocks) {
		return new Predicate<A>() {
			
			public boolean is(A a) {
				boolean bool = false;
				for (int n = 0; n < blocks.length; n++) {
					bool = bool || blocks[n].is(a);
					if (bool) return true;
				}
				return false;
			}
		};
	}

	public static <A> int atFirst(List<A> list, Predicate<A> block) {
		for (int n = 0; n < list.size(); n++) {
			if (block.is(list.get(n)))
				return n;
		}
		return -1;
	}

	public static <A> int atLast(List<A> list, Predicate<A> block) {
		for (int n = list.size() - 1; n >= 0; n--) {
			if (block.is(list.get(n)))
				return n;
		}
		return -1;
	}

	public static <T> T first(List<T> list) {
		return list.get(0);
	}

	public static <T> T last(List<T> list) {
		return list.get(list.size() - 1);
	}

	public static void out(Object object) {
		System.out.println(object);
	}

	public static void out(Object[] objects) {
		if (objects == null) {
			out(null);
			return;
		}
		System.out.print("[");
		for (int n = 0; n < objects.length; n++) {
			if (n != 0)
				System.out.print(", ");
			System.out.print(objects[n]);
		}
		System.out.println("]");
	}

	public static boolean isEqual(Object a, Object b) {
		return a == null ? b == null : a.equals(b);
	}

	// public static <A> List<List<A>> splitWhere(Iterable<A> coll, Predicate<A>
	// block) {
	// ArrayList<List<A>> result = new ArrayList<List<A>>();
	// ArrayList<A> run = new ArrayList<A>();
	// for (A each : coll) {
	// if (block.is(each) && !run.isEmpty()) {
	// run.trimToSize();
	// result.add(run);
	// run = new ArrayList<A>();
	// }
	// run.add(each);
	// }
	// result.trimToSize();
	// return result;
	// }

}
