//  Copyright (c) 1998-2007 Adrian Kuhn <akuhn(a)iam.unibe.ch>
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

import static ch.akuhn.util.Magic.*;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.List;

import org.junit.Test;

import ch.akuhn.util.Magic;
import ch.akuhn.util.Predicate;

public class BlocksTest {

	@SuppressWarnings("unchecked")
	@Test
	public void testSelectInstanceOf() {
		Collection<Object> coll = (List<Object>) asList(1, "A", 2, "B", 3);
		Collection<Object> reply = select(coll, Magic.instanceOf(String.class));
		assertEquals(reply, asList("A", "B"));
		reply = select(coll, Magic.instanceOf(Number.class));
		assertEquals(reply, asList(1,2,3));
	}
	
	
	@Test
	public void testSelect() {
		Collection<Integer> coll = asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
		Collection<Integer> reply = select(coll, new Predicate<Integer>() {
			@Override
			public boolean is(Integer a) {
				return a % 2 == 1;
			}
		});
		assertEquals(reply, asList(1, 3, 5, 7, 9));
	}
	
	
	
	

}
