//  Copyright (c) 1998-2008 Adrian Kuhn <akuhn(a)students.unibe.ch>
//  
//  This file is part of ch.akuhn.util.
//  
//  ch.akuhn.util is free software: you can redistribute it and/or modify it
//  under the terms of the GNU Lesser General Public License as published by the
//  Free Software Foundation, either version 3 of the License, or (at your
//  option) any later version.
//  
//  ch.akuhn.util is distributed in the hope that it will be useful, but
//  WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
//  or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
//  License for more details.
//  
//  You should have received a copy of the GNU Lesser General Public License
//  along with ch.akuhn.util. If not, see <http://www.gnu.org/licenses/>.
//  

package magic;

import static java.lang.Character.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Iterator;
import java.util.NoSuchElementException;

import magic.util.SimpleIter;

/** Static methods that operate on or return strings.
 * 
 *
 */
public abstract class Strings {

	public final static Iterable<CharSequence> camelCase(
			final CharSequence string) {
		return new Iterable<CharSequence>() {
			public Iterator<CharSequence> iterator() {
				return new Iterator<CharSequence>() {
					private int index = 0;

					public boolean hasNext() {
						return index < string.length();
					}

					public CharSequence next() {
						if (index >= string.length())
							throw new NoSuchElementException();
						int start = index++; // first letter is okay
						if (index < string.length()) {
							char ch = string.charAt(index++);
							boolean abbreviation = Character.isUpperCase(ch);
							for (; index < string.length(); index++) {
								if (abbreviation != Character
										.isUpperCase(string.charAt(index)))
									break;
							}
							if (abbreviation && index < string.length())
								index--;
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

	public static final Iterable<Character> chars(final CharSequence string) {
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

	public static final CharSequence fromFile(File file) {
		try {
		    assert file.exists() : file;
			// memory mapped file
			FileInputStream input = new FileInputStream(file);
			FileChannel channel = input.getChannel();
			long fileLength = channel.size();
			MappedByteBuffer buffer = channel.map(
					FileChannel.MapMode.READ_ONLY, 0, fileLength);
			// character buffer
			Charset charset = java.nio.charset.Charset.forName("UTF-8");
			CharsetDecoder decoder = charset.newDecoder();
			return decoder.decode(buffer);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public static final CharSequence fromFile(String filename) {
		return Strings.fromFile(new File(filename));
	}

	public static final CharSequence fromResource(String name) {
		InputStream in = ClassLoader.getSystemResourceAsStream(name);
		StringBuilder builder = new StringBuilder();
		try {
			while (true) {
				int ch = in.read();
				if (ch < 0)
					break;
				builder.append((char) ch);
			}
			in.close();
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
		return builder.toString();
	}

	public static final boolean isAlphanumeric(String string) {
		for (int n = 0; n < string.length(); n++) {
			if (!Character.isLetterOrDigit(string.charAt(n))
					&& string.charAt(n) != '_')
				return false;
		}
		return true;
	}

	public static final Iterable<String> letters(final CharSequence string) {
        return new SimpleIter<String>() {
            private int index = 0;
            public String iterate() {
                for (; index < string.length(); index++) {
                    if (isLetter(string.charAt(index))) break;
                }
                if (index == string.length()) return done();
                int mark = index;
                for (; index < string.length(); index++) {
                    if (!isLetter(string.charAt(index))) break;
                }
                return string.subSequence(mark, index).toString(); 
            }
        };
    }

	public static final Iterable<String> lines(final CharSequence string) {
	    return new SimpleIter<String>() {
	        private int index = 0;
            public String iterate() {
                if (index >= string.length()) return done();
                int mark = index;
                for (; index < string.length(); index++) {
                    if (string.charAt(index) == '\n') break;
                }
                return string.subSequence(mark, index++).toString();
            }
	    };
	}

	public static final String toUpperFirstChar(String string) {
	    if (string == null || string.length() == 0) return string;
		StringBuilder $ = new StringBuilder(string.length());
		$.append(toUpperCase(string.charAt(0)));
		$.append(string.substring(1));
		return $.toString();
	}

	public static final Iterable<String> words(final CharSequence string) {
	    return new SimpleIter<String>() {
            private int index = 0;
            public String iterate() {
                for (; index < string.length(); index++) {
                    if (!isWhitespace(string.charAt(index))) break;
                }
                if (index == string.length()) return done();
                int mark = index;
                for (; index < string.length(); index++) {
                    if (isWhitespace(string.charAt(index))) break;
                }
                return string.subSequence(mark, index).toString(); 
            }
	    };
	}

	private Strings() {
		throw new AssertionError();
	}

	public static final String reformatParagraph(String paragraph) {
		return reformatParagraph(paragraph, 76);
	}
	
	public static final String reformatParagraph(String paragraph, int breakAt) {
		StringBuilder builder = new StringBuilder();
		int length = 0;
		for (String word : words(paragraph)) {
			int newLength = length + 1 + word.length();
			if (newLength > breakAt) {
				builder.append("\n");
				newLength = word.length();
			} else if (length > 0) {
				builder.append(' ');
			}
			builder.append(word);
			length = newLength;
		}
		return builder.toString();
	}

}
