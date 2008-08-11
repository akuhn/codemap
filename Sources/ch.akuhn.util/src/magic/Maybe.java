package magic;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Avoids void clutter in the presence of null. Your eyes do not deceive you,
 * this is Haskell's maybe monad in Java. Use it as follows.
 * 
 * <pre>
 * public static void main(String[] args) {
 *    for (String arg: Maybe.last(args)) {
 *        System.out.println(arg);
 *    }
 * 
 * </pre>
 * 
 * @author Tony Morris, 2006/11/13
 * @author Daniel Spiewak, 2008/08/07
 * @author Stephan Schmidt, 2008/08/06
 * @author Adrian Kuhn, 2008/08/08
 * 
 * @see http://blog.tmorris.net/maybe-in-java/
 * @see http://www.codecommit.com/blog/scala/the-option-pattern
 * @see http
 *      ://stephan.reposita.org/archives/2008/08/06/for-hack-with-option-monad
 *      -in-java/
 * 
 */
public abstract class Maybe<T> implements Iterable<T> {

	public abstract T get();
	
	public abstract boolean isNone();
	
	public abstract boolean isSome();

	private Maybe() {
		// avoid more than two subclasses
	}

	private static final class Some<T> extends Maybe<T> {

		private final T t;

		private Some(T t) {
			this.t = t;
		}

		@Override
		public T get() {
			return t;
		}

		// @Override
		public Iterator<T> iterator() {
			return new Iterator<T>() {
				private boolean done = false;

				// @Override
				public boolean hasNext() {
					return !done;
				}

				// @Override
				public T next() {
					if (done)
						throw new NoSuchElementException();
					done = true;
					return t;
				}

				// @Override
				public void remove() {
					throw new UnsupportedOperationException();
				}

			};
		}

		@Override
		public boolean isNone() {
			return false;
		}

		@Override
		public boolean isSome() {
			return true;
		}

	}

	private static final class None<T> extends Maybe<T> {

		private None() {
		}

		@Override
		public T get() {
			throw new NoSuchElementException("Cannot resolve value on None");
		}

		// @Override
		@SuppressWarnings("unchecked")
		public Iterator<T> iterator() {
			return NONE_ITER;
		}
		
		@Override
		public boolean isNone() {
			return true;
		}

		@Override
		public boolean isSome() {
			return false;
		}


	}

	@SuppressWarnings("unchecked")
	private static final None NONE = new None();

	@SuppressWarnings("unchecked")
	private static final Iterator NONE_ITER = new Iterator() {

		// @Override
		public boolean hasNext() {
			return false;
		}

		// @Override
		public Object next() {
			throw new NoSuchElementException();
		}

		// @Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

	};

	@SuppressWarnings("unchecked")
	public static <T> Maybe<T> none() {
		return NONE;
	}

	public static <T> Maybe<T> some(T t) {
		return new Some<T>(t);
	}

	public static <T> Maybe<T> maybe(T t) {
		return t == null ? Maybe.<T> none() : Maybe.<T> some(t);
	}

	public static <T> Maybe<T> maybe(Maybe<T> maybe) {
		return maybe;
	}

	public static final <E> Maybe<E> first(E[] es) {
		if (es.length == 0)
			return none();
		return maybe(es[0]);
	}

	public static final <E> Maybe<E> first(Iterable<E> es) {
		for (E e : es)
			return maybe(e);
		return none();
	}

	public static final <E> Maybe<E> first(List<E> es) {
		if (es.isEmpty())
			return none();
		return maybe(es.get(0));
	}

	public static final <E> Maybe<E> last(E[] es) {
		if (es.length == 0)
			return none();
		return maybe(es[es.length - 1]);
	}

	public static final <E> Maybe<E> last(Iterable<E> es) {
		E $ = null;
		for (E e : es)
			$ = e;
		return maybe($);
	}

	public static final <E> Maybe<E> last(List<E> es) {
		if (es.isEmpty())
			return none();
		return maybe(es.get(es.size() - 1));
	}

}
