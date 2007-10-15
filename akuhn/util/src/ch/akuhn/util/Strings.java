package ch.akuhn.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

public abstract class Strings {

	public static final Iterable<Character> forEach(final CharSequence string) {
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
	
	public final static Iterable<CharSequence> camelCase(final CharSequence string) {
		return new Iterable<CharSequence>() {
			public Iterator<CharSequence> iterator() {
				return new Iterator<CharSequence>() {
					private int index = 0;
					public boolean hasNext() {
						return index < string.length();
					}

					public CharSequence next() {
						if (index >= string.length()) throw new NoSuchElementException();
						int start = index++; // first letter is okay
						if (index < string.length()) {
							char ch = string.charAt(index++);
							boolean abbreviation = Character.isUpperCase(ch); 
							for (; index < string.length(); index++) {
								if (abbreviation != Character.isUpperCase(string.charAt(index))) break;
							}
							if (abbreviation && index < string.length()) index--;
						}
						return string.subSequence(start, index);
					}

					public void remove() {
						throw new UnsupportedOperationException();
					}
				};
			}
		};
	}


	
}
