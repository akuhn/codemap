//  Copyright (c) 1998-2008 Adrian Kuhn <akuhn(a)students.unibe.ch>
//  
//  This file is part of ch.akuhn.util.
//  
//  ch.akuhn.util is free software: you can redistribute it and/or modify it
//  under the terms of the GNU Lesser General Public License as published by the
//  Free Software Foundation, either version 3 of the License, or (at your
//  option) any later version.
//  
//  ch.akuhn.util is distributed in the hope that it will be useful, but
//  WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
//  or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
//  License for more details.
//  
//  You should have received a copy of the GNU Lesser General Public License
//  along with ch.akuhn.util. If not, see <http://www.gnu.org/licenses/>.
//  

package magic.blocks;

import java.util.*;

import magic.Extensions;


public class Blocks {

	private Blocks() {
		throw new UnsupportedOperationException();
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

	/** Same as {@link collect}, but returns the results as set. */
	public static <T, A> Set<T> collectAsSet(Iterable<A> coll,
			Function<T, A> block) {
		Set<T> result = new HashSet<T>();
		for (A each : coll) {
			result.add(block.eval(each));
		}
		return result;
	}

	/** Checks if the specified array contains the specified element.
	 * 
	 */
	public static <A> boolean contains(A[] array, A a) {
		for (A each : array) {
			if (Extensions.eq(each, a)) return true;
		}
		return false;
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

	public static <T> T first(List<T> list) {
		return list.get(0);
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

	public static <A> Predicate<A> not(final Predicate<A> block) {
		return new Predicate<A>() {
			
			public boolean is(A a) {
				return !block.is(a);
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

	/** Returns all elements that do <i>not</i> satisfy the specified predicate.
	 * 
	 */
	public static <A> Collection<A> reject(Iterable<A> coll, Predicate<A> block) {
		return select(coll, not(block));
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

	/** Returns a list of all subsequences whose elements do <i>not</i> satisfy the specified predicate. 
	 * 
	 */
	public static <A> List<List<A>> runsFailing(Iterable<A> coll,
			Predicate<A> block) {
		return runsSatisfying(coll, not(block));
	}
	
}
