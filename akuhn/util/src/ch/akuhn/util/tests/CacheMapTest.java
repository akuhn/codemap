package ch.akuhn.util.tests;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;

import ch.akuhn.util.CacheMap;

public class CacheMapTest {

	@Test
	public void example() {
		Map<Integer,String> map = new CacheMap<Integer,String>() {
			public String initialize(Integer key) {
				return key.toString();
			}
		};
		assertEquals(map.get(1),"1");
		assertEquals(map.get(2),"2");
		assertEquals(map.get(3),"3");
		assertEquals(map.get(2),"2");
	}
	
	
	
}
