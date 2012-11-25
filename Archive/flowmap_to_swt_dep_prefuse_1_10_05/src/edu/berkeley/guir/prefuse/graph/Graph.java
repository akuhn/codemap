package edu.berkeley.guir.prefuse.graph;

import java.util.Iterator;

import edu.berkeley.guir.prefuse.graph.event.GraphEventListener;

/**
 * Interface for representing a graph
 * 
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public interface Graph {

	/**
	 * Return the number of nodes in the graph.
	 * @return the number of nodes.
	 */
	public int getNodeCount();
	
	/**
	 * Return the number of edges in the graph.
	 * @return the number of edges.
	 */
	public int getEdgeCount();

	/**
	 * Returns an iterator over all the nodes in the graph.
	 * @return an iterator over all nodes.
	 */
	public Iterator getNodes();
	
	/**
	 * Returns an iterator over all the edges in the graph.
	 * @return an iterator over all edges.
	 */
	public Iterator getEdges();
	
	/**
	 * Indicates if the graph contains directed of undirected edges.
	 * @return true if directed, false if undirected.
	 */
	public boolean isDirected();
    
    /**
     * Adds a node to this graph.
     * @param n the node to add to the graph.
     * @return true if the node was successfully added, false otherwise
     */
    public boolean addNode(Node n);
    
    /**
     * Adds an edge to this graph.
     * @param e the edge to add to the graph.
     * @return true if the node was successfully added, false otherwise
     */
    public boolean addEdge(Edge e);
    
    /**
     * Remove a node from this graph
     * @param n the node to remove from the graph.
     * @return true if the node was in the graph and removed, false otherwise
     */
    public boolean removeNode(Node n);
    
    /**
     * Remove an edge from this graph
     * @param e the edge to remove from the graph.
     * @return true if the edge was in the graph and removed, false otherwise
     */
    public boolean removeEdge(Edge e);
	
    /**
     * Replaces an existing node in the graph with the provided
     * new node. The new node should be free of edges. The previous
     * node will stripped of all edges, and these edges will be
     * reconnected to the new node.
     * @param prev the existing node in the graph
     * @param next the node to swap into the graph
     * @return true if the replace is successful, false if either prev
     *  is not in the graph or next already has edges attached.
     */
    public boolean replaceNode(Node prev, Node next);
    
    /**
     * Replaces an existing edge in the graph with the provided
     * new edge. The previous edge will have its adjacent node
     * references set to null. The new edge will have its adjacent
     * node references overwritten.
     * @param prev the existing edge in the graph
     * @param next the edge to swap into the graph
     * @return true if the replace is successful, false if prev
     *  is not in the graph or the edge directionalities are not
     *  the same.
     */
    public boolean replaceEdge(Edge prev, Edge next);
    
	/**
	 * Indicates if the graph contains the specified node.
	 * @param n the node to check for graph membership.
	 * @return true if the node is in the graph, false otherwise.
	 */
	public boolean contains(Node n);
    
    /**
     * Indicates if the graph contains the specified edge.
     * @param e the dge to check for graph membership.
     * @return true if the edge is in the graph, false otherwise.
     */
    public boolean contains(Edge e);

    /**
     * Adds a graph listener to monitor changes on this graph
     * @param gl the GraphListener to add
     */
	public void addGraphEventListener(GraphEventListener gl);
    
    /**
     * Removes a registered graph listener from this graph 
     * @param gl the GraphListener to remove
     */
	public void removeGraphEventListener(GraphEventListener gl);

} // end of interface Graph
