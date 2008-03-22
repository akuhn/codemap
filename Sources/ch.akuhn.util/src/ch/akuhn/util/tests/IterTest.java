package ch.akuhn.util.tests;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Before;
import org.junit.Test;

import ch.akuhn.util.Iter;

public class IterTest {

	private Collection<String> empty;
	private Collection<String> single;
	private Collection<String> many;

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
	public void cycleEmpty() {
		Iterator<String> cycle = Iter.cycle(empty).iterator();
		assertTrue(!cycle.hasNext());
	}

	@Test
	public void cycleSingle() {
		Iterator<String> cycle = Iter.cycle(single).iterator();
		assertTrue(cycle.hasNext());
		assertEquals("boe", cycle.next());
		assertTrue(cycle.hasNext());
		assertEquals("boe", cycle.next());
		assertTrue(cycle.hasNext());
		assertEquals("boe", cycle.next());
	}
	
	@Test
	public void cycleMany() {
		Iterator<String> cycle = Iter.cycle(many).iterator();
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
	public void cycleSingleRemove() {
		Iterator<String> cycle = Iter.cycle(single).iterator();
		assertTrue(cycle.hasNext());
		assertEquals("boe", cycle.next());
		assertTrue(cycle.hasNext());
		assertEquals("boe", cycle.next());
		cycle.remove();
		assertTrue(!cycle.hasNext());
	}
	
	
}
