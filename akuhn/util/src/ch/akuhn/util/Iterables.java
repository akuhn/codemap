//  Copyright (c) 2007 Adrian Kuhn <akuhn(a)iam.unibe.ch>
//
//  This file is part of "Adrian Kuhn's Utilities for Java".
//
//  "Adrian Kuhn's Utilities for Java" is free software: you can redistribute it
//  and/or modify it under the terms of the GNU Lesser General Public License as
//  published by the Free Software Foundation, either version 3 of the License,
//  or (at your option) any later version.
//
//  "Adrian Kuhn's Utilities for Java" is distributed in the hope that it will be
//  useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser
//  General Public License for more details.
//
//  You should have received a copy of the GNU Lesser General Public License along
//  with "Adrian Kuhn's Utilities for Java". If not, see
//  <http://www.gnu.org/licenses/>.
//

package ch.akuhn.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Iterables {

	public static <A> boolean allSatisfy(Iterable<A> coll, Predicate<A> block) {
		for (A each : coll) {
			if (!block.is(each))
				return false;
		}
		return true;
	}

	public static <T> T any(Iterable<T> coll) {
		return coll.iterator().next();
	}

	public static <A> boolean anySatisfy(Iterable<A> coll, Predicate<A> block) {
		for (A each : coll) {
			if (block.is(each))
				return true;
		}
		return false;
	}

	public static <T, A> Collection<T> collect(Iterable<A> coll,
			Function<T, A> block) {
		Collection<T> result = new ArrayList<T>();
		for (A each : coll) {
			result.add(block.eval(each));
		}
		return result;
	}

	public static <A> int count(Iterable<A> coll, Predicate<A> block) {
		int count = 0;
		for (A each : coll) {
			if (block.is(each))
				count++;
		}
		return count;
	}

	public static <A> A detect(Iterable<A> coll, Predicate<A> block) {
		for (A each : coll) {
			if (block.is(each))
				return each;
		}
		return null;
	}

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

	private static <A> Predicate<A> not(final Predicate<A> block) {
		return new Predicate<A>() {
			@Override
			public boolean is(A a) {
				return !block.is(a);
			}
		};
	}

	public static <A> Collection<A> reject(Iterable<A> coll, Predicate<A> block) {
		return select(coll, not(block));
	}

	public static <A> void remove(Iterable<A> coll, Predicate<A> block) {
		for (Iterator<A> iterator = coll.iterator(); iterator.hasNext();) {
			A each = iterator.next();
			if (block.is(each)) {
				iterator.remove();
			}
		}
	}

	public static <A> List<List<A>> runsFailing(Iterable<A> coll,
			Predicate<A> block) {
		return runsSatisfying(coll, not(block));
	}

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

	public static <A> Collection<A> select(Iterable<A> coll, Predicate<A> block) {
		Collection<A> result = new ArrayList<A>();
		for (A each : coll) {
			if (block.is(each)) {
				result.add(each);
			}
		}
		return result;
	}

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
