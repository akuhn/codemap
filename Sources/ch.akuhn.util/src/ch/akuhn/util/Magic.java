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

package ch.akuhn.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

/** Static methods that operate on objects, iterables and blocks.
 *  Blocks are <tt>Predicate</tt>, <tt>Function</tt>, and <tt>Procedure</tt>.
 * 
 *
 */
public abstract class Magic {

	public static <E> List<E> list(E... elements) {
		return Arrays.asList(elements);
	}
	
	public static <E> Set<E> set(E... elements) {
		return new HashSet<E>(Arrays.asList(elements));
	}

	public static <E> E last(List<E> list) {
		return list.get(list.size() - 1);
	}

	public static void out(Object object, Object... objects) {
		System.out.println(object);
		for (Object o : objects) {
			System.out.print(", ");
			System.out.print(o);
		}
	}
	
	
	public static void out(Object object) {
		System.out.println(object);
	}
	
	public static <E> void  out(Iterable<E> iterable) {
		System.out.print("[");
		Iterator<E> it = iterable.iterator(); 
		while (it.hasNext()) {
			System.out.print(it.next());
			if (it.hasNext()) 
				System.out.print(", ");
		}
		System.out.println("]");
	}

	public static void out(Object[] objects) {
		if (objects == null) {
			out((Object) null);
			return;
		}
		System.out.print("[");
		for (int n = 0; n < objects.length; n++) {
			if (n != 0)
				System.out.print(", ");
			System.out.print(objects[n]);
		}
		System.out.println("]");
	}

	public static boolean isEqual(Object a, Object b) {
		return a == null ? b == null : a.equals(b);
	}

}
