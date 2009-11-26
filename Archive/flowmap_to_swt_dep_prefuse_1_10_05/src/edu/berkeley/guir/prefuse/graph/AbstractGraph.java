package edu.berkeley.guir.prefuse.graph;

import edu.berkeley.guir.prefuse.graph.event.GraphEventListener;
import edu.berkeley.guir.prefuse.graph.event.GraphEventMulticaster;

/**
 * Skeletal graph implementation handling graph listener methods. 
 * 
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public abstract class AbstractGraph implements Graph {

	protected GraphEventListener m_graphListener = null;
	
	/**
	 * Add a graph event listener.
	 * @param gl the listener to add.
	 */
	public void addGraphEventListener(GraphEventListener gl) {
		m_graphListener = GraphEventMulticaster.add(m_graphListener, gl);
	} //
  	
	/**
	 * Remove a focus listener.
	 * @param gl the listener to remove.
	 */
	public void removeGraphEventListener(GraphEventListener gl) {
		m_graphListener = GraphEventMulticaster.remove(m_graphListener, gl);
	} //
	
	protected void fireNodeAdded(Node n) {
		if ( m_graphListener != null )
			m_graphListener.nodeAdded(this, n);
	} //

	protected void fireNodeRemoved(Node n) {
		if ( m_graphListener != null )
			m_graphListener.nodeRemoved(this, n);
	} //
	
	protected void fireNodeReplaced(Node o, Node n) {
		if ( m_graphListener != null )
			m_graphListener.nodeReplaced(this,o,n);
	} //
	
	protected void fireEdgeAdded(Edge e) {
		if ( m_graphListener != null )
			m_graphListener.edgeAdded(this, e);
	} //
	
	protected void fireEdgeRemoved(Edge e) {
		if ( m_graphListener != null )
			m_graphListener.edgeRemoved(this, e);
	} //
	
	protected void fireEdgeReplaced(Edge o, Edge n) {
		if ( m_graphListener != null )
			m_graphListener.edgeReplaced(this, o,n);
	} //

} // end of class AbstractGraph
