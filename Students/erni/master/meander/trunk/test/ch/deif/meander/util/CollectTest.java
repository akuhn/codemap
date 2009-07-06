package ch.deif.meander.util;

import org.junit.Test;

public class CollectTest {

	@Test
	public void test() {
		Collect<Integer> query = Collect.fromArray(1, 2, 3, 4, 5);
		for (Collect<Integer> each: query) {
			each.value *= each.value;
		}
		System.out.println(query.asList());
	}
	
}
