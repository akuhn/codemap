package ch.deif.meander.util;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicReference;

public abstract class Collect<E> implements Iterable<Collect<E>> { 

	public static <E> Collect<E> fromArray(E... elements) {
		return new FromArray<E>(elements);
	}
	
	private static class FromArray<E> extends Collect<E> implements Iterator<Collect<E>> {

		private E[] elements;
		private E[] result;
		private int index = 0;
		private AtomicReference<FromArray<E>> iterator;
		
		public FromArray(E[] elements) {
			this.elements = elements;
			this.iterator = new AtomicReference<FromArray<E>>();
		}
		
		@Override
		public Iterator<Collect<E>> iterator() {
			if (!iterator.compareAndSet(null, this)) throw new IllegalStateException();
			return this;
		}

		@Override
		public boolean hasNext() {
			if (index > 0) result[index - 1] = this.value;
			return index < elements.length;
		}

		@Override
		public Collect<E> next() {
			if (!hasNext()) throw new NoSuchElementException();
			if (result == null) result = elements.clone();
			this.value = elements[index++];
			return this;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

		@Override
		public E[] asArray() {
			return result;
		}
		
	}

	public E value;
	
	public abstract E[] asArray();

	public List<E> asList() {
		return Arrays.asList(asArray());
	}
	
}
