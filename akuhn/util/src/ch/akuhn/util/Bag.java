//  Copyright (c) 2007 Adrian Kuhn <akuhn(a)iam.unibe.ch>
//
//  This file is part of "Adrian Kuhn's Utilities for Java".
//
//  "Adrian Kuhn's Utilities for Java" is free software: you can redistribute it
//  and/or modify it under the terms of the GNU Lesser General Public License as
//  published by the Free Software Foundation, either version 3 of the License,
//  or (at your option) any later version.
//
//  "Adrian Kuhn's Utilities for Java" is distributed in the hope that it will be
//  useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser
//  General Public License for more details.
//
//  You should have received a copy of the GNU Lesser General Public License along
//  with "Adrian Kuhn's Utilities for Java". If not, see
//  <http://www.gnu.org/licenses/>.
//

package ch.akuhn.util;

import java.util.AbstractCollection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Bag<T> extends AbstractCollection<T> {

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
				count = values.get(curr).intValue();
			} else {
				curr = null;
			}
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

	}

	private static class Sum {

		private int sum;

		public Sum() {
			this.sum = 0;
		}

		public void inc() {
			sum++;
		}

		public void inc(int summand) {
			sum += summand;
		}

		public int intValue() {
			return sum;
		}
	}

	private Map<T, Sum> values = new HashMap<T, Sum>();

	@Override
	public boolean add(T e) {
		Sum sum = values.get(e);
		if (sum == null) {
			values.put(e, sum = new Sum());
		}
		sum.inc();
		return true;
	}

	public boolean add(T e, int occurrences) {
		assert occurrences >= 0;
		Sum sum = values.get(e);
		if (sum == null) {
			values.put(e, sum = new Sum());
		}
		sum.inc(occurrences);
		return true;
	}

	@Override
	public void clear() {
		values.clear();
	}

	@Override
	public boolean contains(Object o) {
		return values.containsKey(o);
	}

	@Override
	public boolean isEmpty() {
		return values.isEmpty();
	}

	@Override
	public Iterator<T> iterator() {
		return new Itr();
	}

	public int occurrences(Object o) {
		Sum sum = values.get(o);
		return sum == null ? 0 : sum.intValue();
	}

	@Override
	public boolean remove(Object o) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int size() {
		int size = 0;
		for (Sum sum : values.values()) {
			size += sum.intValue();
		}
		return size;
	}

}
