
package ch.akuhn.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;



public class CycleTest {

    private Collection<String> empty;
    private Collection<String> many;
    private Collection<String> single;

    @Test
    public void cycleEmpty() {
        Iterator<String> cycle = Cycle.forever(empty).iterator();
        assertTrue(!cycle.hasNext());
    }

    @Test
    public void cycleMany() {
        Iterator<String> cycle = Cycle.forever(many).iterator();
        assertTrue(cycle.hasNext());
        assertEquals("foo", cycle.next());
        assertTrue(cycle.hasNext());
        assertEquals("bar", cycle.next());
        assertTrue(cycle.hasNext());
        assertEquals("qux", cycle.next());
        assertTrue(cycle.hasNext());
        assertEquals("foo", cycle.next());
    }

    @Test
    public void cycleSingle() {
        Iterator<String> cycle = Cycle.forever(single).iterator();
        assertTrue(cycle.hasNext());
        assertEquals("boe", cycle.next());
        assertTrue(cycle.hasNext());
        assertEquals("boe", cycle.next());
        assertTrue(cycle.hasNext());
        assertEquals("boe", cycle.next());
    }

    @Test
    public void cycleSingleRemove() {
        Iterator<String> cycle = Cycle.forever(single).iterator();
        assertTrue(cycle.hasNext());
        assertEquals("boe", cycle.next());
        assertTrue(cycle.hasNext());
        assertEquals("boe", cycle.next());
        cycle.remove();
        assertTrue(!cycle.hasNext());
    }

    @Before
    public void fixture() {
        empty = new ArrayList<String>();
        single = new ArrayList<String>();
        single.add("boe");
        many = new ArrayList<String>();
        many.add("foo");
        many.add("bar");
        many.add("qux");
    }

    @Test
    public void sortIterable() {
        java.util.List<Integer> a = As.list(1, 2, 3, 4, 5, 6, 7, 8, 9);
        Iterable<Integer> b = Get.shuffle(a);
        java.util.List<Integer> c = As.list(b);
        java.util.List<Integer> d = As.list(Get.sorted(c));
        assertEquals(a, d);
    }

}
