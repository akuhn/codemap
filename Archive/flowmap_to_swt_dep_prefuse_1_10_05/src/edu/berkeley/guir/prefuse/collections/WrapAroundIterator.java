package edu.berkeley.guir.prefuse.collections;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Iterator for traversing a list starting at an arbitray position in the
 * list, and then wrapping around back to beginning of the list as
 * necessary.
 * 
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public class WrapAroundIterator implements Iterator {

	private int      m_cur, m_count, m_size;
	private List     m_items;

	/**
	 * Constructor.
	 * @param items the list to iterator over
	 * @param start the starting position of the iterator in the list
	 */
	public WrapAroundIterator(List items, int start) {
		this(items, start, items.size()-1);
	} //

	/**
	 * Constructor.
	 * @param items the list to iterate over
	 * @param start the starting position of the iterator in the list
	 * @param limit the maximum index that should be visited by the iterator
	 */
	public WrapAroundIterator(List items, int start, int limit) {
		if ( start > limit ) { 
			throw new IllegalArgumentException(); }
		m_items = items;
		m_cur   = start;
		m_count = 0;
		m_size  = limit+1;
	} //

	/**
	 * @see java.util.Iterator#hasNext()
	 */
	public boolean hasNext() {
		return ( m_count < m_size );
	} //

	/**
	 * @see java.util.Iterator#next()
	 */
	public Object next() {
		if ( m_count >= m_size ) {
			throw new NoSuchElementException("Iterator has no next element.");
		} else {
			int idx = m_cur;
			m_cur = ++m_cur % m_size;
			m_count++;
			return m_items.get(idx);
		}
	} //

	/**
	 * @see java.util.Iterator#remove()
	 */
	public void remove() {
		throw new UnsupportedOperationException("Remove not supported");
	} //

} // end of class WrapAroundIterator
