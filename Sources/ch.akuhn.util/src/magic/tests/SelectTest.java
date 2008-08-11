package magic.tests;

import static magic.Extensions.newList;
import static magic.blocks.Methods.newPredicate;
import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import magic.blocks.Methods;
import magic.blocks.Select;

public class SelectTest {

	public static final boolean oddLength(String s) {
		return s.length() % 2 != 0;
	}
	
	@Test
	public void selectEmpty() {
		Iterable<String> list = newList();
		Iterable<String> $ = Select.from(list, newPredicate("#oddLength"));
		Iterator<String> it = $.iterator();
		assertEquals( false, it.hasNext() );
	}

	@Test
	public void selectSome() {
		Iterable<String> list = newList("aa", "abc", "cc", "cde", "ee");
		Iterable<String> $ = Select.from(list, newPredicate("#oddLength"));
		Iterator<String> it = $.iterator();
		assertEquals( true, it.hasNext() );
		assertEquals( "abc", it.next() );
		assertEquals( true, it.hasNext() );
		assertEquals( "cde", it.next() );
		assertEquals( false, it.hasNext() );
	}

	@Test
	public void selectSomeMore() {
		Iterable<String> list = newList("abc", "cc", "cde", "ee", "efg");
		Iterable<String> $ = Select.from(list, newPredicate("#oddLength"));
		Iterator<String> it = $.iterator();
		assertEquals( true, it.hasNext() );
		assertEquals( "abc", it.next() );
		assertEquals( true, it.hasNext() );
		assertEquals( "cde", it.next() );
		assertEquals( true, it.hasNext() );
		assertEquals( "efg", it.next() );
		assertEquals( false, it.hasNext() );
	}
	
	@Test
	public void selectNone() {
		Iterable<String> list = newList("aa", "bb", "cc", "dd", "ee");
		Iterable<String> $ = Select.from(list, newPredicate("#oddLength"));
		Iterator<String> it = $.iterator();
		assertEquals( false, it.hasNext() );
	}
	
	@Test
	public void selectList() {
		List<String> list = newList("aa", "abc", "cc", "cde", "ee");
		List<String> $ = Select.from(list, newPredicate("#oddLength"));
		assertEquals( 2, $.size() );
		assertEquals( "abc", $.get(0) );
		assertEquals( "cde", $.get(1) );
	}
	
}
