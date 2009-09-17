package edu.berkeley.guir.prefuse.graph;

/**
 * Interface representing an edge connecting two nodes in a graph.
 *
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public interface Edge extends Entity {

    /**
     * Indicates if this edge is directed or undirected.
     * @return true if the edge is directed, false if undirected.
     */
    public boolean isDirected();
    
    /**
     * Indicates if this is a tree edge, that is, if this edge helps
     * define part of a tree structure. This holds true if both incident
     * edges are instances of DefaultTreeNode and this edge defines a 
     * parent-child relationship between the two nodes.
     * @return true if this is a tree-edge, false otherwise.
     */
    public boolean isTreeEdge();
    
    /**
     * Indicates if this edge is incident on a given node
     * @param n the node to check
     * @return true if this edge is incident on the node, false otherwise.
     */
    public boolean isIncident(Node n);
    
    /**
     * Returns the first node in the edge (the source node for
     * directed edges).
     * @return the first (source) node
     */
    public Node getFirstNode();
    
    /**
     * Returns the second node in the edge (the target node for
     * directed edges).
     * @return the second (target) node
     */
    public Node getSecondNode();
    
    /**
     * Sets the first node in the edge (the source node for
     *  directed edges).
     * @param n the new first (source) node.
     */
    public void setFirstNode(Node n);
    
    /**
     * Sets the second node in the edge (the target node for
     * directed edges).
     * @param n the new second (target) node.
     */
    public void setSecondNode(Node n);
    
    /**
     * Returns the node in this <code>DefaultEdge</code> adjacent to the 
     * provided node, or null if the provided node is not in this edge.
     * @param n a Node that should be in this DefaultEdge
     * @return the Node adjacent to the provided Node
     */
    public Node getAdjacentNode(Node n);
    
} // end of interface Edge
