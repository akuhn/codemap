package edu.berkeley.guir.prefuse.graph.event;

import java.util.EventListener;

import edu.berkeley.guir.prefuse.event.EventMulticaster;
import edu.berkeley.guir.prefuse.graph.Edge;
import edu.berkeley.guir.prefuse.graph.Graph;
import edu.berkeley.guir.prefuse.graph.Node;

/**
 * Manages listeners for graph modification events.
 * 
 * @author newbergr
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public class GraphEventMulticaster extends EventMulticaster 
    implements GraphEventListener
{
    /**
     * @see edu.berkeley.guir.prefuse.graph.event.GraphEventListener#nodeAdded(edu.berkeley.guir.prefuse.graph.Graph, edu.berkeley.guir.prefuse.graph.Node)
     */
	public void nodeAdded(Graph g, Node n) {
		((GraphEventListener) a).nodeAdded(g,n);
		((GraphEventListener) b).nodeAdded(g,n);
	} //

    /**
     * @see edu.berkeley.guir.prefuse.graph.event.GraphEventListener#nodeRemoved(edu.berkeley.guir.prefuse.graph.Graph, edu.berkeley.guir.prefuse.graph.Node)
     */
	public void nodeRemoved(Graph g, Node n) {
		((GraphEventListener) a).nodeRemoved(g,n);
		((GraphEventListener) b).nodeRemoved(g,n);
	} //

    /**
     * @see edu.berkeley.guir.prefuse.graph.event.GraphEventListener#nodeReplaced(edu.berkeley.guir.prefuse.graph.Graph, edu.berkeley.guir.prefuse.graph.Node, edu.berkeley.guir.prefuse.graph.Node)
     */
	public void nodeReplaced(Graph g, Node o, Node n) {
		((GraphEventListener) a).nodeReplaced(g,o,n);
		((GraphEventListener) b).nodeReplaced(g,o,n);		
	} //

    /**
     * @see edu.berkeley.guir.prefuse.graph.event.GraphEventListener#edgeAdded(edu.berkeley.guir.prefuse.graph.Graph, edu.berkeley.guir.prefuse.graph.Edge)
     */
	public void edgeAdded(Graph g, Edge e) {
		((GraphEventListener) a).edgeAdded(g,e);
		((GraphEventListener) b).edgeAdded(g,e);
	} //

    /**
     * @see edu.berkeley.guir.prefuse.graph.event.GraphEventListener#edgeRemoved(edu.berkeley.guir.prefuse.graph.Graph, edu.berkeley.guir.prefuse.graph.Edge)
     */
	public void edgeRemoved(Graph g, Edge e) {
		((GraphEventListener) a).edgeRemoved(g,e);
		((GraphEventListener) b).edgeRemoved(g,e);
	} //

    /**
     * @see edu.berkeley.guir.prefuse.graph.event.GraphEventListener#edgeReplaced(edu.berkeley.guir.prefuse.graph.Graph, edu.berkeley.guir.prefuse.graph.Edge, edu.berkeley.guir.prefuse.graph.Edge)
     */
	public void edgeReplaced(Graph g, Edge o, Edge n) {
		((GraphEventListener) a).edgeReplaced(g,o,n);
		((GraphEventListener) b).edgeReplaced(g,o,n);		
	} //

	public static GraphEventListener add(GraphEventListener a, GraphEventListener b) {
		return (GraphEventListener) addInternal(a, b);
	} //

	public static GraphEventListener remove(GraphEventListener a, GraphEventListener b) {
		return (GraphEventListener) removeInternal(a, b);
	} //

	/** 
	 * Returns the resulting multicast listener from adding listener-a
	 * and listener-b together.  
	 * If listener-a is null, it returns listener-b;  
	 * If listener-b is null, it returns listener-a
	 * If neither are null, then it creates and returns
	 * a new AWTEventMulticaster instance which chains a with b.
	 * @param a event listener-a
	 * @param b event listener-b
	 */
	protected static EventListener addInternal(
		EventListener a,
		EventListener b) {
		if (a == null)
			return b;
		if (b == null)
			return a;
		return new GraphEventMulticaster(a, b);
	} //

	protected EventListener remove(EventListener oldl) {
		if (oldl == a)
			return b;
		if (oldl == b)
			return a;
		EventListener a2 = removeInternal(a, oldl);
		EventListener b2 = removeInternal(b, oldl);
		if (a2 == a && b2 == b) {
			return this; // it's not here
		}
		return addInternal(a2, b2);
	} //
	
	protected GraphEventMulticaster(EventListener a, EventListener b) {
		super(a,b);
	} //

} // end of class PrefuseEventMulticaster
