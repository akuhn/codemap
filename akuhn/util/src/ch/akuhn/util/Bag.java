package ch.akuhn.util;

import java.util.*;

public class Bag<T> extends AbstractCollection<T> {

	private Map<T,Sum> values = new HashMap<T,Sum>();
	
	private static class Sum {
		
		private int sum;

		public Sum() {
			this.sum = 0;
		}
		
		public int intValue() {
			return sum;
		}
		
		public void inc() {
			sum++;
		}
		
		public void inc(int summand) {
			sum += summand;
		}
	}
	
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
	
	public int occurrences(Object o) {
		Sum sum = values.get(o);
		return sum == null ? 0 : sum.intValue();
	}

	@Override
	public boolean isEmpty() {
		return values.isEmpty();
	}

	@Override
	public Iterator<T> iterator() {
		return new Itr();
	}
	
	
	private class Itr implements Iterator<T> {
		
		
			private Iterator<T> iter;
			private T curr;
			private int count;
			
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
				if (count <= 0) this.prefetch();
				if (curr == null) throw new UnsupportedOperationException();
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
