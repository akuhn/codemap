package ch.akuhn.hapax.index;

import static ch.akuhn.hapax.index.AssociativeList.NONE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;

import ch.unibe.jexample.Given;
import ch.unibe.jexample.JExample;

@RunWith(JExample.class)
public class AssociativeListTest {

	@Test
	public AssociativeList<String> empty() {
		AssociativeList<String> list = new AssociativeList<String>();
		assertEquals(0, list.size());
		return list;
	}
	
	@Test @Given("empty")
	public AssociativeList<String> withElements(AssociativeList<String> list) {
		list.add("A");
		list.add("B");
		list.add("C");
		list.add("D");
		list.add("E");
		assertEquals(5, list.size());
		assertEquals(0, list.get("A"));
		assertEquals(1, list.get("B"));
		assertEquals(2, list.get("C"));
		assertEquals(3, list.get("D"));
		assertEquals(4, list.get("E"));
		assertEquals("A", list.get(0));
		assertEquals("B", list.get(1));
		assertEquals("C", list.get(2));
		assertEquals("D", list.get(3));
		assertEquals("E", list.get(4));
		return list;
	}
		
	@Test @Given("withElements")
	public void testRemoveElement(AssociativeList<String> list) {
		int index = list.remove("C");
		assertEquals(2, index);
		assertEquals(4, list.size());
		assertEquals(0, list.get("A"));
		assertEquals(1, list.get("B"));
		assertEquals(NONE, list.get("C"));
		assertEquals(2, list.get("D"));
		assertEquals(3, list.get("E"));
		assertEquals("A", list.get(0));
		assertEquals("B", list.get(1));
		assertEquals("D", list.get(2));
		assertEquals("E", list.get(3));
		assertEquals(null, list.get(4));
	}

	@Test @Given("withElements")
	public void testRemoveOnClone(AssociativeList<String> list) {
		list.clone().remove("C");
		assertEquals(5, list.size());
		assertEquals(2, list.get("C"));
		assertEquals("C", list.get(2));
	}

	@Test @Given("withElements")
	public void testContains(AssociativeList<String> list) {
		assertFalse(list.contains(null));
		assertTrue(list.contains("A"));
		assertTrue(list.contains("B"));
		assertTrue(list.contains("C"));
		assertTrue(list.contains("D"));
		assertTrue(list.contains("E"));
		assertFalse(list.contains("F"));
	}

	@Test(expected=IllegalArgumentException.class) @Given("empty")
	public void cannotAddNull(AssociativeList<String> list) {
		list.add(null);
	}
	
}
