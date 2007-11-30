package ch.akuhn.util.tests;

import static ch.akuhn.util.Iterables.select;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

import java.util.Collection;

import org.junit.Test;

import ch.akuhn.util.Predicate;

public class IterablesTest {

	@Test
	public void testSelect() {
		Collection<Integer> coll = asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
		Collection<Integer> reply = select(coll, new Predicate<Integer>() {
			@Override
			public boolean is(Integer a) {
				return a % 2 == 1;
			}
		});
		assertEquals(reply, asList(1,3,5,7,9));
	}
	
}
