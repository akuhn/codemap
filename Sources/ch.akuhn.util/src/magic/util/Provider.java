package magic.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

@SuppressWarnings("unchecked")
public abstract class Provider<E> implements Iterator<E>, Iterable<E> {

	private static final Object EMPTY = new Object();
	private static final Object DONE = new Object();
	private Object next = EMPTY;

	public boolean hasNext() {
		if (next == EMPTY)
			update();
		return next != DONE;
	}

	private void update() {
		try {
			next = provide();
		} catch (Done done) {
			if (done.owner != this)
				throw done;
			next = DONE;
		}
	}

	public E next() {
		if (next == EMPTY)
			update();
		if (next == DONE)
			throw new NoSuchElementException();
		Object $ = next;
		next = EMPTY;
		return (E) $;
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}

	public Iterator<E> iterator() {
		return this;
	}

	public abstract E provide();

	public E done() {
		throw new Done(this);
	}

	private static class Done extends NoSuchElementException {
		private Provider owner;

		public Done(Provider owner) {
			this.owner = owner;
		}
	}

}
