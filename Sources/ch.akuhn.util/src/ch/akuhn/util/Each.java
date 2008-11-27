package ch.akuhn.util;

import java.util.Iterator;

public class Each {

	public static final <A,B> Provider<Pair<A,B>> zip(final Iterator<A> first, final Iterator<B> second) {
		return new Provider<Pair<A,B>>() {
			@Override
			public Pair<A, B> provide() {
				if (!first.hasNext() || !second.hasNext()) return done();
				return Pair.of(first.next(), second.next());
			}
		};
	}
	
}
