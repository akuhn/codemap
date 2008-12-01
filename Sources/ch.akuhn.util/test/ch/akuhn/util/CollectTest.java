package ch.akuhn.util;

import static org.junit.Assert.*;

import java.util.Iterator;

import org.junit.Test;

import ch.akuhn.util.Collect;
import ch.akuhn.util.List;
import ch.akuhn.util.Methods;
import ch.akuhn.util.Strings;
import ch.akuhn.util.blocks.Function;


public class CollectTest {

	@Test
	public void collectIterable() {
		Iterable<Integer> nums = List.of(1, 2, 3, 4);
		Iterable<String> $ = Collect.each(nums, "#toString()");
		Iterator<String> it = $.iterator();
		assertEquals( true, it.hasNext() );
		assertEquals( "1", it.next() );
		assertEquals( true, it.hasNext() );
		assertEquals( "2", it.next() );
		assertEquals( true, it.hasNext() );
		assertEquals( "3", it.next() );
		assertEquals( true, it.hasNext() );
		assertEquals( "4", it.next() );
		assertEquals( false, it.hasNext() );
	}

	@Test
	public void collectIterable2() {
		Iterable<String> nums = Strings.words("A quick fox jumped");
		Iterable<Integer> $ = Collect.each(nums, "#length()");
		Iterator<Integer> it = $.iterator();
		assertEquals( true, it.hasNext() );
		assertEquals( 1, it.next() );
		assertEquals( true, it.hasNext() );
		assertEquals( 5, it.next() );
		assertEquals( true, it.hasNext() );
		assertEquals( 3, it.next() );
		assertEquals( true, it.hasNext() );
		assertEquals( 6, it.next() );
		assertEquals( false, it.hasNext() );
	}
	
}
