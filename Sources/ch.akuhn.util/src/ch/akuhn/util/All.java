package ch.akuhn.util;

import ch.akuhn.util.blocks.Predicate;

/** A utility class to realize universally quantified checks of an entire collection.
 * 
 * @author Adrian Kuhn
 * @author Yossi Gil, 21/06/2008
 * 
 */
public class All {

	public static boolean notNull(Object[] values) {
		assert values != null;
		for (Object o : values) {
			if (o == null) return false;
		}
		return true;
	}

	public static <T> boolean notNull(Iterable<T> iter) {
		assert iter != null;
		for (T t : iter) {
			if (t == null) return false;
		}
		return true;
	}
	
	public static <T> boolean satisfy(Iterable<T> iter, Predicate<T> block) {
		for (T each : iter) if (!block.apply(each)) return false;
		return true;
	}
	
	public static <T> boolean satisfy(Iterable<T> iter, String reference) {
		return All.satisfy(iter, Methods.<T>asPredicate(reference));
	}
	
}
