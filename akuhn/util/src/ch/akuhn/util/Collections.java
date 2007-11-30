package ch.akuhn.util;

import java.util.*;

public class Collections {

	public static <T> T any(Collection<T> coll) {
		return coll.iterator().next();
	}
	
	public static <T> T first(List<T> list) {
		return list.get(0);
	}
	
	public static <T> T last(List<T> list) {
		return list.get(list.size() - 1);
	}
	
	public static <T,A> Collection<T> collect(Collection<A> coll, Function<T,A> block) {
		Collection<T> result = new ArrayList<T>(coll.size());
		for (A each : coll) {
			result.add(block.eval(each));
		}
		return result;
	}
	
	public static <A> Collection<A> select(Collection<A> coll, Predicate<A> block) {
		Collection<A> result = new ArrayList<A>();
		for (A each : coll) {
			if (block.is(each)) {
				result.add(each);
			}
		}
		return result;
	}

	public static <A> Collection<A> remove(Collection<A> coll, Predicate<A> block) {
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
	
	
	public static <A> Collection<A> reject(Collection<A> coll, Predicate<A> block) {
		Collection<A> result = new ArrayList<A>();
		for (A each : coll) {
			if (!block.is(each)) {
				result.add(each);
			}
		}
		return result;
	}
	
	public static <A> int count(Collection<A> coll, Predicate<A> block) {
		int count = 0;
		for (A each : coll) {
			if (block.is(each)) count++;
		}
		return count;
	}

	public static <A> void forEach(Collection<A> coll, Procedure<A> block) {
		for (A each : coll) {
			block.run(each);
		}
	}
	
	public static <A> A detect(Collection<A> coll, Predicate<A> block) {
		for (A each : coll) {
			if (block.is(each)) return each;
		}
		throw new NoSuchElementException();
	}
	
	public static <T,A> T inject(Collection<A> coll, T initialValue, BinaryFunction<T,T,A> block) {
		T value = initialValue;
		for (A each : coll) {
			value = block.eval(value, each);
		}
		return value;
	}
	
	
}
