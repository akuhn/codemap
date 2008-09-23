package magic;

/** A utility class to realize universally quantified checks of an entire collection.
 * 
 * @author Yossi Gil, 21/06/2008
 * 
 */
public class All {

	private All() {
		throw new RuntimeException("Cannot instantiate.");
	}
	
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
	
}
