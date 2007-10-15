package ch.akuhn.util;

import java.util.Iterator;

public abstract class Strings {

	public static final Iterable<Character> iter(final CharSequence string) {
		return new Iterable<Character>() {
			public Iterator<Character> iterator() {
				return new Iterator<Character>() {
					private int index = 0;
					public boolean hasNext() {
						return index < string.length();
					}
		
					public Character next() {
						return string.charAt(index++);
					}
		
					public void remove() {
						throw new UnsupportedOperationException();
					}
				};
			}
		};
	}
	
}
