package ch.akuhn.foreach;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.Iterator;

import org.junit.Test;

import ch.akuhn.foreach.Collect;
import ch.akuhn.foreach.Each;

public class CollectTest {

	private Collect<String> query;
	private String[] array;
	
	@Test
	public void testEmptyArray() {
		query = Collect.from();
		for (Each<String> each: query) each.yield = each.value.toUpperCase();
		array = query.getResultArray();
		assertNotNull(array);
		assertEquals(0, array.length);
	}

	@Test
	public void testArrayWithOneElement() {
		query = Collect.from("foo");
		for (Each<String> each: query) each.yield = each.value.toUpperCase();
		array = query.getResultArray();
		assertNotNull(array);
		assertEquals(1, array.length);
		assertEquals("FOO", array[0]);
	}
	
	@Test
	public void testArrayWithThreeElement() {
		query = Collect.from("foo", "bar", "qux");
		for (Each<String> each: query) each.yield = each.value.toUpperCase();
		array = query.getResultArray();
		assertNotNull(array);
		assertEquals(3, array.length);
		assertEquals("FOO", array[0]);
		assertEquals("BAR", array[1]);
		assertEquals("QUX", array[2]);
	}

	@Test
	public void testArrayIterator() {
		query = Collect.from("foo", "bar", "qux");
		Iterator<Each<String>> iter = query.iterator();
		iter.hasNext();
		iter.next().yield = "FOO";
		iter.hasNext();
		iter.next().yield = "BAR";
		iter.hasNext();
		iter.next().yield = "QUX";
		assertFalse(iter.hasNext());
		array = query.getResultArray();
		assertNotNull(array);
		assertEquals(3, array.length);
		assertEquals("FOO", array[0]);
		assertEquals("BAR", array[1]);
		assertEquals("QUX", array[2]);
	}
	
	@Test
	public void testArrayIterator2() {
		query = Collect.from("foo", "bar", "qux");
		Iterator<Each<String>> iter = query.iterator();
		iter.next().yield = "FOO";
		iter.next().yield = "BAR";
		iter.next().yield = "QUX";
		array = query.getResultArray();
		assertNotNull(array);
		assertEquals(3, array.length);
		assertEquals("FOO", array[0]);
		assertEquals("BAR", array[1]);
		assertEquals("QUX", array[2]);
	}

	@Test
	public void testEmptyCollection() {
		query = Collect.from(Arrays.<String>asList());
		for (Each<String> each: query) each.yield = each.value.toUpperCase();
		array = query.getResultArray(String.class);
		assertNotNull(array);
		assertEquals(0, array.length);
	}

	@Test
	public void testCollectionWithOneElement() {
		query = Collect.from(Arrays.<String>asList("foo"));
		for (Each<String> each: query) each.yield = each.value.toUpperCase();
		array = query.getResultArray();
		assertNotNull(array);
		assertEquals(1, array.length);
		assertEquals("FOO", array[0]);
	}
	
	@Test
	public void testCollectionWithThreeElement() {
		query = Collect.from(Arrays.<String>asList("foo", "bar", "qux"));
		for (Each<String> each: query) each.yield = each.value.toUpperCase();
		array = query.getResultArray();
		assertNotNull(array);
		assertEquals(3, array.length);
		assertEquals("FOO", array[0]);
		assertEquals("BAR", array[1]);
		assertEquals("QUX", array[2]);
	}

	@Test
	public void testCollectionIterator() {
		query = Collect.from(Arrays.<String>asList("foo", "bar", "qux"));
		Iterator<Each<String>> iter = query.iterator();
		iter.hasNext();
		iter.next().yield = "FOO";
		iter.hasNext();
		iter.next().yield = "BAR";
		iter.hasNext();
		iter.next().yield = "QUX";
		assertFalse(iter.hasNext());
		array = query.getResultArray();
		assertNotNull(array);
		assertEquals(3, array.length);
		assertEquals("FOO", array[0]);
		assertEquals("BAR", array[1]);
		assertEquals("QUX", array[2]);
	}
	
	@Test
	public void testCollectionIterator2() {
		query = Collect.from(Arrays.<String>asList("foo", "bar", "qux"));
		Iterator<Each<String>> iter = query.iterator();
		iter.next().yield = "FOO";
		iter.next().yield = "BAR";
		iter.next().yield = "QUX";
		array = query.getResultArray();
		assertNotNull(array);
		assertEquals(3, array.length);
		assertEquals("FOO", array[0]);
		assertEquals("BAR", array[1]);
		assertEquals("QUX", array[2]);
	}
	
	
}
