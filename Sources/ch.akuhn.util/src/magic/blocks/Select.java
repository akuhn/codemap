package magic.blocks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import magic.util.Provider;

public class Select {
	
	public static final <T> Iterable<T> from(final Iterable<T> it, final Predicate<? super T> f) {
		return new Iterable<T>() {
			public Iterator<T> iterator() {
				return Select.from(it.iterator(), f);
			}
		};
	}
	
	public static final <T> Iterator<T> from(final Iterator<T> it, final Predicate<? super T> f) {
		return new Provider<T>() {
			@Override
			public T provide() {
				while (it.hasNext()) {
					T t = it.next();
					if (f.apply(t)) return t;
				}
				return done();
			}
		};
	}
	
	public static final <T> List<T> from(final List<T> ts, final Predicate<? super T> f) {
		ArrayList<T> $ = new ArrayList<T>();
		for (T t : ts) {
			if (f.apply(t)) $.add(t);
		}
		$.trimToSize();
		return $;
	}
	
	@SuppressWarnings("unchecked")
	public static final <T> T[] from(final T[] ts, final Predicate<? super T> f) {
		return (T[]) Select.from(Arrays.asList(ts), f).toArray();
	}
	
}
