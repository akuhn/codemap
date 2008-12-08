package ch.akuhn.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

public abstract class Provider<E> implements IterableIterator<E> {

    private enum State {
        DONE, EMPTY, FAIL, READY
    }

    private E next = null;

    private State state = State.EMPTY;

    private final boolean computeNext() {
        state = State.FAIL;
        next = provide();
        if (state != State.DONE) {
            state = State.READY;
            return true;
        }
        return false;
    }
    public final E done() {
        state = State.DONE;
        return null;
    }

    public final boolean hasMoreElements() {
        return this.hasNext();
    }

    public final boolean hasNext() {
        if (state == State.FAIL) throw new IllegalStateException();
        switch (state) {
        case DONE:
            return false;
        case READY:
            return true;
        default:
            return computeNext();
        }
    }

    public final Iterator<E> iterator() {
        return this;
    }

    public final E next() {
        if (!hasNext()) throw new NoSuchElementException();
        state = State.EMPTY;
        return next;
    }

    public final E nextElement() {
        return this.next;
    }

    public abstract E provide();

    public final void remove() {
        throw new UnsupportedOperationException();
    }

}
