package ch.akuhn.util.tests;

import static ch.akuhn.util.Iterables.select;
import static ch.akuhn.util.Predicates.instanceOf;
import static java.util.Arrays.asList;
import static org.junit.Assert.*;

import java.util.Collection;
import java.util.List;

import org.junit.Test;

public class PredicatesTest {

	@SuppressWarnings("unchecked")
	@Test
	public void testSelectInstanceOf() {
		Collection<Object> coll = (List<Object>) asList(1, "A", 2, "B", 3);
		Collection<Object> reply = select(coll, instanceOf(String.class));
		assertEquals(reply, asList("A", "B"));
		reply = select(coll, instanceOf(Number.class));
		assertEquals(reply, asList(1,2,3));
	}
	
}
