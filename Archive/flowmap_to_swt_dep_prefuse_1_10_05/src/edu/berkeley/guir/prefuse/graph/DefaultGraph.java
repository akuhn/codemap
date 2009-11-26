package edu.berkeley.guir.prefuse.graph;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;

/**
 * A straight-forward representation of a general graph.
 * 
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public class DefaultGraph extends AbstractGraph {

	protected LinkedHashSet m_nodes;
    protected LinkedHashSet m_edges;
	protected boolean m_directed = false;

	/**
	 * Constructor. Takes in a collection of nodes contained in
	 * the graph.
	 * @param nodes a collection of the nodes of the graph.
	 */
	public DefaultGraph(Collection nodes, boolean directed) {
		this(directed);
		m_nodes.addAll(nodes);
        Iterator niter = m_nodes.iterator();
        while ( niter.hasNext() ) {
            Node n = (Node)niter.next();
            Iterator eiter = n.getEdges();
            while ( eiter.hasNext() ) {
                Edge e = (Edge)eiter.next();
                if ( e.isDirected() != directed ) {
                    throw new IllegalStateException(
                        "Directedness of edge and graph differ");
                }
                m_edges.add(e);
            }
        }
	} //
	
	/**
	 * Constructor. Takes in a collection of nodes contained in
	 * the graph.
	 * @param nodes a collection of the nodes of the graph.
	 */
	public DefaultGraph(Collection nodes) {
		this(nodes, false);
	} //	
	
	/**
	 * Constructor. Creates an empty graph undirected graph.
	 */
	public DefaultGraph() {
		this(false);	
	} //

	/**
	 * Constructor. Creates an empty graph.
     * @param directed indicates if this graph is directed or undirected
	 */
	public DefaultGraph(boolean directed) {
		m_directed = directed;
		m_nodes = new LinkedHashSet();
        m_edges = new LinkedHashSet();	
	} //

    /**
     * Re-initializes this graph, allowing for reuse. All nodes and edges
     * are removed from this graph and the edge directedness is reset. 
     * This call does not notify any listeners of the change!
     */
    public void reinit(boolean directed) {
        m_nodes.clear();
        m_edges.clear();
        m_directed = directed;
    } //
    
	/**
	 * Add a node to this graph.
	 * @param n the node to add
	 */
	public boolean addNode(Node n) {
        if ( m_nodes.contains(n) ) {
            return false;
        } else {
            m_nodes.add(n);
            fireNodeAdded(n);
            return true;
        }
	} //
	
	/**
	 * Remove a node from the graph. For undirected graphs this operation
     * runs in time linear in the number of neighbors of <code>n</code>,
     * for directed graphs the running time is linear in the total number
     * of edges in the graph, as inlinks must be resolved.
	 * @param n the node the remove from the graph
     * @return true if the node is succesfully removed, false if the node
     *  is not in this graph.
	 */
	public boolean removeNode(Node n) {
        if ( !m_nodes.contains(n) )
            return false;
        
		int nN = n.getEdgeCount();
		for ( int i = 0; i < nN; i++ ) {
			Edge e = n.removeEdge(0);
			if ( !e.isDirected() ) {
                Node n2 = (Node)e.getAdjacentNode(n);				
				n2.removeNeighbor(n);
			}
            m_edges.remove(e);
			fireEdgeRemoved(e);
		}
		m_nodes.remove(n);
		
		// remove any 'inlinks' to the removed node
		if ( m_directed ) {
			Iterator iter = m_nodes.iterator();
			while ( iter.hasNext() ) {
				Node n2 = (Node)iter.next();
				int nidx = n2.getIndex(n);
				if ( nidx > -1 ) {
					Edge e = n2.removeEdge(nidx);
                    m_edges.remove(e);
					fireEdgeRemoved(e);
				}
			}
		}
		fireNodeRemoved(n);
        return true;
	} //

	/**
	 * Add an edge to this graph between the given nodes. The default
     *  edge representation is used.
	 * @param u the first node in the edge
	 * @param v the second node in the edge
	 */
	public boolean addEdge(Node u, Node v) {
		return addEdge(new DefaultEdge(u,v,m_directed));
	} //
	
	/**
	 * Add an edge to this graph.
	 * @param e the <code>Edge</code> to add
	 */
	public boolean addEdge(Edge e) {
		if ( m_directed ^ e.isDirected() ) {
			throw new IllegalStateException(
				"Directedness of edge and graph differ");
		}
        Node n1 = (Node)e.getFirstNode();
        Node n2 = (Node)e.getSecondNode();
        if ( m_edges.contains(e) || n1.isNeighbor(n2) || 
             (!m_directed && n2.isNeighbor(n1)) ) {
            return false;
        }
		n1.addEdge(e);
		if ( !m_directed ) {
			n2.addEdge(e);
		}
        m_edges.add(e);
		fireEdgeAdded(e);
        return true;
	} //

	/**
	 * Remove an edge from the graph
	 * @param e the <code>Edge</code> to remove
	 */
	public boolean removeEdge(Edge e) {
		if ( m_directed ^ e.isDirected() ) {
			throw new IllegalStateException(
				"Directedness of edge and graph differ");
		}
		Node n1 = (Node)e.getFirstNode();
		Node n2 = (Node)e.getSecondNode();
		if ( !n1.isNeighbor(n2) || (!m_directed && !n2.isNeighbor(n1)) )
			return false; // both nodes are not in the graph
		n1.removeNeighbor(n2);
		if ( !m_directed )
			n2.removeNeighbor(n1);
		fireEdgeRemoved(e);
        return true;
	} //

    /**
     * @see edu.berkeley.guir.prefuse.graph.Graph#replaceNode(edu.berkeley.guir.prefuse.graph.Node, edu.berkeley.guir.prefuse.graph.Node)
     */
    public boolean replaceNode(Node prev, Node next) {
        if ( next.getEdgeCount()>0 || !contains(prev) || contains(next) )
            return false;
        
        Iterator iter = prev.getEdges();
        while ( iter.hasNext() ) {
            Edge e = (Edge)iter.next();
            if ( e.getFirstNode() == prev )
                e.setFirstNode(next);
            else
                e.setSecondNode(next);
            next.addEdge(e);
        }
        prev.removeAllNeighbors();
        
        // update any 'inlinks'
        if ( m_directed ) {
            iter = m_nodes.iterator();
            while ( iter.hasNext() ) {
                Node n2 = (Node)iter.next();
                int nidx = n2.getIndex(prev);
                if ( nidx > -1 ) {
                    Edge e = n2.getEdge(nidx);
                    if ( e.getFirstNode() == prev )
                        e.setFirstNode(next);
                    else
                        e.setSecondNode(next);
                }
            }
        }
        fireNodeReplaced(prev, next);
        return true;
    } //

    /**
     * @see edu.berkeley.guir.prefuse.graph.Graph#replaceEdge(edu.berkeley.guir.prefuse.graph.Edge, edu.berkeley.guir.prefuse.graph.Edge)
     */
    public boolean replaceEdge(Edge prev, Edge next) {
        boolean rv = m_edges.contains(prev) && !m_edges.contains(next)
                     && next.isDirected()==m_directed;
        if ( !rv ) return false;
        Node p1 = prev.getFirstNode(), p2 = prev.getSecondNode();
        Node n1 = next.getFirstNode(), n2 = next.getSecondNode();
        if ( m_directed ) {
            rv = p1==n1 && p2==n2;
        } else {
            rv = (p1==n1 && p2==n2) || (p1==n2 && p2==n1);     
        }
        if ( rv ) {
            int idx = p1.getIndex(prev);
            p1.removeEdge(idx);
            p1.addEdge(idx, next);
            if ( !m_directed ) {
                idx = p2.getIndex(prev);
                p2.removeEdge(idx);
                p2.addEdge(idx, next);
            }
            fireEdgeReplaced(prev, next);
            return true;
        }
        return false;
    } //
    
	/**
	 * @see edu.berkeley.guir.prefuse.graph.Graph#getNodeCount()
	 */
	public int getNodeCount() {
		return m_nodes.size();
	} //

	/**
	 * @see edu.berkeley.guir.prefuse.graph.Graph#getEdgeCount()
	 */
	public int getEdgeCount() {
		return m_edges.size();
	} //

	/**
	 * @see edu.berkeley.guir.prefuse.graph.Graph#getNodes()
	 */
	public Iterator getNodes() {
		return m_nodes.iterator();
	} //

	/**
	 * @see edu.berkeley.guir.prefuse.graph.Graph#getEdges()
	 */
	public Iterator getEdges() {
		return m_edges.iterator();
	} //

	/**
	 * @see edu.berkeley.guir.prefuse.graph.Graph#isDirected()
	 */
	public boolean isDirected() {
		return m_directed;
	} //

	/**
	 * @see edu.berkeley.guir.prefuse.graph.Graph#contains(edu.berkeley.guir.prefuse.graph.Node)
	 */
	public boolean contains(Node n) {
		return m_nodes.contains(n);
	} //
    
    /**
     * @see edu.berkeley.guir.prefuse.graph.Graph#contains(edu.berkeley.guir.prefuse.graph.Edge)
     */
    public boolean contains(Edge e) {
        return m_edges.contains(e);
    } //

} // end of class DefaultGraph
