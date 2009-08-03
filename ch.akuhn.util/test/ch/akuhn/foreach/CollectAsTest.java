package ch.akuhn.foreach;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.Iterator;

import org.junit.Test;

import ch.akuhn.foreach.Collect;
import ch.akuhn.foreach.Each;

public class CollectAsTest {

	private CollectAs<String,Integer> query;
	private Integer[] array;
	
	@Test
	public void testEmptyArray() {
		query = CollectAs.from(Integer.class);
		for (EachAs<String,Integer> each: query) each.yield = each.value.length();
		array = query.getResultArray();
		assertNotNull(array);
		assertEquals(0, array.length);
	}

	@Test
	public void testArrayWithOneElement() {
		query = CollectAs.from(Integer.class, "the");
		for (EachAs<String,Integer> each: query) each.yield = each.value.length();
		array = query.getResultArray();
		assertNotNull(array);
		assertEquals(1, array.length);
		assertEquals(3, (int) array[0]);
	}
	
	@Test
	public void testArrayWithThreeElement() {
		query = CollectAs.from(Integer.class, "the", "quick", "lazy");
		for (EachAs<String,Integer> each: query) each.yield = each.value.length();
		array = query.getResultArray();
		assertNotNull(array);
		assertEquals(3, array.length);
		assertEquals(3, (int) array[0]);
		assertEquals(5, (int) array[1]);
		assertEquals(4, (int) array[2]);
	}

	@Test
	public void testArrayIterator() {
		query = CollectAs.from(Integer.class, "the", "quick", "lazy");
		Iterator<EachAs<String,Integer>> iter = query.iterator();
		iter.hasNext();
		iter.next().yield = 3;
		iter.hasNext();
		iter.next().yield = 5;
		iter.hasNext();
		iter.next().yield = 4;
		assertFalse(iter.hasNext());
		array = query.getResultArray();
		assertNotNull(array);
		assertEquals(3, array.length);
		assertEquals(3, (int) array[0]);
		assertEquals(5, (int) array[1]);
		assertEquals(4, (int) array[2]);
	}
	
	@Test
	public void testArrayIterator2() {
		query = CollectAs.from(Integer.class, "the", "quick", "lazy");
		Iterator<EachAs<String,Integer>> iter = query.iterator();
		iter.next().yield = 3;
		iter.next().yield = 5;
		iter.next().yield = 4;
		array = query.getResultArray();
		assertNotNull(array);
		assertEquals(3, array.length);
		assertEquals(3, (int) array[0]);
		assertEquals(5, (int) array[1]);
		assertEquals(4, (int) array[2]);
	}

	@Test
	public void testEmptyCollection() {
		query = CollectAs.from(Integer.class, Arrays.<String>asList());
		for (EachAs<String,Integer> each: query) each.yield = each.value.length();
		array = query.getResultArray();
		assertNotNull(array);
		assertEquals(0, array.length);
	}

	@Test
	public void testCollectionWithOneElement() {
		query = CollectAs.from(Integer.class, Arrays.<String>asList("foo"));
		for (EachAs<String,Integer> each: query) each.yield = each.value.length();
		array = query.getResultArray();
		assertNotNull(array);
		assertEquals(1, array.length);
		assertEquals(3, (int) array[0]);
	}
	
	@Test
	public void testCollectionWithThreeElement() {
		query = CollectAs.from(Integer.class, Arrays.<String>asList("the", "quick", "lazy"));
		for (EachAs<String,Integer> each: query) each.yield = each.value.length();
		array = query.getResultArray();
		assertNotNull(array);
		assertEquals(3, array.length);
		assertEquals(3, (int) array[0]);
		assertEquals(5, (int) array[1]);
		assertEquals(4, (int) array[2]);
	}

	@Test
	public void testCollectionIterator() {
		query = CollectAs.from(Integer.class, Arrays.<String>asList("foo", "bar", "qux"));
		Iterator<EachAs<String,Integer>> iter = query.iterator();
		iter.hasNext();
		iter.next().yield = 3;
		iter.hasNext();
		iter.next().yield = 5;
		iter.hasNext();
		iter.next().yield = 4;
		assertFalse(iter.hasNext());
		array = query.getResultArray();
		assertNotNull(array);
		assertEquals(3, array.length);
		assertEquals(3, (int) array[0]);
		assertEquals(5, (int) array[1]);
		assertEquals(4, (int) array[2]);
	}
	
	@Test
	public void testCollectionIterator2() {
		query = CollectAs.from(Integer.class, Arrays.<String>asList("foo", "bar", "qux"));
		Iterator<EachAs<String,Integer>> iter = query.iterator();
		iter.next().yield = 3;
		iter.next().yield = 5;
		iter.next().yield = 4;
		array = query.getResultArray();
		assertNotNull(array);
		assertEquals(3, array.length);
		assertEquals(3, (int) array[0]);
		assertEquals(5, (int) array[1]);
		assertEquals(4, (int) array[2]);
	}
	
	
}
