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

import java.util.List;

/** Static methods that operate on or return lists.
 * 
 *
 */
public class Lists {

	public static <A> int atFirst(List<A> list, Predicate<A> block) {
		for (int n = 0; n < list.size(); n++) {
			if (block.is(list.get(n)))
				return n;
		}
		return -1;
	}

	public static <A> int atLast(List<A> list, Predicate<A> block) {
		for (int n = list.size() - 1; n >= 0; n--) {
			if (block.is(list.get(n)))
				return n;
		}
		return -1;
	}

	public static <T> T first(List<T> list) {
		return list.get(0);
	}

	public static <T> T last(List<T> list) {
		return list.get(list.size() - 1);
	}

}
