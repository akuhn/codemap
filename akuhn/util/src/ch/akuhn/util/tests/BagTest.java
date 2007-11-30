package ch.akuhn.util.tests;

import static org.junit.Assert.*;

import java.util.Iterator;

import org.junit.Test;

import ch.akuhn.util.Bag;

public class BagTest  {

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
