package edu.berkeley.guir.prefuse.graph.event;

import edu.berkeley.guir.prefuse.graph.Edge;
import edu.berkeley.guir.prefuse.graph.Graph;
import edu.berkeley.guir.prefuse.graph.Node;

/**
 * Adapter class to simplify creating GraphEventListener instances
 * 
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public class GraphEventAdapter implements GraphEventListener {

	/**
	 * @see edu.berkeley.guir.prefuse.graph.event.GraphEventListener#nodeAdded(edu.berkeley.guir.prefuse.graph.Graph, edu.berkeley.guir.prefuse.graph.Node)
	 */
	public void nodeAdded(Graph g, Node n) {} //

	/**
	 * @see edu.berkeley.guir.prefuse.graph.event.GraphEventListener#nodeRemoved(edu.berkeley.guir.prefuse.graph.Graph, edu.berkeley.guir.prefuse.graph.Node)
	 */
	public void nodeRemoved(Graph g, Node n) {} //

	/**
	 * @see edu.berkeley.guir.prefuse.graph.event.GraphEventListener#nodeReplaced(edu.berkeley.guir.prefuse.graph.Graph, edu.berkeley.guir.prefuse.graph.Node, edu.berkeley.guir.prefuse.graph.Node)
	 */
	public void nodeReplaced(Graph g, Node o, Node n) {} //

	/**
	 * @see edu.berkeley.guir.prefuse.graph.event.GraphEventListener#edgeAdded(edu.berkeley.guir.prefuse.graph.Graph, edu.berkeley.guir.prefuse.graph.Edge)
	 */
	public void edgeAdded(Graph g, Edge e) {} //

	/**
	 * @see edu.berkeley.guir.prefuse.graph.event.GraphEventListener#edgeRemoved(edu.berkeley.guir.prefuse.graph.Graph, edu.berkeley.guir.prefuse.graph.Edge)
	 */
	public void edgeRemoved(Graph g, Edge e) {} //
    
	/**
	 * @see edu.berkeley.guir.prefuse.graph.event.GraphEventListener#edgeReplaced(edu.berkeley.guir.prefuse.graph.Graph, edu.berkeley.guir.prefuse.graph.Edge, edu.berkeley.guir.prefuse.graph.Edge)
	 */
	public void edgeReplaced(Graph g, Edge o, Edge n) {} //

} // end of class GraphEventAdapter
