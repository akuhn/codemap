package ch.akuhn.util.query;

import java.util.Iterator;

public final class Each<T> {

    private static class Iter<T> implements Iterable<Each<T>>, Iterator<Each<T>> {

        private final Iterator<T> elements;
        private int index = 0;

        public Iter(Iterator<T> elements) {
            this.elements = elements;
        }

        @Override
        public boolean hasNext() {
            return elements.hasNext();
        }

        @Override
        public Iterator<Each<T>> iterator() {
            return this;
        }

        @Override
        public Each<T> next() {
            return new Each<T>(index++, elements.next());
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

    }

    public static <T> Iterable<Each<T>> withIndex(Iterable<T> elements) {
        return new Iter<T>(elements.iterator());
    }

    public final T element;

    public final int index;

    public Each(int index, T element) {
        this.index = index;
        this.element = element;
    }

}
