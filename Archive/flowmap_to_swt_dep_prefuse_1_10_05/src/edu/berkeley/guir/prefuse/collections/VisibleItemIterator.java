package edu.berkeley.guir.prefuse.collections;

import java.util.Iterator;
import java.util.List;

import edu.berkeley.guir.prefuse.VisualItem;

/**
 * Provides an iterator over only the currently visible items in a graph.
 * 
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public class VisibleItemIterator implements Iterator {

	private Iterator  m_iter;
	private VisualItem m_item;

	/**
	 * Constructor
	 * @param items the list of VisualItems over which to iterate
	 * @param reverse if true, will traverse list in reverse order
	 */
	public VisibleItemIterator(List items, boolean reverse) {
		if ( items.isEmpty() ) {
			m_item = null;
		} else {
			m_iter = ( reverse ? new ReverseListIterator(items) : items.iterator() );
			while ( m_iter.hasNext() && !(m_item=(VisualItem)m_iter.next()).isVisible() );
			if ( !m_item.isVisible() ) {
				m_item = null;
			}
		}
	} //

	/**
	 * @see java.util.Iterator#hasNext()
	 */
	public boolean hasNext() {
		return m_item != null;
	} //

	/**
	 * @see java.util.Iterator#next()
	 */
	public Object next() {
		if ( m_item != null ) {
			VisualItem retval = m_item;
			while ( m_iter.hasNext() && !(m_item=(VisualItem)m_iter.next()).isVisible() );
			if ( !m_iter.hasNext() && (m_item == retval || !m_item.isVisible()) ) {
				m_item = null;
			}
			return retval;
		} else {
			throw new IllegalStateException("Iterator has no next element.");
		} 
	} //

	/**
	 * @see java.util.Iterator#remove()
	 */
	public void remove() {
		throw new UnsupportedOperationException("Remove not supported.");
	} //

} // end of class VisibleItemIterator
