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

/** Loops over each element of a collection, returning a new
 * collection containing the values yielded by the loop body.
 * In the loop body, read <em>each.value</em> to access the current
 * elements, and write <em>each.yield</em> to yield elements.
 *<P>
 * Example:
 *<PRE>
 * Collect&lt;String&gt; query = Collect.from(words);
 * for (Each&lt;String&gt; each: query) {
 *     each.yield = each.value.toUppercase();
 * }
 * result = query.resultAsList(); 
 *</PRE>
 * Or use the short form:
 *<PRE>
 * for (Each&lt;String&gt; each: Query.collect(words)) {
 *     each.yield = each.value.toUppercase();
 * }
 * result = Query.resultAsList(); 
 *</PRE> 
 *   
 * 
 * @author akuhn
 *
 * @param <E>
 */
public abstract class Collect<E> implements Iterable<Each<E>> { 

	public static <E> Collect<E> fromArray(E... elements) {
		return new CollectFromArray<E>(elements);
	}
	
	public static <E> Collect<E> fromCollection(Iterable<E> elements) {
		return new FromCollection<E>(elements);
	}
	
	public static <E> Collect<E> from(Iterable<E> elements) {
		return Collect.fromCollection(elements);
	}

	public abstract E[] resultArray();

	public abstract E[] resultArray(Class<? extends E> type);

	public abstract List<E> getResult();
	
}

class CollectFromArray<E> extends Collect<E> implements Iterator<Each<E>> {
	
	private State state;
	private Each<E> each;
	private E[] elements;
	private E[] result;
	private int index = 0;

	public CollectFromArray(E[] elements) {
		this.state = NULL;
		this.elements = elements;
	}
	
	@Override
	public Iterator<Each<E>> iterator() {
		if (state != NULL) throw new IllegalStateException("Cannot run query twice!");
		state = VOID;
		each = new Each<E>();
		result = elements.clone();
		index = 0;
		return this;
	}
	
	@Override
	public boolean hasNext() {
		if (state == YIELD) {
			result[index - 1] = each.yield;
			state = VOID;
		}
		if (index < elements.length) return true;
		elements = null;
		return false;
	}
	
	@Override
	public Each<E> next() {
		if (!hasNext()) throw new NoSuchElementException();
		each.yield = each.value = elements[index];
		each.index = index++;
		state = YIELD;
		return each;
	}
	
	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public E[] resultArray() {
		if (state == YIELD) hasNext();
		return result;
	}

	@Override
	public List<E> getResult() {
		return Arrays.asList(result);
	}

	@Override
	public E[] resultArray(Class<? extends E> type) {
		return result;
	}
	
}

class FromCollection<E> extends Collect<E> implements Iterator<Each<E>> {

	private State state;
	private Each<E> each;
	private Iterator<E> elements;
	private List<E> result;
	private int index = 0;
	
	public FromCollection(Iterable<E> elements) {
		this.state = NULL;
		this.elements = elements.iterator();
	}
	
	@Override
	public Iterator<Each<E>> iterator() {
		if (state != NULL) throw new IllegalStateException("Cannot run query twice!");
		state = VOID;
		each = new Each<E>();
		result = new ArrayList<E>();
		index = 0;
		return this;
	}

	@Override
	public boolean hasNext() {
		if (state == YIELD) {
			result.add(each.yield);
			state = VOID;
		}
		if (elements.hasNext()) return true;
		elements = null;
		return false;
	}

	@Override
	public Each<E> next() {
		if (!hasNext()) throw new NoSuchElementException();
		each.yield = each.value = elements.next();
		each.index = index++;
		state = YIELD;
		return each;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

	@Override
	@SuppressWarnings("unchecked")
	public E[] resultArray() {
		return resultArray((Class<? extends E>) getResult().iterator().next().getClass());
	}

	@Override
	public List<E> getResult() {
		if (state == YIELD) hasNext();
		return result;
	}

	@Override
	@SuppressWarnings("unchecked")
	public E[] resultArray(Class<? extends E> type) {
		return getResult().toArray((E[]) Array.newInstance(type, result.size()));
	}
	
}
