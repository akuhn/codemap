package edu.berkeley.guir.prefuse.graph;

import java.util.Iterator;

/**
 * An interface representing a node in a graph.
 *
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public interface Node extends Entity {

    /**
     * Adds an edge connecting this node to another node. The edge is added to
     * the end of this node's internal list of edges.
     * @param e the Edge to add
     * @return true if the edge was added, false if the edge connects to a
     *  node that is alrady a neighbor of this node.
     */
    public boolean addEdge(Edge e);
    
    /**
     * Adds an edge connecting this node to another node at the specified 
     * index.
     * @param idx the index at which to insert the edge
     * @param e the Edge to add
     * @return true if the edge was added, false if the edge connects to a
     *  node that is alrady a neighbor of this node.
     */
    public boolean addEdge(int idx, Edge e);
    
    /**
     * Returns the edge connected to the given neighbor node.
     * @param n the neighbor node for which to retrieve the edge
     * @return the requested Edge
     * @throws NoSuchElementException if the given node is not a neighbor of
     *  this node.
     */
    public Edge getEdge(Node n);
    
    /**
     * Returns the edge at the specified index.
     * @param idx the index at which to retrieve the edge
     * @return the requested Edge
     */
    public Edge getEdge(int idx);
    
    /**
     * Returns the number of edges adjacent to this node.
     * @return the number of adjacent edges. This is the same as the number
     *  of neighbor nodes connected to this node.
     */
    public int getEdgeCount();
    
    /**
     * Returns an iterator over all edges adjacent to this node.
     * @return an iterator over all adjacent edges.
     */
    public Iterator getEdges();
    
    /**
     * Returns the index, or position, of an incident edge. Returns -1 if the
     * input edge is not incident on this node.
     * @param e the edge to find the index of
     * @return the edge index, or -1 if this edge is not incident
     */
    public int getIndex(Edge e);
    
    /**
     * Returns the index, or position, of a neighbor node. Returns -1 if the
     * input node is not a neighbor of this node.
     * @param n the node to find the index of
     * @return the node index, or -1 if this node is not a neighbor
     */
    public int getIndex(Node n);
    
    /**
     * Returns the neighbor of this node at the given index.
     * @param idx the index of the neighbor in the neighbor list.
     * @return DefaultNode the DefaultNode at the specified position in the list of
     *  neighbors
     */
    public Node getNeighbor(int idx);
    
    /**
     * Returns an iterator over all neighbor nodes of this node.
     * @return an iterator over this node's neighbors.
     */
    public Iterator getNeighbors();

    /**
     * Indicates if a given edge is not only incident on this node
     * but stored in this node's internal list of edges.
     * @param e the edge to check for incidence
     * @return true if the edge is incident on this node and stored in this
     *  node's internal list of edges, false otherwise.
     */
    public boolean isIncidentEdge(Edge e);
    
    /**
     * Indicates if a given node is a neighbor of this one.
     * @param n the node to check as a neighbor
     * @return true if the node is a neighbor, false otherwise
     */
    public boolean isNeighbor(Node n);
    
    /**
     * Removes all edges incident on this node.
     */
    public void removeAllNeighbors();
    
    /**
     * Remove the given edge as an incident edge on this node
     * @param e the edge to remove
     * @return true if the edge was found and removed, false otherwise
     */
    public boolean removeEdge(Edge e);
    
    /**
     * Remove the incident edge at the specified index.
     * @param idx the index at which to remove an edge
     */
    public Edge removeEdge(int idx);
    
    /**
     * Remove the given node as a neighbor of this node. The edge connecting
     * the nodes is also removed.
     * @param n the node to remove
     * @return true if the node was found and removed, false otherwise
     */
    public boolean removeNeighbor(Node n);
    
    /**
     * Remove the neighbor node at the specified index.
     * @param idx the index at which to remove a node
     */
    public Node removeNeighbor(int idx);
    
} // end of interface Node
