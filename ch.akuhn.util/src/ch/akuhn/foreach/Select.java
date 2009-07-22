package ch.akuhn.foreach;

import static ch.akuhn.foreach.State.NULL;
import static ch.akuhn.foreach.State.VOID;
import static ch.akuhn.foreach.State.YIELD;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public final class Select<E> implements Iterable<EachB<E>> {

	public static <E> Select<E> from(E... elements) {
		return new Select<E>(Arrays.asList(elements), elements.getClass().getComponentType());
	}

	public static <E> Select<E> from(Iterable<E> elements) {
		return new Select<E>(elements, null);
	}

	private Class<?> type;
	private Iter iter;

	private Select(Iterable<E> elements, Class<?> type) {
		this.iter = new Iter(elements.iterator());
		this.type = type;
	}

	public List<E> getResult() {
		return iter.getResult();
	}

	@SuppressWarnings("unchecked")
	public E[] getResultArray() {
		List<E> result = getResult();
		if (type == null) type = result.iterator().next().getClass();
		return result.toArray((E[]) Array.newInstance(type, result.size()));
	}
	
	public Iterator<EachB<E>> iterator() {
		return iter.start();
	}

	private class Iter implements Iterator<EachB<E>> {

		private EachB<E> each;
		private Iterator<E> elements;
		private int index = 0;
		private List<E> result;
		private State state;

		private Iter(Iterator<E> elements) {
			state = NULL;
			this.elements = elements;
		}

		private List<E> getResult() {
			if (state == YIELD) hasNext();
			return result;
		}

		public boolean hasNext() {
			if (state == YIELD) {
				if (each.yield) result.add(each.value);
				state = VOID;
			}
			if (elements.hasNext()) return true;
			elements = null;
			return false;
		}

		public EachB<E> next() {
			if (!hasNext()) throw new NoSuchElementException();
			each.value = elements.next();
			each.yield = false;
			each.index = index++;
			state = YIELD;
			return each;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}

		private Iter start() {
			if (state != NULL) throw new IllegalStateException("Cannot run query twice!");
			state = VOID;
			each = new EachB<E>();
			result = new ArrayList<E>();
			index = 0;
			return this;
		}
	}

}
