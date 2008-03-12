//  Copyright (c) 1998-2008 Adrian Kuhn <akuhn(a)iam.unibe.ch>
//  
//  This file is part of "Adrian Kuhn's Utilities for Java".
//  
//  "Adrian Kuhn's Utilities for Java" is free software: you can redistribute
//  it and/or modify it under the terms of the GNU Lesser General Public License
//  as published by the Free Software Foundation, either version 3 of the
//  License, or (at your option) any later version.
//  
//  "Adrian Kuhn's Utilities for Java" is distributed in the hope that it will
//  be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser
//  General Public License for more details.
//  
//  You should have received a copy of the GNU Lesser General Public License
//  along with "Adrian Kuhn's Utilities for Java". If not, see
//  <http://www.gnu.org/licenses/>.
//  

package ch.akuhn.util.tests;

import static org.junit.Assert.assertEquals;

import java.util.Iterator;

import org.junit.Test;

import ch.akuhn.util.Bag;

public class BagTest {

	@Test
	public void testBag() {
		Bag<String> bag = new Bag<String>();
		bag.add("A");
		assertEquals(1, bag.size());
		bag.add("B");
		assertEquals(2, bag.size());
		bag.add("B");
		assertEquals(3, bag.size());
		bag.add("A");
		assertEquals(4, bag.size());
		bag.add("A");
		assertEquals(5, bag.size());
		bag.add("C");
		assertEquals(6, bag.size());
		bag.add("D");
		assertEquals(7, bag.size());
		bag.add("A");
		assertEquals(8, bag.size());
		bag.add("A");
		assertEquals(9, bag.size());

		int count = 0;
		for (Iterator<String> it = bag.iterator(); it.hasNext();) {
			it.next();
			count++;
		}
		assertEquals(9, count);
	}

	@Test
	public void testBag2() {
		Bag<String> bag = new Bag<String>();
		bag.add("A");
		assertEquals(1, bag.size());
		bag.add("B");
		assertEquals(2, bag.size());
		bag.add("C");
		assertEquals(3, bag.size());
		bag.add("D");
		assertEquals(4, bag.size());

		int count = 0;
		for (Iterator<String> it = bag.iterator(); it.hasNext();) {
			it.next();
			count++;
		}
		assertEquals(4, count);
	}

	@Test
	public void testBag3() {
		Bag<String> bag = new Bag<String>();
		bag.add("A");
		assertEquals(1, bag.size());
		bag.add("A");
		assertEquals(2, bag.size());
		bag.add("A");
		assertEquals(3, bag.size());
		bag.add("A");
		assertEquals(4, bag.size());

		int count = 0;
		for (Iterator<String> it = bag.iterator(); it.hasNext();) {
			it.next();
			count++;
		}
		assertEquals(4, count);
	}
	
	@Test
	public void testAddOverflow() {
		Bag<String> bag = new Bag<String>();
		boolean changed =bag.add("A", Integer.MAX_VALUE - 2);
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
	public void testAddOccurrencesOverflow() {
		Bag<String> bag = new Bag<String>();
		boolean changed =bag.add("A", Integer.MAX_VALUE - 200);
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
		boolean changed =bag.add("A", Integer.MAX_VALUE - 150);
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
		boolean changed =bag.add("A", Integer.MAX_VALUE - 200);
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
	public void testEmptyBag() {
		Bag<String> bag = new Bag<String>();
		assertEquals(0, bag.size());

		int count = 0;
		for (Iterator<String> it = bag.iterator(); it.hasNext();) {
			it.next();
			count++;
		}
		assertEquals(0, count);
	}

}
