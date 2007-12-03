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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Collections;
import java.util.Set;
/**
 * A collection that contains unordered, duplicate elements.
 * 
 */
public class Bag<T> extends MAbstractCollection<T> {

	private class Itr implements Iterator<T> {

		private int count;
		private T curr;
		private Iterator<T> iter;

		public Itr() {
			iter = values.keySet().iterator();
			this.prefetch();
		}

		@Override
		public boolean hasNext() {
			return count > 0 || iter.hasNext();
		}

		@Override
		public T next() {
			if (count <= 0)
				this.prefetch();
			if (curr == null)
				throw new UnsupportedOperationException();
			count--;
			return curr;
		}

		private void prefetch() {
			if (iter.hasNext()) {
				curr = iter.next();
				count = values.get(curr).value;
			} else {
				curr = null;
			}
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

	}

	private static class Int {

		public int value;

	}

	private Map<T, Int> values = new HashMap<T, Int>();

	/** Adds the specified element to this bag. <p>If, as a result of this call, the occurrences of the specified element exceed <tt>Integer.MAX_VALUE</tt>, 
	 * the number of occurrences is set to <tt>Integer.MAX_VALUE</tt>.
	 * 
	 * @param e the element to be added to this bag.
	 * @return <tt>true</tt> if the bag changes as a result of this call
	 */
	@Override
	public boolean add(T e) {
		Int i = values.get(e);
		if (i == null) {
			values.put(e, i = new Int());
		}
		if (i.value == Integer.MAX_VALUE) return false;
		i.value++;
		return true;
	}

	/** Adds <tt>n</tt> occurrences of the specified element to this bag.
	 * <p>If, as a result of this call, the occurrences of the specified element exceed <tt>Integer.MAX_VALUE</tt>, 
	 * the number of occurrences is set to <tt>Integer.MAX_VALUE</tt>.
	 * 
	 * @param e the element to be added <tt>n</tt> times to this bag.
	 * @param n the number of times element <tt>e</tt> is to be added.
	 * @return <tt>true</tt> if the bag changes as a result of this call
	 */
	public boolean add(T e, int n) {
		if (n < 0) throw new IllegalArgumentException();
		if (n == 0) return false;
		Int i = values.get(e);
		if (i == null) {
			values.put(e, i = new Int());
		}
		if (i.value == Integer.MAX_VALUE) return false;
		if (i.value > Integer.MAX_VALUE - n) {
			i.value = Integer.MAX_VALUE;
		} else {
			i.value += n;
		}
		return true;
	}

	  /**
     * Removes all of the elements from this bag.
     * The bag will be empty after this method returns. */
	@Override
	public void clear() {
		values.clear();
	}

	/** Returns <tt>true</tt> if this bag contains the specified element.
    *
    * @param o element whose presence in this collection is to be tested
    * @return <tt>true</tt> if this bag contains the specified
    *         element	
    */         
	@Override
	public boolean contains(Object o) {
		return values.containsKey(o);
	}

	/**
     * Returns <tt>true</tt> if this bag contains no elements.
     *
     * @return <tt>true</tt> if this bag contains no elements
     */
	@Override
	public boolean isEmpty() {
		return values.isEmpty();
	}

	/**
     * Returns an iterator over the elements in this collection.  There are no
     * guarantees concerning the order in which the elements are returned.
     *
     * @return an <tt>Iterator</tt> over the elements in this collection
     */
	@Override
	public Iterator<T> iterator() {
		return new Itr();
	}
	
	/** Return a <tt>Set</tt> view of the elements contained in this bag. The set is backed by the bag,
	 * so changes to the bag are reflected in the set, but <i>not</i> vice-versa. 
	 * 
	 * @return an unmodifiable set view of the elements contained in this bag.
	 */
	public Set<T> elementSet() {
		return Collections.unmodifiableSet(values.keySet());
	}
	

	/** Returns the number of occurrences of the specified element in this bag.
	 * 
	 * @param o element whose occurrence in this bag is to be queried.
	 * @return the number of occurrences of the specified element, otherwise <tt>0</tt>.
	 */
	public int occurrences(Object o) {
		Int sum = values.get(o);
		return sum == null ? 0 : sum.value;
	}
	
	/** Returns the maximum number of occurrences of any element in this bag.
	 * 
	 * @return the maximum number of occurrences of any element, or <tt>0</tt> if this bag is empty.
	 */
	public int maxOccurrences() {
		int max = 0;
		for (Int each : values.values()) {
			max = Math.max(max, each.value);
		}
		return max;
	}
	
	/** Returns the element with most occurrences in this bag. There are no guarantees concerning the returned element, if there is more than one element
     * that has the same number of most occurrences.
     * 
     * @return any of the elements with most occurrences, or <tt>null</tt> if the bag is empty.
     */
	public T mostOccurring() {
		int max = 0;
		T most = null;
		for (Map.Entry<T, Int> each : values.entrySet()) {
			if (max < (max = Math.max(max, each.getValue().value))) {
				most = each.getKey();
			}
		}
		return most;
	}

	/**
     * Removes a single instance of the specified element from this
     * bag, if it is present.
     *
     * @param o element to be removed from this bag, if present.
     * @return <tt>true</tt> if an element was removed as a result of this call.
	 */
	@Override
	public boolean remove(Object o) {
		Int i = values.get(o);
		if (i == null) return false;
		i.value--;
		if (i.value == 0) {
			values.remove(o);
		}
		return true;
	}
	
	/**
     * Removes all occurrences of the specified element from this
     * bag.
     *
     * @param o elements to be removed from this bag, if present.
     * @return <tt>true</tt> if elements were removed as a result of this call.
	 */
	public boolean removeEach(Object o) {
		Int i = values.get(o);
		if (i == null) return false;
		values.remove(o);
		return true;
	}

	/** Returns the number of elements in this bag. If this bag
     * contains more than <tt>Integer.MAX_VALUE</tt> elements, returns
     * <tt>Integer.MAX_VALUE</tt>.
	 * 
	 * @return the number of elements in this bag.
	 */
	public int size() {
		int size = 0;
		for (Int i : values.values()) {
			if (size > Integer.MAX_VALUE - i.value) return Integer.MAX_VALUE;
			size += i.value;
		}
		return size;
	}

	/** Returns the number of unique elements in this bag.
	 * 
	 * @return the number of unique elements in this bag.
	 */
	public int elementSize() {
		return values.size();
	}
	
}
