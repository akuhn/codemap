package ch.akuhn.util.query;

import java.util.Iterator;

public class Each<T> {
        
        public int index;
        public T element;
        
        public static <T> Iterable<Each<T>> withIndex(Iterable<T> elements) {
            return new Each<T>().iterable(elements);
        }
        
        private Iterable<Each<T>> iterable(Iterable<T> elements) {
            return new Iter(elements.iterator());
        }
        
        private class Iter implements Iterable<Each<T>>, Iterator<Each<T>> {

            private final Iterator<T> elements;
            private int index = 0;
            
            public Iter(Iterator<T> elements) {
                this.elements = elements;
            }
            
            @Override
            public Iterator<Each<T>> iterator() {
                return this;
            }

            @Override
            public boolean hasNext() {
                return elements.hasNext();
            }

            @Override
            public Each<T> next() {
                Each.this.element = elements.next();
                Each.this.index = index;
                index++;
                return Each.this;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
            
        }
    }

