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

package ch.akuhn.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Set;

/**
 * Methods for static import.
 * 
 * @author Adrian Kuhn
 * 
 */
@SuppressWarnings("unchecked")
public abstract class Extensions {

    // private final static Object NONE = new Object();

    public static <T> T[] Array(Class<T> tClass, T t, T... ts) {
        T[] $ = (T[]) Array.newInstance(tClass, ts.length + 1);
        System.arraycopy(ts, 0, $, 1, ts.length);
        $[0] = t;
        return $;
    }

    public static <T> T[] Array(T t, T... ts) {
        return (T[]) Array(leastUpperBound(t.getClass(), ts), t, ts);
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

    public static <E> Iterable<E> each(final Enumeration<E> enumeration) {
        return new Iterable<E>() {
            public Iterator<E> iterator() {
                return new Iterator<E>() {
                    public boolean hasNext() {
                        return enumeration.hasMoreElements();
                    }

                    public E next() {
                        return enumeration.nextElement();
                    }

                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
    }

    public static <E> Iterable<E> each(final Iterator<E> iter) {
        return new Iterable<E>() {
            public Iterator<E> iterator() {
                return iter;
            }
        };
    }

    public static boolean eq(Object a, Object b) {
        return a == null ? b == null : a.equals(b);
    }

    /**
     * Returns first element or fail.
     * 
     * @throws NoSuchElementException
     *             if <code>iterable</code> has no elements
     */
    public static final <E> E head(final Iterable<E> iterable) {
        return iterable.iterator().next();
    }

    /**
     * Returns first element or default value.
     */
    public static final <E> E head(final Iterable<E> iterable, final E defaultValue) {
        Iterator<E> it = iterable.iterator();
        return it.hasNext() ? it.next() : defaultValue;
    }

    /**
     * Checks if <code>iterable</code> has no elements.
     */
    public static final boolean isEmpty(final Iterable<?> iterable) {
        return !iterable.iterator().hasNext();
    }

    public static <E> E last(E[] es) {
        return es[es.length - 1];
    }

    public static <E> E last(Iterable<E> es) {
        E $ = null;
        for (E e : es)
            $ = e;
        return $;
    }

    public static <E> E last(List<E> es) {
        return es.get(es.size() - 1);
    }

    public static Class<?> leastUpperBound(Class<?> initial, Object... os) {
        Class $ = initial;
        for (Object o : os) {
            while (!$.isAssignableFrom(o.getClass())) {
                $ = $.getSuperclass();
                if ($ == null) return Object.class;
            }
        }
        return $;
    }

    public static <T> T[] newArray(T[] arr, T t) {
        T[] $ = (T[]) Array.newInstance(arr.getClass().getComponentType(), arr.length + 1);
        System.arraycopy(arr, 0, $, 0, arr.length);
        $[arr.length] = t;
        return $;
    }

    public static <T> T[] newArray(T[] aaa, T[] bbb) {
        T[] $ = (T[]) Array.newInstance(aaa.getClass().getComponentType(), aaa.length + bbb.length);
        System.arraycopy(aaa, 0, $, 0, aaa.length);
        System.arraycopy(bbb, 0, $, aaa.length, bbb.length);
        return $;
    }

    public static <E> List<E> newList(E... elements) {
        return Arrays.asList(elements);
    }

    public static <E> List<E> newList(Iterable<E> iterable) {
        ArrayList<E> list = new ArrayList<E>();
        for (E each : iterable)
            list.add(each);
        list.trimToSize();
        return list;
    }

    public static RuntimeException newRethrowable(Throwable cause) {
        throw Throw.runtimeException(cause);
    }

    public static void puts(int[] more) {
        System.out.print("#(");
        Separator s = new Separator();
        for (int each : more) {
            System.out.print(s);
            System.out.print(each);
        }
        System.out.println(")");
    }

    public static <E> void puts(Iterable<E> iterable) {
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

    public static void puts(Object[] objects) {
        System.out.print("[");
        Separator s = new Separator();
        for (Object o : objects) {
            System.out.print(s);
            System.out.print(o);
        }
        System.out.println("]");
    }

    public static void puts(String format, Object... objects) {
        System.out.println(String.format(format, objects));
    }

    public static <E> Set<E> Set(E... elements) {
        return new HashSet<E>(Arrays.asList(elements));
    }

    public static <T> Iterable<T> shuffle(final Iterable<T> iterable) {
        // Fisher-Yates algorithm
        return new Provider<T>() {
            
            List<T> list = newList(iterable);
            final int len = list.size();
            int n = 0;
            Random random = new Random();

            @Override
            public T provide() {
                if (n >= len) return done();
                int s = random.nextInt(len - n) + n;
                T temp = list.get(s);
                list.set(s, list.get(n++));
                return temp;
            }
        };
    }

    public static <T> T[] shuffle(T[] array) {
        // Fisher-Yates algorithm
        Random random = new Random();
        for (int n = array.length; n > 1;) {
            int k = random.nextInt(n--);
            T temp = array[n];
            array[n] = array[k];
            array[k] = temp;
        }
        return array;
    }

    public static <T> Iterable<T> sort(Iterable<T> iter) {
        return sorted(iter);
    }

    public static <E> Iterable<E> sorted(Iterable<E> iter) {
        ArrayList<E> list = new ArrayList<E>();
        for (E e : iter)
            list.add(e);
        Collections.sort(list, null);
        return list;
    }

    /**
     * Iterate over all elements of <code>iterable</code>, except the first one.
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
