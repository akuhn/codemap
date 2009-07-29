package ch.akuhn.util;

import org.junit.Test;

public class FilesTest {

	@Test
	public void testFilter() {
		assert Files.match("foo", "foo");
		assert Files.match("???", "foo");
		assert Files.match("fo?", "foo");
		assert ! Files.match("??", "foo");
		assert Files.match("*", "foo");
	}

	@Test
	public void testQuestionMarkFilter() {
		assert ! Files.match("???", "foooo");
		assert ! Files.match("???", "fooo");
		assert Files.match("???", "foo");
		assert ! Files.match("???", "fo");
		assert ! Files.match("???", "f");
		assert ! Files.match("???", "");
	}

	@Test
	public void testQuestionMarkFilter2() {
		assert ! Files.match("??o", "foooo");
		assert ! Files.match("??o", "fooo");
		assert Files.match("??o", "foo");
		assert ! Files.match("??o", "fo");
		assert ! Files.match("??o", "f");
		assert ! Files.match("??o", "");
	}
	
	
	@Test
	public void testFilter3() {
		assert ! Files.match("foo", "foooo");
		assert ! Files.match("foo", "fooo");
		assert Files.match("foo", "foo");
		assert ! Files.match("foo", "fo");
		assert ! Files.match("foo", "f");
		assert ! Files.match("foo", "");
	}
	
	
	@Test
	public void testFilter2() {
		assert Files.match("*.java", ".java");
		assert Files.match("*.java", "Foo.java");
		assert ! Files.match("*.java", "Foo.java.");
		assert ! Files.match("*.java", "Foo.java.ja");
		assert Files.match("*.java", "Foo.java.java");
		assert Files.match("**.java", "Foo.java");
		assert Files.match("*?.java", "Foo.java");
		assert Files.match("*?.java", "Foo.java");
		assert Files.match("*??.java", "Foo.java");
		assert Files.match("*???.java", "Foo.java");
		assert Files.match("*???.java", "Foo.java");
		assert ! Files.match("*????.java", "Foo.java");
	}
	
	
}
