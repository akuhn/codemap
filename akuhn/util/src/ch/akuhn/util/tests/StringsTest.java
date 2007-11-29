package ch.akuhn.util.tests;

import static ch.akuhn.util.Strings.*;
import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.junit.Test;

public class StringsTest {

	@Test
	public void testWords() {
		String abc = "aaa aaa  aaa   aaa";
		int index = 0; 
		for (CharSequence line : words(abc)) {
			assertEquals("aaa", line.toString());
			index++;
		}
		assertEquals(4, index);
	}
	
	@Test
	public void testWords2() {
		String abc = " aaa aaa aaa ";
		int index = 0; 
		for (CharSequence line : words(abc)) {
			assertEquals("aaa", line.toString());
			index++;
		}
		assertEquals(3, index);
	}	
	
	@Test
	public void testWords3() {
		String abc = "    ";
		assertFalse(words(abc).iterator().hasNext());
	}	
	
	@Test
	public void testWords4() {
		String abc = "";
		assertFalse(words(abc).iterator().hasNext());
	}	

	@Test
	public void testLines() {
		String abc = "aaa\naaa\naaa\naaa";
		int index = 0; 
		for (String line : lines(abc)) {
			assertEquals("aaa", line);
			index++;
		}
		assertEquals(4, index);
	}
	
	@Test
	public void testLines2() {
		String abc = "aaa\naaa\naaa\n";
		int index = 0; 
		for (String line : lines(abc)) {
			assertEquals("aaa", line);
			index++;
		}
		assertEquals(3, index);
	}
	
	@Test
	public void testLines3() {
		String abc = "\n\n\n";
		int index = 0; 
		for (String line : lines(abc)) {
			assertEquals("", line);
			index++;
		}
		assertEquals(3, index);
	}
	@Test
	public void testLines4() {
		String abc = "";
		assertFalse(lines(abc).iterator().hasNext());
	}

	
	
	
	
	@Test
	public void testForEach() {
		String abc = "abcdef";
		int index = 0; 
		for (Character each : chars(abc)) {
			assertEquals(abc.charAt(index++), each);
		}
		assertEquals(index, abc.length());
	}
	
	@Test
	public void testCamelCase() {
		String foo = "fooBarQUXDone";
		Iterator<CharSequence> it = camelCase(foo).iterator();
		assertEquals("foo", it.next());
		assertEquals("Bar", it.next());
		assertEquals("QUX", it.next());
		assertEquals("Done", it.next());
	}
	
	@Test(expected=NoSuchElementException.class)
	public void emptyCamelCase() {
		String foo = "";
		Iterator<CharSequence> it = camelCase(foo).iterator();
		assertTrue(!it.hasNext());
		it.next();
	}

	@Test(expected=NoSuchElementException.class)
	public void simpleCamelCase1() {
		String foo = "aaa";
		Iterator<CharSequence> it = camelCase(foo).iterator();
		assertEquals("aaa", it.next());
		assertTrue(!it.hasNext());
		it.next();
	}

	@Test
	public void simpleCamelCase2() {
		String foo = "AAA";
		Iterator<CharSequence> it = camelCase(foo).iterator();
		assertEquals("AAA", it.next());
		assertTrue(!it.hasNext());
	}

	@Test
	public void simpleCamelCase3() {
		String foo = "AA";
		Iterator<CharSequence> it = camelCase(foo).iterator();
		assertEquals("AA", it.next());
		assertTrue(!it.hasNext());
	}	

	@Test
	public void simpleCamelCase4() {
		String foo = "AAa";
		Iterator<CharSequence> it = camelCase(foo).iterator();
		assertEquals("A", it.next());
		assertEquals("Aa", it.next());
		assertTrue(!it.hasNext());
	}	
	
	
}
