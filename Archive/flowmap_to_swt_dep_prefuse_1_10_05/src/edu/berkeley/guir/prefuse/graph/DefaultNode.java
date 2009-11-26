package edu.berkeley.guir.prefuse.graph;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import edu.berkeley.guir.prefuse.collections.NodeIterator;

/**
 * Represents a node in a graph.
 * 
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public class DefaultNode extends DefaultEntity implements Node {
	
	protected List m_edges;
	
	/**
	 * Default constructor. Creates a new unconnected node.
	 */
	public DefaultNode() {
	    m_edges = new ArrayList(3);
	} //
	
    /**
     * Adds an edge connecting this node to another node. The edge is added to
     * the end of this node's internal list of edges.
     * @param e the Edge to add
     * @return true if the edge was added, false if the edge connects to a
     *  node that is alrady a neighbor of this node.
     * @see edu.berkeley.guir.prefuse.graph.Node#addEdge(edu.berkeley.guir.prefuse.graph.Edge)
     */
    public boolean addEdge(Edge e) {       
        return addEdge(m_edges.size(), e);
    } //
    
    /**
     * Adds an edge connecting this node to another node at the specified 
     * index.
     * @param idx the index at which to insert the edge
     * @param e the Edge to add
     * @return true if the edge was added, false if the edge connects to a
     *  node that is alrady a neighbor of this node.
     * @see edu.berkeley.guir.prefuse.graph.Node#addEdge(int, edu.berkeley.guir.prefuse.graph.Edge)
     */
    public boolean addEdge(int idx, Edge e) {
        if ( e.isDirected() && this != e.getFirstNode() ) {
            throw new IllegalArgumentException("Directed edges must have the "
                    + "source as the first node in the Edge.");
        }
        Node n = e.getAdjacentNode(this);
        if ( n == null ) {
            throw new IllegalArgumentException(
                    "The Edge must be incident on this Node.");
        }
        if ( isNeighbor(n) )
            return false;     
        m_edges.add(idx,e);
        return true;
    } //
    
    /**
     * Returns the edge at the specified index.
     * @param idx the index at which to retrieve the edge
     * @return the requested Edge
     * @see edu.berkeley.guir.prefuse.graph.Node#getEdge(int)
     */
    public Edge getEdge(int idx) {
        return (Edge)m_edges.get(idx);
    } //
    
    /**
     * Returns the edge connected to the given neighbor node.
     * @param n the neighbor node for which to retrieve the edge
     * @return the requested Edge
     * @throws NoSuchElementException if the given node is not a neighbor of
     *  this node.
     * @see edu.berkeley.guir.prefuse.graph.Node#getEdge(edu.berkeley.guir.prefuse.graph.Node)
     */
    public Edge getEdge(Node n) {
        for ( int i=0; i < m_edges.size(); i++ ) {
            Edge e = (Edge)m_edges.get(i);
            if ( n == e.getAdjacentNode(this) )
                return e;
        }
        throw new NoSuchElementException();
    } //
    
    /**
     * Returns the number of edges adjacent to this node.
     * @return the number of adjacent edges. This is the same as the number
     *  of neighbor nodes connected to this node.
     * @see edu.berkeley.guir.prefuse.graph.Node#getEdgeCount()
     */
    public int getEdgeCount() {
        return m_edges.size();
    } //
    
    /**
     * Returns an iterator over all edges adjacent to this node.
     * @return an iterator over all adjacent edges.
     * @see edu.berkeley.guir.prefuse.graph.Node#getEdges()
     */
    public Iterator getEdges() {
        return m_edges.iterator();
    } //
    
    /**
     * Returns the index, or position, of an incident edge. Returns -1 if the
     * input edge is not incident on this node.
     * @param e the edge to find the index of
     * @return the edge index, or -1 if this edge is not incident
     */
    public int getIndex(Edge e) {
        return m_edges.indexOf(e);
    } //
    
    /**
     * Returns the index, or position, of a neighbor node. Returns -1 if the
     * input node is not a neighbor of this node.
     * @param n the node to find the index of
     * @return the node index, or -1 if this node is not a neighbor
     */
    public int getIndex(Node n) {
        for ( int i=0; i < m_edges.size(); i++ ) {
            if ( n == ((Edge)m_edges.get(i)).getAdjacentNode(this) )
                return i;
        }
        return -1;
    } //
    
    /**
     * Returns the i'th neighbor of this node.
     * @param idx the index of the neighbor in the neighbor list.
     * @return the neighbor node at the specified position
     */
    public Node getNeighbor(int idx) {
        return ((Edge)m_edges.get(idx)).getAdjacentNode(this);
    } //
    
    /**
     * Returns an iterator over all neighbor nodes of this node.
     * @return an iterator over this node's neighbors.
     */
    public Iterator getNeighbors() {
        return new NodeIterator(m_edges.iterator(), this);
    } //
    
    /**
     * Indicates if a given edge is not only incident on this node
     * but stored in this node's internal list of edges.
     * @param e the edge to check for incidence
     * @return true if the edge is incident on this node and stored in this
     *  node's internal list of edges, false otherwise.
     * @see edu.berkeley.guir.prefuse.graph.Node#isIncidentEdge(edu.berkeley.guir.prefuse.graph.Edge)
     */
    public boolean isIncidentEdge(Edge e) {
        return ( m_edges.indexOf(e) > -1 );
    } //
    
	/**
	 * Indicates if a given node is a neighbor of this one.
	 * @param n the node to check as a neighbor
	 * @return true if the node is a neighbor, false otherwise
	 */
	public boolean isNeighbor(Node n) {
		return ( getIndex(n) > -1 );
	} //
	
    /**
     * Removes all edges incident on this node.
     */
    public void removeAllNeighbors() {
        m_edges.clear();
    } //
    
    /**
     * Remove the given edge as an incident edge on this node
     * @param e the edge to remove
     * @return true if the edge was found and removed, false otherwise
     */
    public boolean removeEdge(Edge e) {
        int idx = m_edges.indexOf(e);
        return ( idx>-1 ? m_edges.remove(idx)!=null : false );
    } //
    
    /**
     * Remove the incident edge at the specified index.
     * @param idx the index at which to remove an edge
     */
    public Edge removeEdge(int idx) {
        return (Edge)m_edges.remove(idx);
    } //
    
	/**
	 * Remove the given node as a neighbor of this node. The edge connecting
     * the nodes is also removed.
	 * @param n the node to remove
     * @return true if the node was found and removed, false otherwise
	 */
	public boolean removeNeighbor(Node n) {
        for ( int i=0; i < m_edges.size(); i++ ) {
            if ( n == ((Edge)m_edges.get(i)).getAdjacentNode(this) )
                return m_edges.remove(i) != null;
        }
        return false;
	} //

	/**
	 * Remove the neighbor node at the specified index.
	 * @param idx the index at which to remove a node
	 */
	public Node removeNeighbor(int idx) {
        return ((Edge)m_edges.remove(idx)).getAdjacentNode(this);
	} //

} // end of class DefaultNode
