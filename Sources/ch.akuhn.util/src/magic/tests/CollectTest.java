package magic.tests;

import static org.junit.Assert.*;

import java.util.Iterator;

import org.junit.Test;

import magic.blocks.Collect;
import magic.blocks.Function;
import magic.blocks.Methods;
import magic.util.List;

public class CollectTest {

	private static final Function<String,Integer> toString
			= Methods.newFunction("#toString");
	
	@Test
	public void collectIterable() {
		Iterable<Integer> nums = List.of(1, 2, 3, 4);
		Iterable<String> $ = Collect.all(nums, toString);
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
	
}
