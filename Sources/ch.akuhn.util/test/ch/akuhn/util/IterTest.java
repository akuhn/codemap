//  Copyright (c) 1998-2008 Adrian Kuhn <akuhn(a)students.unibe.ch>
//  
//  This file is part of ch.akuhn.util.
//  
//  ch.akuhn.util is free software: you can redistribute it and/or modify it
//  under the terms of the GNU Lesser General Public License as published by the
//  Free Software Foundation, either version 3 of the License, or (at your
//  option) any later version.
//  
//  ch.akuhn.util is distributed in the hope that it will be useful, but
//  WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
//  or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
//  License for more details.
//  
//  You should have received a copy of the GNU Lesser General Public License
//  along with ch.akuhn.util. If not, see <http://www.gnu.org/licenses/>.
//  

package ch.akuhn.util;

import static ch.akuhn.util.Extensions.newList;
import static ch.akuhn.util.Extensions.shuffle;
import static ch.akuhn.util.Extensions.sort;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

public class IterTest {

    private Collection<String> empty;
    private Collection<String> many;
    private Collection<String> single;

    @Test
    public void cycleEmpty() {
        Iterator<String> cycle = Extensions.cycle(empty).iterator();
        assertTrue(!cycle.hasNext());
    }

    @Test
    public void cycleMany() {
        Iterator<String> cycle = Extensions.cycle(many).iterator();
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
        Iterator<String> cycle = Extensions.cycle(single).iterator();
        assertTrue(cycle.hasNext());
        assertEquals("boe", cycle.next());
        assertTrue(cycle.hasNext());
        assertEquals("boe", cycle.next());
        assertTrue(cycle.hasNext());
        assertEquals("boe", cycle.next());
    }

    @Test
    public void cycleSingleRemove() {
        Iterator<String> cycle = Extensions.cycle(single).iterator();
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
        java.util.List<Integer> a = newList(1, 2, 3, 4, 5, 6, 7, 8, 9);
        Iterable<Integer> b = shuffle(a);
        java.util.List<Integer> c = newList(b);
        java.util.List<Integer> d = newList(sort(c));
        assertEquals(a, d);
    }

}
