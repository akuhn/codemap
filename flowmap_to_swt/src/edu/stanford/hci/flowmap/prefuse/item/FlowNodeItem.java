package edu.stanford.hci.flowmap.prefuse.item;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import edu.berkeley.guir.prefuse.ItemRegistry;
import edu.berkeley.guir.prefuse.NodeItem;
import edu.berkeley.guir.prefuse.graph.Entity;
import edu.stanford.hci.flowmap.prefuse.structure.FlowNode;
import edu.stanford.hci.flowmap.utils.GraphicsGems;


/**
 * A VisualItem for FlowNodes.
 *
 * This software is distributed under the Berkeley Software Distribution License.
 * Please see http://graphics.stanford.edu/~dphan/code/bsd.license.html
 *
 */
public abstract class FlowNodeItem extends NodeItem {

	
	/************************************************************
	 * STATIC FIELDS
	 ************************************************************/
	public static final String NAME_ATT = "name_a0934lkndo9fu5i23498";
	public static final String ID_ATT = "id_249809p8q4rjdfo9h4r";
	public static final String CREATE_ID = "create_id_0928340293423";

	//used by the layout algorithm
	public static final String ROUTING_PARENT_ATT = "routing_parent_124098sadfoj239048";
	
	//used by the rendering algorithm. For each node, it is the 
	// position of the last control point for the thick spline that
	// connects this node with its parent
	public static final String PREV_CTRL_PT_ATT = "previous control point ad098adf098adsf098adsf";
	
	/************************************************************
	 * INITIALIZATION CODE
	 ************************************************************/
	/**
	 * Initialize this NodeItem, binding it to the given
	 * ItemRegistry and Entity.
	 * @param registry the ItemRegistry monitoring this VisualItem
	 * @param entity the Entity represented by this VisualItem
	 */	
	public void init(ItemRegistry registry, String itemClass, Entity entity) {
		if ( entity != null && !(entity instanceof FlowNode) ) {
			throw new IllegalArgumentException("NodeItem can only represent an Entity of type Node.");				
		}
		super.init(registry, itemClass, entity);
		
		//Do Flow related initialization for the node
		if(entity instanceof FlowNode){
			this.setID(((FlowNode) entity).getID());
		} else 
			throw new RuntimeException("Received: " + entity);
	} 
	
	public static void initializeRealNode(FlowNodeItem newItem){
		if(newItem.getEntity() instanceof FlowNode){
			FlowNode srcNode = (FlowNode)newItem.getEntity();
			newItem.setLocation(srcNode.getPosition2D());
			newItem.setEndLocation(srcNode.getPosition2D());
			newItem.setName(srcNode.getName());
			newItem.id = ((FlowNode)newItem.getEntity()).getID();
		}
	}
	
	
	/************************************************************
	 * CLASS DEFINITION
	 ************************************************************/
	
	//from the graph node
	protected int id;
	
	protected String dummyName = null;
	
	protected boolean routingParentSet = false;
	protected boolean prevControlPtSet = false;
	
	protected boolean isRootNode = false;
	protected boolean isDummyNode = false;
	
	/************************************************************
	 * ACCESSORS 
	 ************************************************************/
	
	public void setDummy(boolean flag) {
		isDummyNode = flag;
	}
	
	public boolean isDummy() {
		return isDummyNode;
	}
	
	public String getDummyName(){
		return dummyName;
	}
	
	public void addOutEdge(FlowEdgeItem out) {
		assert (out.getFirstNode().equals(this));
		boolean b = this.addEdge(out);
	} 
	
	public void addInEdge(FlowEdgeItem in) {
		assert (in.getSecondNode().equals(this));
		boolean b = this.addEdge(in);
	}
	
	public Collection getOutEdges() {
		//filter the edge list for those edges for which this is the first node
		ArrayList outEdges = new ArrayList();
		for(Iterator edgeIter = this.getEdges(); edgeIter.hasNext();){
			FlowEdgeItem t_edge = (FlowEdgeItem)edgeIter.next();
			//System.out.println("FlowNodeItem.getOutEdges() " + t_edge);
			if(t_edge.getFirstNode().equals(this)){
				outEdges.add(t_edge);
				//System.out.println("Added.");
			}
		}
		//System.out.println();
		return outEdges;
	}
	
	public Collection getInEdges() {
		//filter the edge list for those edges for which this is the second node
		ArrayList inEdges = new ArrayList();
		for(Iterator edgeIter = this.getEdges(); edgeIter.hasNext();){
			FlowEdgeItem t_edge = (FlowEdgeItem)edgeIter.next();
			if(t_edge.getSecondNode().equals(this)){
				inEdges.add(t_edge);
			}
		}
		return inEdges;
	}
	
	public FlowNode getSourceNode(){
		if(this.getEntity() instanceof FlowNode){
			return (FlowNode)this.getEntity();
		} else {
			throw new RuntimeException("FlowNodeItem.getSourceNode has entity not of type FlowNode");
		}
	}
	
	/************************************************************/
	
	public String getName(){
		return (String) this.getVizAttribute(NAME_ATT);
	}
	
	public void setName(String name){
		this.setVizAttribute(NAME_ATT,name);
	}
	
	/************************************************************/
	
	public int getID(){	
		return ((Integer)this.getVizAttribute(ID_ATT)).intValue();
	}
	
	public void setID(int id){
		this.setVizAttribute(ID_ATT,new Integer(id));
	}
	
	/************************************************************/
	
	public FlowNodeItem getRoutingParent(){
		if (routingParentSet)
			return (FlowNodeItem) this.getVizAttribute(ROUTING_PARENT_ATT);
		else
			throw new RuntimeException("FlowNodeItem routingParent is not set (maybe set it to null?)");
	}
	
	public void setRoutingParent(FlowNodeItem nodeItem){
		routingParentSet = true;
		this.setVizAttribute(ROUTING_PARENT_ATT,nodeItem);
		
		if(this.isRootNode() && nodeItem != null){
			System.err.println("Setting root routing parent: " + nodeItem);
		}
	}
	
	/************************************************************/
	
	public Point2D getPrevControlPoint(){
		if (prevControlPtSet)
			return (Point2D) this.getVizAttribute(PREV_CTRL_PT_ATT);
		else{
			String name = this.getName();
			if(this instanceof FlowDummyNodeItem)
				name = ((FlowDummyNodeItem)this).dummyName;
			
			throw new RuntimeException(name + ":" + id + " prevControlPoint not set (maybe set it to null?)");
		}
	}
	
	public void setPrevControlPoint(Point2D pt){
		prevControlPtSet = true;
		this.setVizAttribute(PREV_CTRL_PT_ATT,pt);
	}
	
	public boolean hasPrevControlPoint(){
		return prevControlPtSet;
	}
	
	/************************************************************/
	
	public double getWeight(){
		if(getSourceNode() == null || getSourceNode().getRow() == null)
			return 1.0;
		
		return getSourceNode().getRow().getDefaultValue();
	}
	
	public String toString() {
		return id + " name:" + getName() + " location:"+ getLocation();	
	}
	
	public void setLocation(Point2D loc) {
		super.setLocation(loc);
		GraphicsGems.checkNaN(loc);
	}
	
	public void setLocation(double x, double y) {
		super.setLocation(x,y);
		assert(!Double.isNaN(x));
		assert(!Double.isNaN(y));
	}
	
	public boolean isRootNode() {
		return isRootNode;
	}
	
	public void setRootNode(boolean b) {
		isRootNode = true;
	}
}
