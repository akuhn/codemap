package ch.akuhn.foreach.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import ch.akuhn.foreach.Collect;
import ch.akuhn.foreach.Each;

public class CollectTest {

	private Collect<String> query;
	private String[] array;
	private List<String> result;
	
	@Test
	public void testEmptyArray() {
		query = Collect.<String>fromArray();
		for (Each<String> each: query) each.yield = each.value.toUpperCase();
		array = query.resultArray();
		assertNotNull(array);
		assertEquals(0, array.length);
	}

	@Test
	public void testArrayWithOneElement() {
		query = Collect.<String>fromArray("foo");
		for (Each<String> each: query) each.yield = each.value.toUpperCase();
		array = query.resultArray();
		assertNotNull(array);
		assertEquals(1, array.length);
		assertEquals("FOO", array[0]);
	}
	
	@Test
	public void testArrayWithThreeElement() {
		query = Collect.<String>fromArray("foo", "bar", "qux");
		for (Each<String> each: query) each.yield = each.value.toUpperCase();
		array = query.resultArray();
		assertNotNull(array);
		assertEquals(3, array.length);
		assertEquals("FOO", array[0]);
		assertEquals("BAR", array[1]);
		assertEquals("QUX", array[2]);
	}

	@Test
	public void testArrayIterator() {
		query = Collect.<String>fromArray("foo", "bar", "qux");
		Iterator<Each<String>> iter = query.iterator();
		iter.hasNext();
		iter.next().yield = "FOO";
		iter.hasNext();
		iter.next().yield = "BAR";
		iter.hasNext();
		iter.next().yield = "QUX";
		assertFalse(iter.hasNext());
		array = query.resultArray();
		assertNotNull(array);
		assertEquals(3, array.length);
		assertEquals("FOO", array[0]);
		assertEquals("BAR", array[1]);
		assertEquals("QUX", array[2]);
	}
	
	@Test
	public void testArrayIterator2() {
		query = Collect.<String>fromArray("foo", "bar", "qux");
		Iterator<Each<String>> iter = query.iterator();
		iter.next().yield = "FOO";
		iter.next().yield = "BAR";
		iter.next().yield = "QUX";
		array = query.resultArray();
		assertNotNull(array);
		assertEquals(3, array.length);
		assertEquals("FOO", array[0]);
		assertEquals("BAR", array[1]);
		assertEquals("QUX", array[2]);
	}

	@Test
	public void testEmptyCollection() {
		query = Collect.<String>fromCollection(Arrays.<String>asList());
		for (Each<String> each: query) each.yield = each.value.toUpperCase();
		array = query.resultArray(String.class);
		assertNotNull(array);
		assertEquals(0, array.length);
	}

	@Test
	public void testCollectionWithOneElement() {
		query = Collect.<String>fromCollection(Arrays.<String>asList("foo"));
		for (Each<String> each: query) each.yield = each.value.toUpperCase();
		array = query.resultArray();
		assertNotNull(array);
		assertEquals(1, array.length);
		assertEquals("FOO", array[0]);
	}
	
	@Test
	public void testCollectionWithThreeElement() {
		query = Collect.<String>fromCollection(Arrays.<String>asList("foo", "bar", "qux"));
		for (Each<String> each: query) each.yield = each.value.toUpperCase();
		array = query.resultArray();
		assertNotNull(array);
		assertEquals(3, array.length);
		assertEquals("FOO", array[0]);
		assertEquals("BAR", array[1]);
		assertEquals("QUX", array[2]);
	}

	@Test
	public void testCollectionIterator() {
		query = Collect.<String>fromCollection(Arrays.<String>asList("foo", "bar", "qux"));
		Iterator<Each<String>> iter = query.iterator();
		iter.hasNext();
		iter.next().yield = "FOO";
		iter.hasNext();
		iter.next().yield = "BAR";
		iter.hasNext();
		iter.next().yield = "QUX";
		assertFalse(iter.hasNext());
		array = query.resultArray();
		assertNotNull(array);
		assertEquals(3, array.length);
		assertEquals("FOO", array[0]);
		assertEquals("BAR", array[1]);
		assertEquals("QUX", array[2]);
	}
	
	@Test
	public void testCollectionIterator2() {
		query = Collect.<String>fromCollection(Arrays.<String>asList("foo", "bar", "qux"));
		Iterator<Each<String>> iter = query.iterator();
		iter.next().yield = "FOO";
		iter.next().yield = "BAR";
		iter.next().yield = "QUX";
		array = query.resultArray();
		assertNotNull(array);
		assertEquals(3, array.length);
		assertEquals("FOO", array[0]);
		assertEquals("BAR", array[1]);
		assertEquals("QUX", array[2]);
	}
	
	
}
