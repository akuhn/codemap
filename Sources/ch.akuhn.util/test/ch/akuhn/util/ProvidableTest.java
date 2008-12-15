package ch.akuhn.util;

import static org.junit.Assert.*;

import java.util.Iterator;

import org.junit.Test;

public class ProvidableTest {

    private Providable<Integer> newDummy() {
        return new Providable<Integer>() {
            private Integer number;
            public void initialize() {
                number = 42;
            }
            public Integer provide() {
                return number < 45 ? number++ : done();
            }
        };
    }
    
    @Test
    public void testIterator() {
        Iterator<Integer> iter = newDummy().iterator();
        assertEquals(42, iter.next());
        assertEquals(43, iter.next());
        assertEquals(44, iter.next());
        assertEquals(false, iter.hasNext());
    }
    
    @Test
    public void testFirstIsNotClone() {
        Iterable<Integer> dummy = newDummy();
        Iterator<Integer> first = dummy.iterator();
        Iterator<Integer> second = dummy.iterator();
        assertSame(dummy, first);
        assertNotSame(dummy, second);
    }
    
    @Test
    public void testTwoIterator() {
        Iterable<Integer> dummy = newDummy();
        Iterator<Integer> iter_A = dummy.iterator();
        Iterator<Integer> iter_B = dummy.iterator();
        assertNotSame(iter_A,iter_B);
        assertEquals(42, iter_A.next());
        assertEquals(43, iter_A.next());
        assertEquals(42, iter_B.next());
        assertEquals(44, iter_A.next());
        assertEquals(43, iter_B.next());
        assertEquals(false, iter_A.hasNext());
        assertEquals(44, iter_B.next());
        assertEquals(false, iter_A.hasNext());
    }
    
    @Test(expected=IllegalStateException.class)
    public void testProvidableIsNotAnIteratorNext() {
        newDummy().next();
    }
    
    @Test(expected=IllegalStateException.class)
    public void testProvidableIsNotAnIteratorHasNext() {
        newDummy().hasNext();
    }

}
