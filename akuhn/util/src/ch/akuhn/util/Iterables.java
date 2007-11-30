package ch.akuhn.util;

import java.util.*;

public class Iterables {

	private static <A> Predicate<A> not(final Predicate<A> block) {
		return new Predicate<A>() {
			@Override
			public boolean is(A a) {
				return !block.is(a);
			}
		};
	}
	
	public static <T> T any(Iterable<T> coll) {
		return coll.iterator().next();
	}
	
	public static <T,A> Collection<T> collect(Iterable<A> coll, Function<T,A> block) {
		Collection<T> result = new ArrayList<T>();
		for (A each : coll) {
			result.add(block.eval(each));
		}
		return result;
	}

	public static <T,A> Map<T,Collection<A>> groupedBy(Iterable<A> coll, Function<T,A> block) {
		Map<T,Collection<A>> result = new HashMap<T,Collection<A>>();
		for (A each : coll) {
			T key = block.eval(each);
			Collection<A> bucket = result.get(key);
			if (bucket == null) result.put(key, bucket = new ArrayList<A>());
			bucket.add(each);
		}
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

	public static <A> void remove(Iterable<A> coll, Predicate<A> block) {
		for (Iterator<A> iterator = coll.iterator(); iterator.hasNext();) {
			A each = iterator.next();
			if (block.is(each)) {
				iterator.remove();
			}
		}
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
	
	public static <A> Collection<A> reject(Iterable<A> coll, Predicate<A> block) {
		return select(coll, not(block));
	}
	
	public static <A> int count(Iterable<A> coll, Predicate<A> block) {
		int count = 0;
		for (A each : coll) {
			if (block.is(each)) count++;
		}
		return count;
	}
	
	public static <A> boolean anySatisfy(Iterable<A> coll, Predicate<A> block) {
		for (A each : coll) {
			if (block.is(each)) return true;
		}
		return false;
	}
	
	public static <A> boolean allSatisfy(Iterable<A> coll, Predicate<A> block) {
		for (A each : coll) {
			if (!block.is(each)) return false;
		}
		return true;
	}

	public static <A> void forEach(Iterable<A> coll, Procedure<A> block) {
		for (A each : coll) {
			block.run(each);
		}
	}
	
	public static <A> A detect(Iterable<A> coll, Predicate<A> block) {
		for (A each : coll) {
			if (block.is(each)) return each;
		}
		return null;
	}
	
	public static <T,A> T inject(Iterable<A> coll, T initialValue, BinaryFunction<T,T,A> block) {
		T value = initialValue;
		for (A each : coll) {
			value = block.eval(value, each);
		}
		return value;
	}
	
	public static <A> List<List<A>> runsSatisfying(Iterable<A> coll, Predicate<A> block) {
		ArrayList<List<A>> result = new ArrayList<List<A>>();
		ArrayList<A> run = null;
		for (A each : coll) {
			if (block.is(each)) {
				if (run == null) run = new ArrayList<A>();
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
	
	public static <A> List<List<A>> runsFailing(Iterable<A> coll, Predicate<A> block) {
		return runsSatisfying(coll, not(block));
	}
	
//	public static <A> List<List<A>> splitWhere(Iterable<A> coll, Predicate<A> block) {
//		ArrayList<List<A>> result = new ArrayList<List<A>>();
//		ArrayList<A> run = new ArrayList<A>();
//		for (A each : coll) {
//			if (block.is(each) && !run.isEmpty()) {
//				run.trimToSize();
//				result.add(run);
//				run = new ArrayList<A>(); 
//			}
//			run.add(each);
//		}
//		result.trimToSize();
//		return result;
//	}
	
	
}
