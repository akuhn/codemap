package ch.akuhn.foreach;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.Iterator;

import org.junit.Test;

public class SelectTest {

	private Select<String> query;
	private String[] array;
	
	@Test
	@SuppressWarnings("unused")
	public void testEmptyArray() {
		query = Select.from();
		for (EachB<String> each: query) throw null;
		array = query.getResultArray();
		assertNotNull(array);
		assertEquals(0, array.length);
	}
	
	@Test
	public void testSelectOneOfOneElement() {
		query = Select.from("foo");
		for (EachB<String> each: query) each.yield = true;
		array = query.getResultArray();
		assertNotNull(array);
		assertEquals(1, array.length);
		assertEquals("foo", array[0]);
	}

	@Test
	public void testSelectNoneOfOneElement() {
		query = Select.from("foo");
		for (EachB<String> each: query) each.yield = false;
		array = query.getResultArray();
		assertNotNull(array);
		assertEquals(0, array.length);
	}
	
	@Test
	public void testArrayWithThreeElement() {
		query = Select.from("foo", "", "qux");
		for (EachB<String> each: query) each.yield = !each.value.isEmpty();
		array = query.getResultArray();
		assertNotNull(array);
		assertEquals(2, array.length);
		assertEquals("foo", array[0]);
		assertEquals("qux", array[1]);
	}
	
	@Test
	public void testArrayIterator() {
		query = Select.from("foo", "bar", "qux");
		Iterator<EachB<String>> iter = query.iterator();
		iter.hasNext();
		iter.next().yield = true;
		iter.hasNext();
		iter.next().yield = false;
		iter.hasNext();
		iter.next().yield = true;
		assertFalse(iter.hasNext());
		array = query.getResultArray();
		assertNotNull(array);
		assertEquals(2, array.length);
		assertEquals("foo", array[0]);
		assertEquals("qux", array[1]);
	}
	
	@Test
	public void testArrayIterator2() {
		query = Select.from("foo", "bar", "qux");
		Iterator<EachB<String>> iter = query.iterator();
		iter.next().yield = true;
		iter.next().yield = false;
		iter.next().yield = true;
		array = query.getResultArray();
		assertNotNull(array);
		assertEquals(2, array.length);
		assertEquals("foo", array[0]);
		assertEquals("qux", array[1]);
	}

	@Test
	public void testFailure() {
		query = Select.from("foo", "bar", "qux");
		Error error = new Error();
		try {
			for(EachB<String> each: query) {
				if (each.index == 1) throw error;
				each.yield = true;
			}
		}
		catch (Error caught) {
			assertEquals(error, caught);
		}
		array = query.getResultArray();
		assertNotNull(array);
		assertEquals(1, array.length);
		assertEquals("foo", array[0]);
	}
	
	
}
