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

package ch.akuhn.util;

/** Static methods that operate on or return objects in general.
 * 
 *
 */
public abstract class Objects {

	public static void out(Object object) {
		System.out.println(object);
	}

	public static void out(Object[] objects) {
		System.out.print("[");
		for (int n = 0; n < objects.length; n++) {
			if (n != 0)
				System.out.print(", ");
			System.out.print(objects[n]);
		}
		System.out.println("]");
	}

	public boolean equals(Object a, Object b) {
		return a == null ? b == null : a.equals(b);
	}

}
