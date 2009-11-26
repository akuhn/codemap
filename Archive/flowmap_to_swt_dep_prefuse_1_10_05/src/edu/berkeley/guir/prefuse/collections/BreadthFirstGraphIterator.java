package edu.berkeley.guir.prefuse.collections;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Set;

import edu.berkeley.guir.prefuse.graph.Node;

/**
 * Iterates over graph nodes in a breadth-first manner.
 * 
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> - prefuse(AT)jheer.org
 */
public class BreadthFirstGraphIterator implements Iterator {

	private Set m_visited = new HashSet();
	private LinkedList m_queue  = new LinkedList();

	public BreadthFirstGraphIterator(Node n) {
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
		
		Node n = (Node)m_queue.removeFirst();
		m_visited.add(n);
		Iterator iter = n.getNeighbors();
		while ( iter.hasNext() ) {
			Node c = (Node)iter.next();
			if ( !m_visited.contains(c) ) {
				m_queue.add(c);
			}
		}
		return n;
	} //

} // end of class BreadthFirstIterator
