//  Copyright (c) 1998-2008 Adrian Kuhn <akuhn(a)students.unibe.ch>
//  
//  This file is part of ch.akuhn.util.
//  
//  ch.akuhn.util is free software: you can redistribute it and/or modify it
//  under the terms of the GNU Lesser General Public License as published by the
//  Free Software Foundation, either version 3 of the License, or (at your
//  option) any later version.
//  
//  ch.akuhn.util is distributed in the hope that it will be useful, but
//  WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
//  or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
//  License for more details.
//  
//  You should have received a copy of the GNU Lesser General Public License
//  along with ch.akuhn.util. If not, see <http://www.gnu.org/licenses/>.
//  

package magic.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A class for constructing lists by appending elements. Modelled after
 * java.lang.StringBuffer.
 * 
 */
public class ListBuffer<A> implements Collection<A> {

	public static <T> ListBuffer<T> lb() {
		return new ListBuffer<T>();
	}

	/**
	 * The list of elements of this buffer.
	 */
	public List<A> elems;

	/**
	 * A pointer pointing to the last, sentinel element of `elems'.
	 */
	public List<A> last;

	/**
	 * The number of element in this buffer.
	 */
	public int count;

	/**
	 * Has a list been created from this buffer yet?
	 */
	public boolean shared;

	/**
	 * Create a new initially empty list buffer.
	 */
	public ListBuffer() {
		clear();
	}

	public final void clear() {
		this.elems = new List<A>(null, null);
		this.last = this.elems;
		count = 0;
		shared = false;
	}

	/**
	 * Return the number of elements in this buffer.
	 */
	public int length() {
		return count;
	}

	public int size() {
		return count;
	}

	/**
	 * Is buffer empty?
	 */
	public boolean isEmpty() {
		return count == 0;
	}

	/**
	 * Is buffer not empty?
	 */
	public boolean nonEmpty() {
		return count != 0;
	}

	/**
	 * Copy list and sets last.
	 */
	private void copy() {
		List<A> p = elems = new List<A>(elems.head, elems.tail);
		while (true) {
			List<A> tail = p.tail;
			if (tail == null)
				break;
			tail = new List<A>(tail.head, tail.tail);
			p.setTail(tail);
			p = tail;
		}
		last = p;
		shared = false;
	}

	/**
	 * Prepend an element to buffer.
	 */
	public ListBuffer<A> prepend(A x) {
		elems = elems.prepend(x);
		count++;
		return this;
	}

	/**
	 * Append an element to buffer.
	 */
	public ListBuffer<A> append(A x) {
		if (shared)
			copy();
		last.head = x;
		last.setTail(new List<A>(null, null));
		last = last.tail;
		count++;
		return this;
	}

	/**
	 * Append all elements in a list to buffer.
	 */
	public ListBuffer<A> appendList(List<A> xs) {
		while (xs.nonEmpty()) {
			append(xs.head);
			xs = xs.tail;
		}
		return this;
	}

	/**
	 * Append all elements in a list to buffer.
	 */
	public ListBuffer<A> appendList(ListBuffer<A> xs) {
		return appendList(xs.toList());
	}

	/**
	 * Append all elements in an array to buffer.
	 */
	public ListBuffer<A> appendArray(A[] xs) {
		for (int i = 0; i < xs.length; i++) {
			append(xs[i]);
		}
		return this;
	}

	/**
	 * Convert buffer to a list of all its elements.
	 */
	public List<A> toList() {
		shared = true;
		return elems;
	}

	/**
	 * Does the list contain the specified element?
	 */
	public boolean contains(Object x) {
		return elems.contains(x);
	}

	/**
	 * Convert buffer to an array
	 */
	public <T> T[] toArray(T[] vec) {
		return elems.toArray(vec);
	}

	public Object[] toArray() {
		return toArray(new Object[size()]);
	}

	/**
	 * The first element in this buffer.
	 */
	public A first() {
		return elems.head;
	}

	/**
	 * Remove the first element in this buffer.
	 */
	public void remove() {
		if (elems != last) {
			elems = elems.tail;
			count--;
		}
	}

	/**
	 * Return first element in this buffer and remove
	 */
	public A next() {
		A x = elems.head;
		remove();
		return x;
	}

	/**
	 * An enumeration of all elements in this buffer.
	 */
	public Iterator<A> iterator() {
		return new Iterator<A>() {
			List<A> elems = ListBuffer.this.elems;

			public boolean hasNext() {
				return elems != last;
			}

			public A next() {
				if (elems == last)
					throw new NoSuchElementException();
				A elem = elems.head;
				elems = elems.tail;
				return elem;
			}

			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	public boolean add(A a) {
		throw new UnsupportedOperationException();
	}

	public boolean remove(Object o) {
		throw new UnsupportedOperationException();
	}

	public boolean containsAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	public boolean addAll(Collection<? extends A> c) {
		throw new UnsupportedOperationException();
	}

	public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}
}
