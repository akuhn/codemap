package ch.akuhn.util.query;

import java.util.Iterator;

public class Times {

    public static final Iterable<Void> repeat(final long times) {
        return new Iterable<Void>() {
            @Override
            public Iterator<Void> iterator() {
                return new Iterator<Void>() {
                    private long n = 0;
                    @Override
                    public boolean hasNext() {
                        return n < times;
                    }

                    @Override
                    public Void next() {
                        n++;
                        return null;
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
