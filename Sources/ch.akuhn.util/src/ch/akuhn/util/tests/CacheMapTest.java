//  Copyright (c) 1998-2008 Adrian Kuhn <akuhn(a)iam.unibe.ch>
//  
//  This file is part of "Adrian Kuhn's Utilities for Java".
//  
//  "Adrian Kuhn's Utilities for Java" is free software: you can redistribute
//  it and/or modify it under the terms of the GNU Lesser General Public License
//  as published by the Free Software Foundation, either version 3 of the
//  License, or (at your option) any later version.
//  
//  "Adrian Kuhn's Utilities for Java" is distributed in the hope that it will
//  be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser
//  General Public License for more details.
//  
//  You should have received a copy of the GNU Lesser General Public License
//  along with "Adrian Kuhn's Utilities for Java". If not, see
//  <http://www.gnu.org/licenses/>.
//  

package ch.akuhn.util.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;

import ch.akuhn.util.CacheMap;

public class CacheMapTest {

	@Test
	public void example() {
		Map<Integer, String> map = new CacheMap<Integer, String>() {
			public String initialize(Integer key) {
				return key.toString();
			}
		};
		assertEquals(map.get(1), "1");
		assertEquals(map.get(2), "2");
		assertEquals(map.get(3), "3");
		assertEquals(map.get(2), "2");
	}
	
	@Test
	public void instancesSimple() {
		Map<String,Object> map = CacheMap.instances(Object.class);
		Object a = map.get("a");
		Object b = map.get("b");
		Object a2 = map.get("a");
		Object c = map.get("c");
		assertSame(a, a2);
		assertNotSame(a, b);
		assertNotSame(b, c);
		assertNotSame(c, a);
	}
	
	@Test
	public void instancesArgument() {
		Map<String,Wrapper> map = CacheMap.instances(Wrapper.class);
		Wrapper a = map.get("a");
		Wrapper b = map.get("b");
		Wrapper a2 = map.get("a");
		Wrapper c = map.get("c");
		assertSame(a, a2);
		assertNotSame(a, b);
		assertNotSame(b, c);
		assertNotSame(c, a);
		assertEquals("a", a.delegate);
		assertEquals("b", b.delegate);
		assertEquals("c", c.delegate);
	}
	
	public static class Wrapper {
		public Object delegate;
		public Wrapper(Object delegate) {
			this.delegate = delegate;
		}
	}
	

}
