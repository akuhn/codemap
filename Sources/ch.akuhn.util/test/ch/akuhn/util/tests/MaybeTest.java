package ch.akuhn.util.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.TreeSet;

import org.junit.Test;

import ch.akuhn.util.List;
import ch.akuhn.util.Maybe;


public class MaybeTest {

	@Test
	public void forNull() {
		Maybe<String> $ = Maybe.none(); 
		for (String s : $) fail(s);
	}

	@Test
	public void forSome() {
		Maybe<String> $ = Maybe.some("abc"); 
		int tally = 0;
		for (String s : $) {
			tally++;
			assertEquals("abc", s);
		}
		assertEquals( 1, tally );
	}
	
	@Test
	public void iterableNone() {
		Maybe<String> $ = Maybe.none(); 
		Iterator<String> it = $.iterator();
		assertEquals( false, it.hasNext());
	}

	@Test( expected = NoSuchElementException.class )
	public void getNone() {
		Maybe.none().get(); 
	}
	
	@Test
	public void getSome() {
		Maybe<String> $ = Maybe.some("abc"); 
		assertEquals( "abc", $.get() );
	}
	
	@Test
	public void iterableSome() {
		Maybe<String> $ = Maybe.some("abc"); 
		Iterator<String> it = $.iterator();
		assertEquals( true, it.hasNext());
		assertEquals( "abc", it.next());
		assertEquals( false, it.hasNext());
	}
	
	@Test( expected = UnsupportedOperationException.class )
	public void removeIterableSome() {
		Maybe<String> $ = Maybe.some("abc"); 
		Iterator<String> it = $.iterator();
		it.next();
		it.remove();
	}

	@Test( expected = UnsupportedOperationException.class )
	public void removeIterableNone() {
		Maybe<String> $ = Maybe.none(); 
		Iterator<String> it = $.iterator();
		it.remove();
	}

	@Test( expected = NoSuchElementException.class )
	public void noSuchElementIterableSome() {
		Maybe<String> $ = Maybe.some("abc"); 
		Iterator<String> it = $.iterator();
		it.next();
		it.next();
	}

	@Test( expected = NoSuchElementException.class )
	public void noSuchElementIterableNone() {
		Maybe<String> $ = Maybe.none(); 
		Iterator<String> it = $.iterator();
		it.next();
	}
	
	@Test
	public void testIsSome() {
		assertEquals( false, Maybe.none().isSome() );
		assertEquals( true, Maybe.some("abc").isSome() );
	}
	
	@Test
	public void testIsNone() {
		assertEquals( true, Maybe.none().isNone() );
		assertEquals( false, Maybe.some("abc").isNone() );
	}

	@Test
	public void maybeSomeNull() {
		assertEquals( true, Maybe.maybe((String) null).isNone() );
		assertEquals( false, Maybe.some((String) null).isNone() );
	}
	
	@Test
	public void maybeMaybe() {
		Maybe<String> $ = Maybe.some("abc");
		Maybe<String> maybe = Maybe.maybe($);
		assertSame( $, maybe );
	}
	
	@Test
	public void emptyFirst() {
		assertTrue( Maybe.first(new String[0]).isNone() );
		assertTrue( Maybe.first(new ArrayList<String>()).isNone() );
		assertTrue( Maybe.first(new TreeSet<String>()).isNone() );
	}
	
	@Test
	public void emptyLast() {
		assertTrue( Maybe.last(new String[0]).isNone() );
		assertTrue( Maybe.last(new ArrayList<String>()).isNone() );
		assertTrue( Maybe.last(new TreeSet<String>()).isNone() );
	}
	
	@Test
	public void firstLastList() {
		List<String> arr = List.of( "foo", "bar", "qux" );
		assertTrue( Maybe.first(arr).isSome() );
		assertEquals( "foo", Maybe.first(arr).get() );
		assertTrue( Maybe.last(arr).isSome() );
		assertEquals( "qux", Maybe.last(arr).get() );
	}

	@Test
	public void firstLastIterable() {
		Iterable<String> arr = List.of( "foo", "bar", "qux" );
		assertTrue( Maybe.first(arr).isSome() );
		assertEquals( "foo", Maybe.first(arr).get() );
		assertTrue( Maybe.last(arr).isSome() );
		assertEquals( "qux", Maybe.last(arr).get() );
	}
	
	@Test
	public void firstLastArray() {
		String[] arr = new String[] { "foo", "bar", "qux" };
		assertTrue( Maybe.first(arr).isSome() );
		assertEquals( "foo", Maybe.first(arr).get() );
		assertTrue( Maybe.last(arr).isSome() );
		assertEquals( "qux", Maybe.last(arr).get() );
	}
	
	
	
}
