package edu.berkeley.guir.prefuse.collections;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

import edu.berkeley.guir.prefuse.graph.TreeNode;


/**
 * Iterates over tree nodes in a breadth-first manner.
 * 
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public class BreadthFirstTreeIterator implements Iterator {

	private LinkedList m_queue  = new LinkedList();

	public BreadthFirstTreeIterator(TreeNode n) {
		 m_queue.add(n);
	} //

	/**
	 * @see java.util.Iterator#remove()
	 */
	public void remove() {
		throw new UnsupportedOperationException();
	} //

	/**
	 * @see java.util.Iterator#hasNext()
	 */
	public boolean hasNext() {
		return !m_queue.isEmpty();
	} //

	/**
	 * @see java.util.Iterator#next()
	 */
	public Object next() {
		if ( m_queue.isEmpty() ) {
			throw new NoSuchElementException();
		}
		
		TreeNode n = (TreeNode)m_queue.removeFirst();
		Iterator iter = n.getChildren();
		while ( iter.hasNext() ) {
			TreeNode c = (TreeNode)iter.next();
			m_queue.add(c);
		}
		return n;
	} //

} // end of class BreadthFirstTreeIterator
