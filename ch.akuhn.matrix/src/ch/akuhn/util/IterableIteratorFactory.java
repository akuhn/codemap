package ch.akuhn.util;

import java.util.Iterator;

public class IterableIteratorFactory {

    private final static class Iter<E> implements IterableIterator<E> {

        private Iterator<E> iterator;

        public Iter(final Iterator<E> iterator) {
            this.iterator = iterator;
        }

        public boolean hasMoreElements() {
            return iterator.hasNext();
        }

        public boolean hasNext() {
            return iterator.hasNext();
        }

        public Iterator<E> iterator() {
            return iterator;
        }

        public E next() {
            return iterator.next();
        }

        public E nextElement() {
            return iterator.next();
        }

        public void remove() {
            iterator.remove();
        }

    }

    public final static <E> IterableIterator<E> create(final Iterator<E> iterator) {
        return new Iter<E>(iterator);
    }

}
