package ch.akuhn.util;

import java.util.Enumeration;
import java.util.Iterator;

public interface IterableIterator<E> extends Iterable<E>, Iterator<E>, Enumeration<E> {

}
