package ch.akuhn.util.tests;

import static org.junit.Assert.*;


import org.junit.Test;

import ch.akuhn.util.Separator;

public class SeparatorTest {

	@Test
	public void testSequence() {
		Separator s = new Separator("abc");
		assertEquals("", s.toString());
		assertEquals("abc", s.toString());
		assertEquals("abc", s.toString());
		assertEquals("abc", s.toString());
		assertEquals("abc", s.toString());
	}

	@Test
	public void defaultValue() {
		Separator s = new Separator();
		assertEquals("", s.toString());
		assertEquals(", ", s.toString());
		assertEquals(", ", s.toString());
		assertEquals(", ", s.toString());
	}

	@Test
	public void testReset() {
		Separator s = new Separator("abc");
		assertEquals("", s.toString());
		assertEquals("abc", s.toString());
		assertEquals("abc", s.toString());
		s.reset();
		assertEquals("", s.toString());
		assertEquals("abc", s.toString());
		assertEquals("abc", s.toString());
	}

}
