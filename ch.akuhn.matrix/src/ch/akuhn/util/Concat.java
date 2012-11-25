    package ch.akuhn.util;
    
    import java.util.Iterator;
    import java.util.NoSuchElementException;
    
    public class Concat {
    
        public static <T> Iterable<T> all(final Iterable<T>... iterables) {
            return new Iterable<T>() {
                @Override
                public Iterator<T> iterator() {
                    return new Iterator<T>() {
                        Iterator<Iterable<T>> more = Arrays.asList(iterables).iterator();
                        Iterator<T> current = more.hasNext() ? more.next().iterator() : null;
                        @Override
                        public boolean hasNext() {
                            if (current == null) return false;
                            if (current.hasNext()) return true;
                            current = more.hasNext() ? more.next().iterator() : null;
                            return this.hasNext();
                        }
    
                        @Override
                        public T next() {
                            if (!hasNext()) throw new NoSuchElementException();
                            return current.next();
                        }
    
                        @Override
                        public void remove() {
                            throw new UnsupportedOperationException();
                        }
                    };
                }
            };
        }
        
    }
