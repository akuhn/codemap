package ch.akuhn.util.tests;

import static ch.akuhn.util.Strings.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class StringsTest {

	@Test
	public void testForEach() {
		String abc = "abcdef";
		int index = 0; 
		for (Character each : iter(abc)) {
			assertEquals(each, abc.charAt(index++));
		}
		assertEquals(index, abc.length());
	}
	
}
