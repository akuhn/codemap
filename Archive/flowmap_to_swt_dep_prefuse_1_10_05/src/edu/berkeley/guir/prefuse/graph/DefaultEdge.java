package edu.berkeley.guir.prefuse.graph;

/**
 * Represents an edge in a graph.
 * 
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public class DefaultEdge extends DefaultEntity implements Edge {

	protected Node m_node1;
	protected Node m_node2;
	protected boolean m_directed;
	
	/**
	 * Constructor. Creates a new <i>undirected</i> edge.
	 * @param n1 the first node in the edge
	 * @param n2 the second node in the edge
	 */
	public DefaultEdge(Node n1, Node n2) {
		this(n1,n2,false);
	} //
	
	/**
	 * Constructor. Creates a new edge.
	 * @param n1 the first (source) node in the edge
	 * @param n2 the second (target) node in the edge
	 * @param directed specifies if this is a directed (true) 
	 * or undirected (false) edge
	 */
	public DefaultEdge(Node n1, Node n2, boolean directed) {
		m_node1 = n1;
		m_node2 = n2;
		m_directed = directed;
	} //
	
	/**
	 * Indicates if this edge is directed or undirected.
	 * @return true if the edge is directed, false otherwise.
	 */
	public boolean isDirected() {
		return m_directed;
	} //
	
	/**
	 * Indicates if this is a tree edge, that is, if this edge helps
	 * define part of a tree structure. This holds true if both incident
	 * edges are instances of DefaultTreeNode and this edge defines a 
	 * parent-child relationship between the two nodes.
	 * @return true if this is a tree-edge, false otherwise.
	 */
	public boolean isTreeEdge() {
		if ( m_node1 instanceof TreeNode && m_node2 instanceof TreeNode ) {
			TreeNode n1 = (TreeNode)m_node1;
			TreeNode n2 = (TreeNode)m_node2;
			return (n1.getParent() == n2 || n2.getParent()== n1);
		}
		return false;
	} //
	
    /**
     * Indicates if this edge is incident on a given node
     * @param n the node to check
     * @return true if this edge is incident on the node, false otherwise.
     */
    public boolean isIncident(Node n) {
        return (m_node1 == n || m_node2 == n);
    } //
    
	/**
	 * Returns the first node in the edge (the source node for
	 * directed edges).
	 * @return the first (source) node
	 */
	public Node getFirstNode() {
		return m_node1;
	} //
	
	/**
	 * Returns the second node in the edge (the target node for
	 * directed edges).
	 * @return the second (target) node
	 */
	public Node getSecondNode() {
		return m_node2;
	} //
	
    /**
     * Sets the first node in the edge (the source node for
     *  directed edges).
     * @param n the new first (source) node.
     */
    public void setFirstNode(Node n) {
        m_node1 = n;
    } //
    
    /**
     * Sets the second node in the edge (the target node for
     * directed edges).
     * @param n the new second (target) node.
     */
    public void setSecondNode(Node n) {
        m_node2 = n;
    } //
    
	/**
	 * Returns the node in this <code>DefaultEdge</code> adjacent to the 
	 * provided node, or null if the provided node is not in this edge.
	 * @param n a Node that should be in this DefaultEdge
	 * @return the Node adjacent to the provided Node
	 */
	public Node getAdjacentNode(Node n) {
		if ( n == m_node1 ) {
			return m_node2;
		} else if ( n == m_node2 ) {
			return m_node1;
		} else {
			return null;
		}
	} //
    
    public String toString() {
        return "Edge("+m_node1+","+m_node2+")";
    } //
	
} // end of class DefaultEdge
