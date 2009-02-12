package ch.akuhn.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

public class Get {

    public static <E> E element(int index, Iterator<E> iter) {
        for (int n = 0; n < index; n++) iter.next();
        return iter.next();
    }

    public static <E> E element(int index, Iterable<E> elements) {
        return Get.element(index, elements.iterator());
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

    public static <E> List<E> sorted(Iterable<? extends E> iter) {
        ArrayList<E> list = new ArrayList<E>();
        for (E e : iter)
            list.add(e);
        Collections.sort(list, null);
        return list;
    }
    
    public static <T> Iterable<T> shuffle(final Iterable<T> iterable) {
        return new Providable<T>() {
            private Random random;
            private List<T> list;
            private int len, n;
            public void initialize() {
                random = new Random();
                list = As.list(iterable);
                len = list.size();
                n = 0;
            }
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

    public static <T> Iterable<T> first(final int num, final Iterable<T> iter) {
        return new Providable<T>() {
            private int count;
            private Iterator<T> it;
            public void initialize() {
                count = num;
                it = iter.iterator();
            }
            public T provide() {
                return (count-- > 0 && it.hasNext()) ? it.next() : this.done();
            }
        };
    }

}
