package ch.akuhn.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

public abstract class Provider<E> implements IterableIterator<E> {

	public boolean hasMoreElements() {
		return this.hasNext();
	}

	public E nextElement() {
		return this.next;
	}

	private enum State { DONE, EMPTY, FAIL, READY }
	
	private E next = null;
	private State state  = State.EMPTY;

	public boolean hasNext() {
	    if (state == State.FAIL) throw new IllegalStateException();
	    switch (state) {
	    	case DONE: return false;
	    	case READY: return true;
	        default: return computeNext();
	    }
	}

	private boolean computeNext() {
		state = State.FAIL; 
		next = provide();
		if (state != State.DONE) {
			state = State.READY;
			return true;
		}
		return false;
	}	
	
	public E next() {
	    if (!hasNext()) throw new NoSuchElementException();
	    state = State.EMPTY;
	    return next;
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}

	public Iterator<E> iterator() {
		return this;
	}

	public abstract E provide();

	public final E done() {
		state = State.DONE;
		return null;
	}

}
