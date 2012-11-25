package edu.berkeley.guir.prefuse;

import edu.berkeley.guir.prefuse.graph.Edge;
import edu.berkeley.guir.prefuse.graph.Entity;
import edu.berkeley.guir.prefuse.graph.Node;

/**
 * Visual representation of an edge in a graph.
 * 
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public class EdgeItem extends VisualItem implements Edge {

	protected NodeItem m_node1;
	protected NodeItem m_node2;

	/**
	 * Initialize this EdgeItem, binding it to the
     *  given ItemRegistry and Entity.
	 * @param registry the ItemRegistry monitoring this VisualItem
	 * @param entity the Entity represented by this VisualItem
	 */
	public void init(ItemRegistry registry, String itemClass, Entity entity) {
		if ( entity != null && !(entity instanceof Edge) ) {
			throw new IllegalArgumentException("EdgeItem can only represent an Entity of type Edge.");
		}
		super.init(registry, itemClass, entity);
		
		Edge edge = (Edge)entity;
		Node n1 = edge.getFirstNode();
		Node n2 = edge.getSecondNode();
		
		NodeItem item1 = getItem(n1);
		setFirstNode(item1);
		NodeItem item2 = getItem(n2);
		setSecondNode(item2);
	} //
	
	protected NodeItem getItem(Node n) {
		return m_registry.getNodeItem(n);
	} //
    
    private void nodeItemCheck(Node n) {
        if ( !(n instanceof NodeItem) )
            throw new IllegalArgumentException(
                "Node must be an instance of NodeItem");
    } //

    /**
     * Indicates whether or not this edge is a directed edge.
     * @return true if this edge is directed, false otherwise
     */
	public boolean isDirected() {
        return ((Edge)m_entity).isDirected();
	} //
    
    /**
     * Indicates whether or not this edge is a tree edge.
     * @return true if this edge is a tree edge, false otherwise
     */
    public boolean isTreeEdge() {
        NodeItem n1 = (NodeItem)m_node1;
        NodeItem n2 = (NodeItem)m_node2;
        return (n1.getParent() == n2 || n2.getParent()== n1);
    } //
	
    /**
     * Given a node item incident on this edge, returns the other node item
     * incident on this edge.
     * @param n a NodeItem incident on this edge
     * @return the other NodeItem incident on this edge
     * @throws IllegalArgumentException if the provided NodeItem is either
     *  not a NodeItem or is not incident on this edge.
     */
    public Node getAdjacentNode(Node n) {
        nodeItemCheck(n);
        if ( m_node1 == n )
            return m_node2;
        else if ( m_node2 == n )
            return m_node1;
        else
            throw new IllegalArgumentException(
               "The given node is not incident on this Edge.");
    } //
    
	/**
	 * Return the VisualItem representing the first (source) node in the edge.
	 * @return the first (source) VisualItem
	 */
	public Node getFirstNode() {
		return m_node1;
	} //
	
	/**
	 * Set the VisualItem representing the first (source) node in the edge.
	 * @param item the first (source) VisualItem
	 */
	public void setFirstNode(Node item) {
        nodeItemCheck(item);
        m_node1 = (NodeItem)item;
	} //
	
	/**
	 * Return the VisualItem representing the second (target) node in the edge.
	 * @return the second (target) VisualItem
	 */
	public Node getSecondNode() {
		return m_node2;
	} //
	
	/**
	 * Set the NodeItem representing the second (target) node in the edge.
	 * @param item the second (target) NodeItem
	 */
	public void setSecondNode(Node item) {
        nodeItemCheck(item);
		m_node2 = (NodeItem)item;
	} //

    /**
     * @see edu.berkeley.guir.prefuse.graph.Edge#isIncident(edu.berkeley.guir.prefuse.graph.Node)
     */
    public boolean isIncident(Node n) {
        return (n == m_node1 || n == m_node2);
    } //

} // end of class EdgeItem
