package magic;

import java.util.Iterator;
import java.util.NoSuchElementException;

/** Maybe monad for Java.
 * 
 * @author Tony Morris, 2006/11/13
 * @author Daniel Spiewak, 2008/04/07
 * @author Adrian Kuhn, 2008/08/08
 *
 * @param <T>
 */
public abstract class Maybe<T> implements Iterable<T> {

    public abstract T get();
    
    private Maybe() {
        // avoid more than two subclasses
    }
    
    public static final class Some<T> extends Maybe<T> {

        private final T t;
        
        private Some(T t) {
            this.t = t;
        }
        
        @Override
        public T get() {
            return t;
        }

        //@Override
        public Iterator<T> iterator() {
           return new Iterator<T>() {
               private boolean done = false;
            //@Override
            public boolean hasNext() {
                return !done;
            }

            //@Override
            public T next() {
                if (done) throw new NoSuchElementException();
                done = true;
                return t;
            }

            //@Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
               
           };
        }
        
    }
    
    public static final class None<T> extends Maybe<T> {

        private None() {
        }
        
        @Override
        public T get() {
            throw new UnsupportedOperationException("Cannot resolve value on None");
        }

        //@Override
        @SuppressWarnings("unchecked")
        public Iterator<T> iterator() {
            return NONE_ITER;
        }
        
    }

    @SuppressWarnings("unchecked")
    private static final None NONE = new None();

    @SuppressWarnings("unchecked")
    private static final Iterator NONE_ITER = new Iterator() {

        //@Override
        public boolean hasNext() {
            return false;
        }

        //@Override
        public Object next() {
            throw new NoSuchElementException();
        }

        //@Override
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
    
}
