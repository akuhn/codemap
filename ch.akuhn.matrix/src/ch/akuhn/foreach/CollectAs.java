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


public class CollectAs<E,R> implements Iterable<EachAs<E,R>> {

	public static <E,R> CollectAs<E,R> from(Class<R> returnType, E... elements) {
		return new CollectAs<E,R>(Arrays.asList(elements), returnType);
	}

	public static <E,R> CollectAs<E,R> from(Class<R> returnType, Iterable<E> elements) {
		return new CollectAs<E,R>(elements, returnType);
	}

	private Class<?> type;
	private Iter iter;

	private CollectAs(Iterable<E> elements, Class<R> type) {
		this.iter = new Iter(elements.iterator());
		this.type = type;
	}

	public List<R> getResult() {
		return iter.getResult();
	}

	@SuppressWarnings("unchecked")
	public R[] getResultArray() {
		List<R> result = getResult();
		return result.toArray((R[]) Array.newInstance(type, result.size()));
	}
	
	
	public Iterator<EachAs<E,R>> iterator() {
		return iter.start();
	}

	private class Iter implements Iterator<EachAs<E,R>> {

		private EachAs<E,R> each;
		private Iterator<E> elements;
		private int index = 0;
		private List<R> result;
		private State state;

		private Iter(Iterator<E> elements) {
			state = NULL;
			this.elements = elements;
		}

		private List<R> getResult() {
			if (state == YIELD) hasNext();
			return result;
		}

		public boolean hasNext() {
			if (state == YIELD) {
				result.add(each.yield);
				state = VOID;
			}
			if (elements.hasNext()) return true;
			elements = null;
			return false;
		}

		public EachAs<E,R> next() {
			if (!hasNext()) throw new NoSuchElementException();
			each.value = elements.next();
			each.yield = null;
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
			each = new EachAs<E,R>();
			result = new ArrayList<R>();
			index = 0;
			return this;
		}
	}

}
