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

/**
 * Loops over each element of a collection, returning a new collection
 * containing the values yielded by the loop body. In the loop body, read
 * <em>each.value</em> to access the current elements, and write
 * <em>each.yield</em> to yield elements.
 *<P>
 * Example:
 * 
 * <PRE>
 * Collect&lt;String&gt; query = Collect.from(words);
 * for (Each&lt;String&gt; each : query) {
 * 	each.yield = each.value.toUppercase();
 * }
 * result = query.resultAsList();
 *</PRE>
 * 
 * Or use the short form:
 * 
 * <PRE>
 * for (Each&lt;String&gt; each : Query.collect(words)) {
 * 	each.yield = each.value.toUppercase();
 * }
 * result = Query.resultAsList();
 *</PRE>
 * 
 * 
 * @author akuhn
 * 
 * @param <E>
 */
public class Collect<E> implements Iterable<Each<E>> {

	public static <E> Collect<E> from(E... elements) {
		return new Collect<E>(Arrays.asList(elements), elements.getClass().getComponentType());
	}

	public static <E> Collect<E> from(Iterable<E> elements) {
		return new Collect<E>(elements, null);
	}

	private Class<?> type;
	private Iter iter;

	private Collect(Iterable<E> elements, Class<?> type) {
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

	@SuppressWarnings("unchecked")
	public E[] getResultArray(Class<? extends E> type) {
		List<E> result = getResult();
		return result.toArray((E[]) Array.newInstance(type, result.size()));
	}
	
	
	public Iterator<Each<E>> iterator() {
		return iter.start();
	}

	private class Iter implements Iterator<Each<E>> {

		private Each<E> each;
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
				result.add(each.yield);
				state = VOID;
			}
			if (elements.hasNext()) return true;
			elements = null;
			return false;
		}

		public Each<E> next() {
			if (!hasNext()) throw new NoSuchElementException();
			each.yield = each.value = elements.next();
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
			each = new Each<E>();
			result = new ArrayList<E>();
			index = 0;
			return this;
		}
	}

}
