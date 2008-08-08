//  Copyright (c) 1998-2008 Adrian Kuhn <akuhn(a)students.unibe.ch>
//  
//  This file is part of ch.akuhn.util.
//  
//  ch.akuhn.util is free software: you can redistribute it and/or modify it
//  under the terms of the GNU Lesser General Public License as published by the
//  Free Software Foundation, either version 3 of the License, or (at your
//  option) any later version.
//  
//  ch.akuhn.util is distributed in the hope that it will be useful, but
//  WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
//  or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
//  License for more details.
//  
//  You should have received a copy of the GNU Lesser General Public License
//  along with ch.akuhn.util. If not, see <http://www.gnu.org/licenses/>.
//  

package magic.tests;

import static magic.Strings.letters;
import static magic.Strings.camelCase;
import static magic.Strings.chars;
import static magic.Strings.lines;
import static magic.Strings.words;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import java.util.NoSuchElementException;

import magic.Strings;

import org.junit.Test;

public class StringsTest {

    @Test(expected=UnsupportedOperationException.class)
    public void unsupportedRemoveOnChars() {
        chars("abcdef").iterator().remove();
    }
    
    @Test(expected=UnsupportedOperationException.class)
    public void unsupportedRemoveOnCamelCase() {
        camelCase("fooBar").iterator().remove();
    }

    @Test
    public void capticalize() {
        assertEquals("Foo", Strings.toUpperFirstChar("foo"));
        assertEquals("Foo", Strings.toUpperFirstChar("Foo"));
    }
    
    @Test
    public void capticalizeEmpty() {
        assertEquals("", Strings.toUpperFirstChar(""));
    }

    @Test
    public void letters() {
        Iterator<String> $ = Strings.letters("To be, or not to be.").iterator();
        assertEquals("To", $.next());
        assertEquals("be", $.next());
        assertEquals("or", $.next());
        assertEquals("not", $.next());
        assertEquals("to", $.next());
        assertEquals("be", $.next());
        assertEquals(false, $.hasNext());
    }
    
    @Test
    public void words() {
        Iterator<String> $ = Strings.words("To be, or not to be.").iterator();
        assertEquals("To", $.next());
        assertEquals("be,", $.next());
        assertEquals("or", $.next());
        assertEquals("not", $.next());
        assertEquals("to", $.next());
        assertEquals("be.", $.next());
        assertEquals(false, $.hasNext());
    }
    
    
    @Test(expected = NoSuchElementException.class)
	public void emptyCamelCase() {
		String foo = "";
		Iterator<CharSequence> it = camelCase(foo).iterator();
		assertTrue(!it.hasNext());
		it.next();
	}

	@Test(expected = NoSuchElementException.class)
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

	@Test
	public void testCamelCase() {
		String foo = "fooBarQUXDone";
		Iterator<CharSequence> it = camelCase(foo).iterator();
		assertEquals("foo", it.next());
		assertEquals("Bar", it.next());
		assertEquals("QUX", it.next());
		assertEquals("Done", it.next());
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
	public void testWords() {
		String abc = "aaa aaa  aaa   aaa";
		int index = 0;
		for (CharSequence line : Strings.words(abc)) {
			assertEquals("aaa", line.toString());
			index++;
		}
		assertEquals(4, index);
	}

	@Test
	public void testWords2() {
		String abc = " aaa aaa aaa ";
		int index = 0;
		for (CharSequence line : Strings.words(abc)) {
			assertEquals("aaa", line.toString());
			index++;
		}
		assertEquals(3, index);
	}

	@Test
	public void testWords3() {
		String abc = "    ";
		assertFalse(Strings.words(abc).iterator().hasNext());
	}

	@Test
	public void testWords4() {
		String abc = "";
		assertFalse(Strings.words(abc).iterator().hasNext());
	}

}
