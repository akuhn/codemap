package ch.akuhn.util;

/** Static methods that operate on or return predicates.
*/
public class Predicates {

	public static <A> Predicate<A> isNull() {
		return new Predicate<A>() {
			@Override
			public boolean is(A a) {
				return null == a;
			}
		};
	}
	
	public static <A> Predicate<A> instanceOf(final Class<?> jazz) {
		return new Predicate<A>() {
			@Override
			public boolean is(A a) {
				return a == null || jazz.isAssignableFrom(a.getClass());
			}
		};
	}

	public static <A> Predicate<A> notNull() {
		return new Predicate<A>() {
			@Override
			public boolean is(A a) {
				return null != a;
			}
		};
	}
	
	public static <A> Predicate<A> not(final Predicate<A> block) {
		return new Predicate<A>() {
			@Override
			public boolean is(A a) {
				return !block.is(a);
			}
		};
	}

	public static <A> Predicate<A> and(final Predicate<A>... blocks) {
		return new Predicate<A>() {
			@Override
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

	public static <A> Predicate<A> or(final Predicate<A>... blocks) {
		return new Predicate<A>() {
			@Override
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
	
}
