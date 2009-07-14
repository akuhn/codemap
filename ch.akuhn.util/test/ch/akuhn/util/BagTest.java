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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import org.junit.Test;
import org.junit.runner.RunWith;

import ch.unibe.jexample.Given;
import ch.unibe.jexample.JExample;



@RunWith(JExample.class)
@SuppressWarnings("unchecked")
public class BagTest {

    private static String sortedPrint(Iterable iter) {
        return As.list(Get.sorted(iter)).toString();
    }

    @Test
    public void addNone() {
        Bag<String> bag = new Bag<String>();
        assertEquals(false, bag.add("aaa", 0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void cannotAddNegativeOccurrences() {
        new Bag().add("A", -1);
    }

    @Test(expected = IllegalArgumentException.class)
    @Given("#withAAA")
    public void cannotAddNegativeOccurrencesWithAAA(Bag bag) {
        bag.add("A", -1);
    }

    @Test
    @Given("#empty")
    public void clearEmpty(Bag bag) {
        bag.clear();
        assertEquals(0, bag.size());
        assertEquals(0, bag.uniqueSize());
        assertEquals(true, bag.isEmpty());
    }

    @Test
    @Given("#withMany")
    public void clearWithMany(Bag bag) {
        bag.clear();
        assertEquals(0, bag.size());
        assertEquals(0, bag.uniqueSize());
        assertEquals(true, bag.isEmpty());
    }

    @Test
    @Given("#empty")
    public void containsEmpty(Bag bag) {
        assertEquals(false, bag.contains("A"));
        assertEquals(false, bag.contains("B"));
        assertEquals(false, bag.contains("C"));
        assertEquals(false, bag.contains("D"));
    }

    @Test
    @Given("#withAAA")
    public void containsWithAAA(Bag bag) {
        assertEquals(true, bag.contains("A"));
        assertEquals(false, bag.contains("B"));
        assertEquals(false, bag.contains("C"));
        assertEquals(false, bag.contains("D"));
    }

    @Test
    @Given("#withABC")
    public void containsWithABC(Bag bag) {
        assertEquals(true, bag.contains("A"));
        assertEquals(true, bag.contains("B"));
        assertEquals(true, bag.contains("C"));
        assertEquals(false, bag.contains("D"));
    }

    @Test
    @Given("#withMany")
    public void containsWithMany(Bag bag) {
        assertEquals(true, bag.contains("A"));
        assertEquals(true, bag.contains("B"));
        assertEquals(true, bag.contains("C"));
    }

    @Test
    public Bag empty() {
        Bag bag = new Bag();
        assertEquals(0, bag.size());
        assertEquals(0, bag.uniqueSize());
        assertEquals(true, bag.isEmpty());
        return bag;
    }

    @Test
    public void equalsEmptyEmpty() {
        assertEquals(new Bag(), new Bag());
    }

    @Test
    @Given("#empty,#withA,#withAAA,#withABC")
    public void equalsIsReflexive(Bag empty, Bag withA, Bag withAAA, Bag withABC) {
        assertEquals(empty, empty);
        assertEquals(withA, withA);
        assertEquals(withAAA, withAAA);
        assertEquals(withABC, withABC);
    }

    @Test
    @Given("#withABC,#withABC")
    public void equalsWithABC(Bag first, Bag second) {
        assertNotSame(first, second);
        assertEquals(first, second);
    }

    @Test
    public void hashCodeEmptyEmpty() {
        assertEquals(new Bag().hashCode(), new Bag().hashCode());
    }

    @Test
    @Given("#withABC,#withABC")
    public void hashCodeWithABC(Bag first, Bag second) {
        assertNotSame(first, second);
        assertEquals(first.hashCode(), second.hashCode());
    }

    @Test
    @Given("#empty")
    public void iterateEmpty(Bag bag) {
        assertEquals(false, bag.iterator().hasNext());
        assertEquals(false, bag.elements().iterator().hasNext());
        assertEquals(false, bag.counts().iterator().hasNext());
    }

    @Test
    @Given("#withAAA")
    public void iterateWithAAA(Bag bag) {
        assertEquals("[A, A, A]", sortedPrint(bag));
        assertEquals("[A]", sortedPrint(bag.elements()));
        assertEquals("[3 x A]", sortedPrint(bag.counts()));
    }

    @Test
    @Given("#withABC")
    public void iterateWithABC(Bag bag) {
        assertEquals("[A, B, C]", sortedPrint(bag));
        assertEquals("[A, B, C]", sortedPrint(bag.elements()));
        assertEquals("[1 x A, 1 x B, 1 x C]", sortedPrint(bag.counts()));
    }

    @Test
    @Given("#withMany")
    public void iterateWithMany(Bag bag) {
        assertEquals("[A, A, A, A, B, C]", sortedPrint(bag));
        assertEquals("[A, B, C]", sortedPrint(bag.elements()));
        assertEquals("[4 x A, 1 x B, 1 x C]", sortedPrint(bag.counts()));
    }

    @Test
    @Given("#empty")
    public void maxOccurrencesEmpty(Bag bag) {
        int max = bag.maxOccurrences();
        assertEquals(0, max);
    }

    @Test
    @Given("#withA")
    public void maxOccurrencesWithA(Bag bag) {
        int max = bag.maxOccurrences();
        assertEquals(1, max);
    }

    @Test
    @Given("#withAAA")
    public void maxOccurrencesWithAAA(Bag bag) {
        int max = bag.maxOccurrences();
        assertEquals(3, max);
    }

    @Test
    @Given("#withMany")
    public void maxOccurrencesWithMany(Bag bag) {
        int max = bag.maxOccurrences();
        assertEquals(4, max);
    }

    @Test
    @Given("#empty")
    public void mostOccurringEmpty(Bag bag) {
        Object most = bag.mostOccurring();
        assertEquals(null, most);
    }

    @Test
    @Given("#withA")
    public void mostOccurringWithA(Bag bag) {
        Object most = bag.mostOccurring();
        assertEquals("A", most);
    }

    @Test
    @Given("#withAAA")
    public void mostOccurringWithAAA(Bag bag) {
        Object most = bag.mostOccurring();
        assertEquals("A", most);
    }

    @Test
    @Given("#withMany")
    public void mostOccurringWithMany(Bag bag) {
        Object most = bag.mostOccurring();
        assertEquals("A", most);
    }

    @Test
    @Given("#empty")
    public void occurrenceEmpty(Bag bag) {
        assertEquals(0, bag.occurrences("A"));
        assertEquals(0, bag.occurrences("B"));
        assertEquals(0, bag.occurrences("C"));
        assertEquals(0, bag.occurrences("D"));
    }

    @Test
    @Given("#withAAA")
    public void occurrenceWithAAA(Bag bag) {
        assertEquals(3, bag.occurrences("A"));
        assertEquals(0, bag.occurrences("B"));
        assertEquals(0, bag.occurrences("C"));
        assertEquals(0, bag.occurrences("D"));
    }

    @Test
    @Given("#withABC")
    public void occurrenceWithABC(Bag bag) {
        assertEquals(1, bag.occurrences("A"));
        assertEquals(1, bag.occurrences("B"));
        assertEquals(1, bag.occurrences("C"));
        assertEquals(0, bag.occurrences("D"));
    }

    @Test
    @Given("#withMany")
    public void occurrenceWithMany(Bag bag) {
        assertEquals(4, bag.occurrences("A"));
        assertEquals(1, bag.occurrences("B"));
        assertEquals(1, bag.occurrences("C"));
        assertEquals(0, bag.occurrences("D"));
    }

    @Test
    @Given("#empty")
    public void removeAllEmpty(Bag bag) {
        boolean f = bag.removeAllOccurrences("A");
        assertEquals(false, f);
        assertEquals(0, bag.size());
        assertEquals(0, bag.uniqueSize());
        assertEquals(true, bag.isEmpty());
    }

    @Test
    @Given("#withA")
    public void removeAllWithA(Bag bag) {
        boolean f = bag.removeAllOccurrences("A");
        assertEquals(true, f);
        assertEquals(0, bag.size());
        assertEquals(0, bag.uniqueSize());
        assertEquals(true, bag.isEmpty());
    }

    @Test
    @Given("#withAAA")
    public void removeAllWithAAA(Bag bag) {
        boolean f = bag.removeAllOccurrences("A");
        assertEquals(true, f);
        assertEquals(0, bag.size());
        assertEquals(0, bag.uniqueSize());
        assertEquals(true, bag.isEmpty());
    }

    @Test
    @Given("#withMany")
    public void removeAllWithMany(Bag bag) {
        boolean f = bag.removeAllOccurrences("A");
        assertEquals(true, f);
        assertEquals(2, bag.size());
        assertEquals(2, bag.uniqueSize());
        assertEquals(false, bag.isEmpty());
    }

    @Test
    @Given("#empty")
    public void removeEmpty(Bag bag) {
        boolean f = bag.remove("A");
        assertEquals(false, f);
        assertEquals(0, bag.size());
        assertEquals(0, bag.uniqueSize());
        assertEquals(true, bag.isEmpty());
    }

    @Test
    @Given("#withA")
    public void removeWithA(Bag bag) {
        boolean f = bag.remove("A");
        assertEquals(true, f);
        assertEquals(0, bag.size());
        assertEquals(0, bag.uniqueSize());
        assertEquals(true, bag.isEmpty());
    }

    @Test
    @Given("#withAAA")
    public void removeWithAAA(Bag bag) {
        boolean f = bag.remove("A");
        assertEquals(true, f);
        assertEquals(2, bag.size());
        assertEquals(1, bag.uniqueSize());
        assertEquals(false, bag.isEmpty());
    }

    @Test
    @Given("#withMany")
    public void removeWithMany(Bag bag) {
        boolean f = bag.remove("A");
        assertEquals(true, f);
        assertEquals(5, bag.size());
        assertEquals(3, bag.uniqueSize());
        assertEquals(false, bag.isEmpty());
    }

    @Test
    public void testAddOccurrencesOverflow() {
        Bag<String> bag = new Bag<String>();
        boolean changed = bag.add("A", Integer.MAX_VALUE - 200);
        assertEquals(Integer.MAX_VALUE - 200, bag.occurrences("A"));
        assertEquals(Integer.MAX_VALUE - 200, bag.size());
        changed = bag.add("A", 100);
        assertEquals(true, changed);
        assertEquals(Integer.MAX_VALUE - 100, bag.occurrences("A"));
        assertEquals(Integer.MAX_VALUE - 100, bag.size());
        changed = bag.add("A", 100);
        assertEquals(true, changed);
        assertEquals(Integer.MAX_VALUE, bag.occurrences("A"));
        assertEquals(Integer.MAX_VALUE, bag.size());
        changed = bag.add("A", 100);
        assertEquals(false, changed);
        assertEquals(Integer.MAX_VALUE, bag.occurrences("A"));
        assertEquals(Integer.MAX_VALUE, bag.size());
    }

    @Test
    public void testAddOccurrencesOverflow2() {
        Bag<String> bag = new Bag<String>();
        boolean changed = bag.add("A", Integer.MAX_VALUE - 150);
        assertEquals(Integer.MAX_VALUE - 150, bag.occurrences("A"));
        assertEquals(Integer.MAX_VALUE - 150, bag.size());
        changed = bag.add("A", 100);
        assertEquals(true, changed);
        assertEquals(Integer.MAX_VALUE - 50, bag.occurrences("A"));
        assertEquals(Integer.MAX_VALUE - 50, bag.size());
        changed = bag.add("A", 100);
        assertEquals(true, changed);
        assertEquals(Integer.MAX_VALUE, bag.occurrences("A"));
        assertEquals(Integer.MAX_VALUE, bag.size());
        changed = bag.add("A", 100);
        assertEquals(false, changed);
        assertEquals(Integer.MAX_VALUE, bag.occurrences("A"));
        assertEquals(Integer.MAX_VALUE, bag.size());
    }

    @Test
    public void testAddOccurrencesOverflow3() {
        Bag<String> bag = new Bag<String>();
        boolean changed = bag.add("A", Integer.MAX_VALUE - 200);
        assertEquals(Integer.MAX_VALUE - 200, bag.occurrences("A"));
        assertEquals(Integer.MAX_VALUE - 200, bag.size());
        changed = bag.add("A", Integer.MAX_VALUE - 200);
        assertEquals(true, changed);
        assertEquals(Integer.MAX_VALUE, bag.occurrences("A"));
        assertEquals(Integer.MAX_VALUE, bag.size());
        changed = bag.add("A", Integer.MAX_VALUE - 200);
        assertEquals(false, changed);
        assertEquals(Integer.MAX_VALUE, bag.occurrences("A"));
        assertEquals(Integer.MAX_VALUE, bag.size());
    }

    @Test
    public void testAddOverflow() {
        Bag<String> bag = new Bag<String>();
        boolean changed = bag.add("A", Integer.MAX_VALUE - 2);
        assertEquals(Integer.MAX_VALUE - 2, bag.occurrences("A"));
        assertEquals(Integer.MAX_VALUE - 2, bag.size());
        changed = bag.add("A");
        assertEquals(true, changed);
        assertEquals(Integer.MAX_VALUE - 1, bag.occurrences("A"));
        assertEquals(Integer.MAX_VALUE - 1, bag.size());
        changed = bag.add("A");
        assertEquals(true, changed);
        assertEquals(Integer.MAX_VALUE, bag.occurrences("A"));
        assertEquals(Integer.MAX_VALUE, bag.size());
        changed = bag.add("A");
        assertEquals(false, changed);
        assertEquals(Integer.MAX_VALUE, bag.occurrences("A"));
        assertEquals(Integer.MAX_VALUE, bag.size());
    }

    @Test
    @Given("#empty,#withA,#withAAA,#withABC")
    public void toString(Bag empty, Bag withA, Bag withAAA, Bag withABC) {
        assertEquals("[]", empty.toString());
        assertEquals("[A]", withA.toString());
        assertEquals("[A, A, A]", withAAA.toString());
        // hash key order is not deterministic, thus check for length only!
        assertEquals("[A, B, C]".length(), withABC.toString().length());
    }

    @Test
    @Given("#empty")
    public Bag withA(Bag bag) {
        bag.add("A");
        assertEquals(1, bag.size());
        assertEquals(1, bag.uniqueSize());
        assertEquals(false, bag.isEmpty());
        return bag;
    }

    @Test
    @Given("#withA")
    public Bag withAAA(Bag bag) {
        bag.add("A");
        bag.add("A");
        assertEquals(3, bag.size());
        assertEquals(1, bag.uniqueSize());
        assertEquals(false, bag.isEmpty());
        return bag;
    }

    @Test
    @Given("#withA")
    public Bag withABC(Bag bag) {
        bag.add("B");
        bag.add("C");
        assertEquals(3, bag.size());
        assertEquals(3, bag.uniqueSize());
        assertEquals(false, bag.isEmpty());
        return bag;
    }

    @Test
    @Given("#withAAA,#withABC")
    public Bag withMany(Bag bag, Bag more) {
        bag.addAll(more);
        assertEquals(6, bag.size());
        assertEquals(3, bag.uniqueSize());
        assertEquals(false, bag.isEmpty());
        return bag;
    }

}
