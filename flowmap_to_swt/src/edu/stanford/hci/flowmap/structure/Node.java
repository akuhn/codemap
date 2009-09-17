package edu.stanford.hci.flowmap.structure;

import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.LinkedList;

import edu.stanford.hci.flowmap.cluster.Cluster;
import edu.stanford.hci.flowmap.db.QueryRow;

/**
 * This software is distributed under the Berkeley Software Distribution License.
 * Please see http://graphics.stanford.edu/~dphan/code/bsd.license.html
 *
 */
public class Node {

	/***********************************************************************
	 * STATIC METHODS AND VARIABLES
	 **********************************************************************/
	public static String getUniqueName(int id) {
		return "Node" + id;
	}
	
	// every node is assigned a unique id when created
	private static int nodeCount = 0;

	/***********************************************************************
	 * CLASS DEFINITION
	 **********************************************************************/
	protected int m_id;
	protected Point2D location;
	protected QueryRow queryRow;
	protected String name;
	protected LinkedList<Edge> inEdges;
	protected LinkedList<Edge> outEdges;
	
	/** the query id that generated the data (the queryRow) for this Node */
	protected Integer m_defaultQueryId;
	
	/** the cluster that encloses this node */
	protected Cluster parentCluster;
	
	/** the cluster that this node has outgoing edges towards */ 
	protected Cluster childCluster;
	
	protected Node routingParent;
	
	protected boolean isRootNode;

	/***********************************************************************
	 * CONSTRUCTORS
	 **********************************************************************/

	public Node(QueryRow row) {
		this();
		this.queryRow = row;
		m_defaultQueryId = row.getQueryId();
		this.setName(row.getName());
		this.setLocation(row.getLocationClone());
		
		//System.out.println("Created new Node name: " + row.getName() + " uniqueName: " + m_id);
	}

	public Node() {
		m_id = Node.nodeCount++;
		location = new Point2D.Double();
		queryRow = null;
		name = Node.getUniqueName(m_id);
		inEdges = new LinkedList<Edge>();
		outEdges = new LinkedList<Edge>();
		m_defaultQueryId = null;
		parentCluster = null;
		childCluster = null;
	}

	/***********************************************************************
	 * ACCESSORS
	 **********************************************************************/
/*
	public void setParentCluster(Cluster c) {
		//assert(c != null);
		parentCluster = c;
	}
	
	public Cluster getParentCluster() {
		//assert(parentCluster != null);
		return parentCluster;
	}*/
	
	public void setChildCluster(Cluster c) {
		childCluster = c;
	}
	
	/**
	 * 
	 * @return the child cluster, or null if this is a leaf node
	 */
	public Cluster getChildCluster() {
		return childCluster;
	}
	
	public void setLocation(Point2D pt) {
		location = pt;
	}
	
	public void setLocation(double x, double y) {
		location.setLocation(x, y);
	}

	public Point2D getLocation() {
		return location;
	}

	public int getID() {
		return m_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String n) {
		name = n;
	}

	public void addOutEdge(Edge out) {
		assert (out.getFirstNode().equals(this));
		outEdges.add(out);
	}

	public void addInEdge(Edge in) {
		assert (in.getSecondNode().equals(this));
		inEdges.add(in);
	}

	public Collection<Edge> getOutEdges() {
		return outEdges;
	}

	public Collection<Edge> getInEdges() {
		return inEdges;
	}
	
	public void removeEdge(Edge edge) {
		inEdges.remove(edge);
		outEdges.remove(edge);
		
	}

	public String toString() {
		if (queryRow == null) {
			return "id:" + m_id;
		} else {
			return queryRow + " id:" + m_id;
		}
	}

	public boolean equals(Object o) {
		Node n = (Node) o;
		// System.out.println("FlowNodeEquals: Does " + getName() + " equal
		// " + n.getName() + " " + getName().equals(n.getName()));
		return getName().equals(n.getName());
	}

	public String toStringId() {
		return m_id + "";
	}
	
	public boolean isRootNode() {
		return isRootNode;
	}
	
	public boolean isDummyNode() {
		return queryRow == null;
	}
	
	public void setRootNode(boolean b) {
		isRootNode = b;
	}
	
	public Node getRoutingParent() {
		return routingParent;
	}
	
	public void setRoutingParent(Node n) {
		routingParent = n;
	}
	
	public QueryRow getQueryRow() {
		return queryRow;
	}
	

}
