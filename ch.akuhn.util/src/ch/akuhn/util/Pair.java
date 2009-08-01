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

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A generic class for pairs.
 * 
 */
public class Pair<A,B> {

    /**
     * Iterate over all consecutive pairs of <tt>elements</tt>.
     * 
     * @return if <tt>elements</tt> has less than two elements, the returned
     *         iterable is empty.
     */
    public static final <E> Iterable<Pair<E,E>> cons(final Iterable<E> iterable) {
        return new Iterable<Pair<E,E>>() {
            public Iterator<Pair<E,E>> iterator() {
                return new Iterator<Pair<E,E>>() {
                    private final Iterator<E> it = iterable.iterator();
                    private E prev = it.hasNext() ? it.next() : null;

                    public boolean hasNext() {
                        return it.hasNext();
                    }

                    public Pair<E,E> next() {
                        if (!hasNext()) throw new NoSuchElementException();
                        return Pair.of(prev, prev = it.next());
                    }

                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
    }
    private static boolean equals(Object x, Object y) {
        return (x == null && y == null) || (x != null && x.equals(y));
    }

    public static <A,B> Pair<A,B> of(A a, B b) {
        return new Pair<A,B>(a, b);
    }

    public static final <A,B> Iterable<Pair<A,B>> zip(final Iterable<A> fst, final Iterable<B> snd) {
        return new Iterable<Pair<A,B>>() {
            public Iterator<Pair<A,B>> iterator() {
                return new Iterator<Pair<A,B>>() {
                    private final Iterator<A> a = fst.iterator();
                    private final Iterator<B> b = snd.iterator();

                    public boolean hasNext() {
                        return a.hasNext() && b.hasNext();
                    }

                    public Pair<A,B> next() {
                        if (!hasNext()) throw new NoSuchElementException();
                        return Pair.of(a.next(), b.next());
                    }

                    public void remove() {
                        a.remove();
                        b.remove();
                    }
                };
            }
        };
    }

    public final A fst;

    public final B snd;

    public Pair(A fst, B snd) {
        this.fst = fst;
        this.snd = snd;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Pair<?,?> && equals(fst, ((Pair<?,?>) other).fst) && equals(snd, ((Pair<?,?>) other).snd);
    }

    @Override
    public int hashCode() {
        if (fst == null) return (snd == null) ? 0 : snd.hashCode() + 1;
        else if (snd == null) return fst.hashCode() + 2;
        else return fst.hashCode() * 17 + snd.hashCode();
    }

    @Override
    public String toString() {
        return "Pair[" + fst + "," + snd + "]";
    }

}
