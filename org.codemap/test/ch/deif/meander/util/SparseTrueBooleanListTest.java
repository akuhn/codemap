package ch.deif.meander.util;

import static org.junit.Assert.assertEquals;

import org.codemap.util.SparseTrueBooleanList;
import org.junit.Test;

public class SparseTrueBooleanListTest {

	@Test
	public void testFalseFalseFalse() {
		SparseTrueBooleanList list = new SparseTrueBooleanList(false, false, false);
		assertEquals(3, list.size());
		assertEquals(false, list.get(0));
		assertEquals(false, list.get(1));
		assertEquals(false, list.get(2));
	}
	
	@Test
	public void testTrueFalseTrue() {
		SparseTrueBooleanList list = new SparseTrueBooleanList(true, false, true);
		assertEquals(3, list.size());
		assertEquals(true, list.get(0));
		assertEquals(false, list.get(1));
		assertEquals(true, list.get(2));
	}

	@Test
	public void testFalseTrueFalse() {
		SparseTrueBooleanList list = new SparseTrueBooleanList(false, true, false);
		assertEquals(3, list.size());
		assertEquals(false, list.get(0));
		assertEquals(true, list.get(1));
		assertEquals(false, list.get(2));
	}

}
