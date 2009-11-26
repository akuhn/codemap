package edu.berkeley.guir.prefuse.collections;

import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.berkeley.guir.prefuse.graph.Edge;
import edu.berkeley.guir.prefuse.graph.TreeNode;

/**
 * Provided an iterator over nodes, this class will iterate over all
 * adjacent edges. Each adjacent edge is returned exactly once in the
 * iteration.
 * 
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public class TreeEdgeIterator implements Iterator {

	private Iterator m_nodeIterator;
	private Iterator m_edgeIterator;
	private TreeNode m_curNode;
	private Edge     m_next;

	/**
	 * Constructor.
	 * @param nodeIterator an iterator over nodes
	 */
	public TreeEdgeIterator(Iterator nodeIterator) {
        m_nodeIterator = nodeIterator;
		if ( nodeIterator.hasNext() ) {
			m_curNode = (TreeNode)nodeIterator.next();
			m_edgeIterator = m_curNode.getChildEdges(); 
		}
		m_next = findNext();
	} //

	/**
	 * Not currently supported. 
	 * TODO: Support in future versions?
	 * @see java.util.Iterator#remove()
	 */
	public void remove() {
		throw new UnsupportedOperationException();
	} //

	/**
	 * @see java.util.Iterator#hasNext()
	 */
	public boolean hasNext() {
		return (m_next != null);
	} //

	/**
	 * @see java.util.Iterator#next()
	 */
	public Object next() {
		if ( m_next == null )
			throw new NoSuchElementException("No next item in iterator");
		Edge retval = m_next;
		m_next = findNext();
		return retval;
	} //
	
	private Edge findNext() {
		while ( true ) {
			if ( m_edgeIterator != null && m_edgeIterator.hasNext() ) {
				return (Edge)m_edgeIterator.next();
			} else if ( m_nodeIterator.hasNext() ) {
				m_curNode = (TreeNode)m_nodeIterator.next();
				m_edgeIterator = m_curNode.getChildEdges();
			} else {
				m_curNode = null;
				m_nodeIterator = null;
				m_edgeIterator = null;
				return null;
			}
		}
	} //

} // end of class TreeEdgeIterator
