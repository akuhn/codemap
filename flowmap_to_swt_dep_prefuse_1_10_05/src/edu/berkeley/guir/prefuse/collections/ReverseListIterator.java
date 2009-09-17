package edu.berkeley.guir.prefuse.collections;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Iterator that traverses a list in reverse.
 * 
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public class ReverseListIterator implements Iterator {

	ListIterator m_iter;

	/**
	 * Constructor.
	 * @param list the list to traverse in reverse
	 */
	public ReverseListIterator(List list) {
		m_iter = list.listIterator();
		// we shouldn't have to do this... but things weren't working properly
		// when attempting to use the previous() method right off the bat.
		while ( m_iter.hasNext() ) { m_iter.next(); }	
	} //

	/**
	 * @see java.util.Iterator#remove()
	 */
	public void remove() {
		throw new UnsupportedOperationException("Remove not supported");
	} //

	/**
	 * @see java.util.Iterator#hasNext()
	 */
	public boolean hasNext() {
		return m_iter.hasPrevious();
	} //

	/**
	 * @see java.util.Iterator#next()
	 */
	public Object next() {		
		return m_iter.previous();
	} //

} // end of class ReverseListIterator
