package ch.akuhn.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

public abstract class Providable<E> implements Iterable<E>, Iterator<E>, Cloneable {

    private enum State {
        DONE, EMPTY, FAIL, READY, UNINITIALIZED 
    }

    private E next = null;

    private State state = State.UNINITIALIZED;

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

    public final boolean hasNext() {
        switch (state) {
        case FAIL:
        case UNINITIALIZED:
            throw new IllegalStateException(state.toString());
        case DONE:
            return false;
        case READY:
            return true;
        default:
            return computeNext();
        }
    }

    public abstract void initialize();

    public final Iterator<E> iterator() {
        Providable<E> clone = maybeClone();
        clone.state = State.FAIL;
        clone.initialize();
        clone.state = State.EMPTY;
        return clone;
    }
    
    @SuppressWarnings("unchecked")
    private final Providable<E> maybeClone() {
        if (state == State.UNINITIALIZED) return this;
        try {
            return (Providable<E>) super.clone();
        } catch (CloneNotSupportedException ex) {
            throw Throw.exception(ex);
        }
    }
    
    public final E next() {
        if (!hasNext()) throw new NoSuchElementException();
        state = State.EMPTY;
        return next;
    }

    public abstract E provide();
    
    public final void remove() {
        throw new UnsupportedOperationException();
    }

}
