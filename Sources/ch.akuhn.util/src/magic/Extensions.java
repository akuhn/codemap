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

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import magic.util.Pair;


/**
 * Methods for static import.
 * 
 * @author Adrian Kuhn
 * 
 */
@SuppressWarnings("unchecked")
public abstract class Extensions {

    //private final static Object NONE = new Object();

    public static <T> T[] Array(Class<T> tClass, T t, T... ts) {
        T[] $ = (T[]) Array.newInstance(tClass, ts.length + 1);
        System.arraycopy(ts, 0, $, 1, ts.length);
        $[0] = t;
        return $;
    }
    
    public static <T> T[] Array(T t, T... ts) {
        return (T[]) Array(leastUpperBound(t.getClass(), ts), t, ts);
    }
    
    public static Class<?> leastUpperBound(Class<?> initial, Object... os) {
        Class $ =  initial;
        for (Object o : os) {
            while (!$.isAssignableFrom(o.getClass())) {
                $ = $.getSuperclass();
                if ($ == null) return Object.class; 
            }
        }
        return $;
    }
    
	/**
	 * Iterate indefinitely over <code>iterable</code>.
	 * 
	 */
	public static final <E> Iterable<E> cycle(final Iterable<E> iterable) {
		return new Iterable<E>() {
			public Iterator<E> iterator() {
				return new Iterator<E>() {
					private Iterator<E> it = iterable.iterator();
					
					public boolean hasNext() {
						if (!it.hasNext()) it = iterable.iterator();
						return it.hasNext();
					}

					public E next() {
						if (!it.hasNext()) it = iterable.iterator();
						return it.next();
					}

					public void remove() {
						it.remove();
					}
				};
			}
		};
	}

	/** Returns first element or fail.
	 * 
	 * @throws NoSuchElementException if <code>iterable</code> has no elements
	 */
	public static final <E> E head(final Iterable<E> iterable) {
		return iterable.iterator().next();
	}

	/** Returns first element or default value.
	 */
	public static final <E> E head(final Iterable<E> iterable, final E defaultValue) {
		Iterator<E> it = iterable.iterator();
		return it.hasNext() ? it.next() : defaultValue;
	}

	/** Checks if <code>iterable</code> has no elements. 
	 */
	public static final boolean isEmpty(final Iterable<?> iterable) {
		return !iterable.iterator().hasNext();
	}

	public static boolean eq(Object a, Object b) {
        return a == null ? b == null : a.equals(b);
    }

	public static <E> Iterable<E> each(final Iterator<E> iter) {
	    return new Iterable<E>() {
	        public Iterator<E> iterator() {
	            return iter;
	        }
	    };
	}
	
	public static <E> Iterable<E> each(final E... elements) {
		return new Iterable<E>() {
			public Iterator<E> iterator() {
				return new Iterator<E>() {
					private int index = 0;
					
					public boolean hasNext() {
						return index < elements.length;
					}
					
					public E next() {
						if (index >= elements.length) throw new NoSuchElementException();
						return elements[index++];
					}
					
					public void remove() {
						throw new UnsupportedOperationException();
					}
				};
			}
			
		};
	}
	
	public static <E> E last(E[] es) {
        return es[es.length - 1];
    }

	public static <E> E last(List<E> es) {
    	return es.get(es.size() - 1);
    }

	public static <E> E last(Iterable<E> es) {
	    E $ = null;
	    for (E e : es) $ = e;
	    return $;
	}
	
	public static <E> List<E> List(E... elements) {
    	return Arrays.asList(elements);
    }

    /** Iterate over all consecutive pairs of <code>iterable</code>. 
	 * @return if <code>iterable</code> has less than two elements, the returned iterable is empty.
	 */
	public static final <E> Iterable<Pair<E, E>> pairs(final Iterable<E> iterable) {
		return new Iterable<Pair<E, E>>() {
			public Iterator<Pair<E, E>> iterator() {
				return new Iterator<Pair<E, E>>() {
					private final Iterator<E> it = iterable.iterator();
					private E prev = it.hasNext() ? it.next() : null;

					public boolean hasNext() {
						return it.hasNext();
					}

					public Pair<E, E> next() {
						return Pair.of(prev, prev = it.next());
					}

					public void remove() {
						throw new UnsupportedOperationException();
					}
				};
			}
		};
	}

    public static <E> void  puts(Iterable<E> iterable) {
        System.out.print("[");
        Separator s = new Separator();
        for (Object o : iterable) {
            System.out.print(s);
            System.out.print(o);
        }
        System.out.println("]");
    }

    public static void puts(Object object) {
        System.out.println(object);
    }

    public static void puts(Object object, Object... objects) {
        System.out.println(object);
        for (Object o : objects) {
            System.out.println(o);
        }
    }

    public static void puts(String format, Object... objects) {
        System.out.println(String.format(format, objects));
    }
    
    public static void puts(Object[] objects) {
        System.out.print("[");
        Separator s = new Separator();
        for (Object o : objects) {
            System.out.print(s);
            System.out.print(o);
        }
        System.out.println("]");
    }
    
    
    /** Iterate over integers 0 to <code>n</code>, excluding <code>n</code>.
	 * 
	 * @param n any integer.
	 * @return if <code>n</code> is negative, the returned iterable is empty.
	 */
	public static final Iterable<Integer> range(final int n) {
		return new Iterable<Integer>() {
			public Iterator<Integer> iterator() {
				return new Iterator<Integer>() {
					private int current = 0;

					public boolean hasNext() {
						return current < n;
					}

					public Integer next() {
						if (!(current < n)) throw new NoSuchElementException();
						return current++;
					}

					public void remove() {
						throw new UnsupportedOperationException();
					}
				};
			}
		};
	}
    
    public static <E> Set<E> Set(E... elements) {
    	return new HashSet<E>(Arrays.asList(elements));
    }

    /** Iterate over all elements of <code>iterable</code>, except the first one.
	 * 
	 */
	public static final <E> Iterable<E> tail(final Iterable<E> iterable) {
		return new Iterable<E>() {
			public Iterator<E> iterator() {
				return new Iterator<E>() {
					private final Iterator<E> it = iterable.iterator();
					{
						if (it.hasNext()) it.next();
					};

					public boolean hasNext() {
						return it.hasNext();
					}

					public E next() {
						return it.next();
					}

					public void remove() {
						it.remove();
					}
				};
			}
		};
	}

    private Extensions() {
		throw new AssertionError();
	}
    
}
