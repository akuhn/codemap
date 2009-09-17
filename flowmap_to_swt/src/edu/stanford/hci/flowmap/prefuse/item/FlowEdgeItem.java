package edu.stanford.hci.flowmap.prefuse.item;

import java.awt.Shape;
import java.awt.geom.CubicCurve2D;
import java.util.Hashtable;

import edu.berkeley.guir.prefuse.EdgeItem;
import edu.berkeley.guir.prefuse.ItemRegistry;
import edu.berkeley.guir.prefuse.NodeItem;
import edu.berkeley.guir.prefuse.graph.Edge;
import edu.berkeley.guir.prefuse.graph.Entity;
import edu.berkeley.guir.prefuse.graph.Node;
import edu.stanford.hci.flowmap.prefuse.structure.FlowDummyNode;
import edu.stanford.hci.flowmap.prefuse.structure.FlowEdge;
import edu.stanford.hci.flowmap.prefuse.structure.FlowNode;

/**
 * This software is distributed under the Berkeley Software Distribution License.
 * Please see http://graphics.stanford.edu/~dphan/code/bsd.license.html
 *
 */
public class FlowEdgeItem extends EdgeItem {

	/*************************************************************************
	 * STATIC METHODS
	 ************************************************************************/
	public static final String WEIGHT_ATTR = "FlowEdgeItem.Weight";
	
	
	/*************************************************************************
	 * CLASS DEFINITIONS
	 ************************************************************************/
	
	protected String dummyName = null;
	
	protected Shape m_shape = null;
	
	protected Hashtable<String, Double> types2Weights = new Hashtable<String, Double>();
	
	protected String defaultType;
	
	
	public static FlowEdgeItem getNewItem(ItemRegistry registry,FlowNodeItem a, FlowNodeItem b, String name){
		FlowEdge tEdge = new FlowEdge(a.getSourceNode(), b.getSourceNode());
		FlowEdgeItem tItem = (FlowEdgeItem)registry.getItem(FlowEdge.class.getName(), tEdge, true);
		tItem.dummyName = name;
		return tItem;
	}
	
	public static FlowEdgeItem getNewItem(ItemRegistry registry,FlowNodeItem a, FlowNodeItem b,
			String defaultType, Hashtable<String, Double> str2Weight, String edgeName){
		FlowEdgeItem tItem = FlowEdgeItem.getNewItem(registry,a,b, edgeName);
		assert(str2Weight != null);
		tItem.types2Weights = str2Weight;
		tItem.defaultType = defaultType;
		
		return tItem;
	}
	
	public String getDummyName(){
		return dummyName;
	}
	
	public double getWeight(String type) {
		assert(types2Weights != null);
		//System.out.println("FlowEdgeItem.Getting type " + type);
		assert(types2Weights.containsKey(type));
		return types2Weights.get(type);
	}
	
	public void setWeight(String type, double val) {
		types2Weights.put(type, val);
	}


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
		m_itemClass = itemClass;
		m_registry = registry;
		m_entity   = entity;
		m_dirty    = 0;
		m_visible  = false;
		m_newlyVisible = false;
		m_doi = Integer.MIN_VALUE;
		initAttributes();
		
		Edge edge = (Edge)entity;
		FlowNode n1 = (FlowNode)edge.getFirstNode();
		FlowNode n2 = (FlowNode)edge.getSecondNode();
		
		//System.out.println("n1 is: " + n1 + " and n2 is: " + n2);
		
	   	if(n1 == null)
	   		throw new IllegalArgumentException(
            "Noden1 can not be null");
	   	if(n2 == null)
	   		throw new IllegalArgumentException(
            "Noden2 can not be null");
    	
		FlowNodeItem item1 = (FlowNodeItem)getItem(n1);
		if(item1 == null)
			throw new IllegalArgumentException(
            "Nodei1 can not be null");
		//System.out.println("Item1 is: " + item1);
		setFirstNode(item1);
		FlowNodeItem item2 = (FlowNodeItem)getItem(n2);
		if(item2 == null)
			throw new IllegalArgumentException(
            "Nodei2 can not be null");
		//System.out.println("Item2 is: " + item2);
		setSecondNode(item2);
	} //
	
	protected NodeItem getItem(Node n) {
		NodeItem temp = null;
		
		if( n instanceof FlowDummyNode)
			temp = (FlowNodeItem) m_registry.getItem(FlowDummyNode.class.getName(),n,false);
		else 
			temp = (FlowNodeItem) m_registry.getItem(FlowNode.class.getName(),n,false);
		
		return temp;
	} //
    
    private void nodeItemCheck(Node n) {
    	
    	if( n == null)
    		throw new IllegalArgumentException(
            "Node can not be null");
    	
        if ( !(n instanceof FlowNodeItem) )
            throw new IllegalArgumentException(
                "Node must be an instance of FlowNodeItem");
    } //

    /**
     * Indicates whether or not this edge is a directed edge.
     * @return true if this edge is directed, false otherwise
     */
	public boolean isDirected() {
        return false;
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
    	//System.out.println("FlowEdgeItem.getAdjacentNode for edge a " + toString() + " with node " + n);
    	//System.out.println("FlowEdgeItem.getAdjacentNode for edge b " + m_node1 + " " + m_node2 + " " + getWeight());
    	//System.out.println(m_node1 == n);
    	//System.out.println(m_node2 == n);
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
        m_node1 = (FlowNodeItem)item;
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
		m_node2 = (FlowNodeItem)item;
	} //

    /**
     * @see edu.berkeley.guir.prefuse.graph.Edge#isIncident(edu.berkeley.guir.prefuse.graph.Node)
     */
    public boolean isIncident(Node n) {
        return (n == m_node1 || n == m_node2);
    } //
    
    public String toString() {
    	return "FlowEdgeItem " + m_node1 + "->" + m_node2 + " w: " + getWeight(defaultType);
    }
    
    public Shape getShape() {
    	return m_shape;
    }
    
    public void setShape(Shape s) {
    	m_shape = s;
    }
    
    public CubicCurve2D getCubicCurve() {
    	if (m_shape instanceof CubicCurve2D)
    		return (CubicCurve2D) m_shape;
    	else
    		throw new RuntimeException("Asked for a cubic curve when we really have a " + m_shape);
    }
}
