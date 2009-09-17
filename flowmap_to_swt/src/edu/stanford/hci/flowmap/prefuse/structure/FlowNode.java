package edu.stanford.hci.flowmap.prefuse.structure;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import edu.berkeley.guir.prefuse.graph.DefaultNode;
import edu.stanford.hci.flowmap.db.QueryRow;
import edu.stanford.hci.flowmap.structure.Node;

/**
 * The class represents a flowmap node
 *
 * This software is distributed under the Berkeley Software Distribution License.
 * Please see http://graphics.stanford.edu/~dphan/code/bsd.license.html
 *
 */
public class FlowNode extends DefaultNode {
	
	/*****************************************************************
	 * STATIC METHODS AND VARIABLES
	 *****************************************************************/
	public static String getUniqueName() {
		return "Node" + nodeCount;
	}
	
	//every node is assigned a unique id when created
	private static int nodeCount = 0;
	
	/*****************************************************************
	 * CLASS DEFINITION
	 *****************************************************************/
	protected int m_id = -1;
	protected QueryRow queryRow = null;
	protected boolean isRootNode = false;
	protected boolean isDummyNode = false;
	protected String name;
	protected Point2D position = null;
	
	
	/*****************************************************************
	 * CONSTRUCTORS
	 *****************************************************************/
	
	public FlowNode(Node n) {
		this();
		//System.out.println("FlowNode got node: " + n.getName());
		this.name = n.getName();
		this.setPosition2D(n.getLocation());
		this.queryRow = n.getQueryRow();
		this.isRootNode = n.isRootNode();
		this.setDummy(n.isDummyNode());
	}
	
	public FlowNode() {
		m_id = FlowNode.nodeCount++;
		this.setName(FlowNode.getUniqueName());
		position = new Point2D.Double();
	}
	
	
	/*****************************************************************
	 * ACCESSORS
	 *****************************************************************/
	public void setDummy(boolean flag) {
		isDummyNode = flag;
	}
	
	public boolean isDummy() {
		return isDummyNode;
	}
	
	public void setRootNode(boolean value){
		isRootNode = value;
	}
	
	public boolean isRootNode(){
		return isRootNode;
	}
	
	public void setPosition2D(Point2D pt){
		position.setLocation(pt);
	}
	
	public Point2D getPosition2D(){
		return new Point2D.Double(position.getX(), position.getY());
	}
	
	public int getID(){
		return m_id;
	}
	
	public QueryRow getRow() {
		return queryRow;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String n) {
		this.name = n;
	}
	
	public void addOutEdge(FlowEdge out) {
		assert (out.getFirstNode().equals(this));
		this.addEdge(out);
	}
	
	public void addInEdge(FlowEdge in) {
		assert (in.getSecondNode().equals(this));
		this.addEdge(in);
	}
	
	public Collection getOutEdges() {
		//filter the edge list for those edges for which this is the first node
		ArrayList outEdges = new ArrayList();
		for(Iterator edgeIter = this.getEdges(); edgeIter.hasNext();){
			FlowEdge t_edge = (FlowEdge)edgeIter.next();
			if(t_edge.getFirstNode().equals(this)){
				outEdges.add(t_edge);
			}
		}
		return outEdges;
	}
	
	public Collection getInEdges() {
		//filter the edge list for those edges for which this is the second node
		ArrayList inEdges = new ArrayList();
		for(Iterator edgeIter = this.getEdges(); edgeIter.hasNext();){
			FlowEdge t_edge = (FlowEdge)edgeIter.next();
			if(t_edge.getSecondNode().equals(this)){
				inEdges.add(t_edge);
			}
		}
		return inEdges;
	}
	
	public String toString() {
		if (this instanceof FlowDummyNode) {
			return "DummyNode id:" + m_id;
		} else {
			return getRow()+ " id:" + m_id;
		}
	}
	
	public boolean equals(Object o) {
		FlowNode n = (FlowNode)o;
		return getName().equals(n.getName());
	}
	
	public String toStringId() {
		return m_id + "";
	}
	
}
