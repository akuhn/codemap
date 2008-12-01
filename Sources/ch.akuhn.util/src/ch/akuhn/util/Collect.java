package ch.akuhn.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ch.akuhn.util.blocks.Function;

public class Collect {

	private static class Collectable<T,A> implements Iterable<T> {
		private final Iterable<A> iterable;
		private final Function<T, ? super A> func;

		public Collectable(Iterable<A> iterable, Function<T, ? super A> func) {
			this.iterable = iterable;
			this.func = func;
		}

		public Iterator<T> iterator() {
			return new Collector<T,A>(iterable.iterator(), func);
		}
	}

	private static class Collector<T,A> implements Iterator<T> {
		private final Iterator<A> iterator;
		private final Function<T, ? super A> func;

		public Collector(Iterator<A> iterator, Function<T, ? super A> func) {
			this.iterator = iterator;
			this.func = func;
		}

		public boolean hasNext() {
			return iterator.hasNext();
		}

		public T next() {
			return func.apply(iterator.next());
		}

		public void remove() {
			iterator.remove();
		}
	}

	public static <T,A> Iterable<T> each(Iterable<A> iterable, Function<T, ? super A> func) {
		return new Collectable<T, A>(iterable, func);
	}
	
	public static <T,A> Iterable<T> each(Iterable<A> iter, String name) {
		return Collect.each(iter, Methods.<T,A>asFunction(name));
	}
	
	public static <T,A> List<T> each(List<A> list, Function<T, ? super A> func) {
		List<T> reply = new ArrayList<T>(list.size());
		for (A a : list) reply.add(func.apply(a));
		return reply;
	}

	public static <T,A> List<T> each(List<A> list, String name) {
		return Collect.each(list, Methods.<T,A>asFunction(name));
	}
	
}
