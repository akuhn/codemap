package edu.stanford.hci.flowmap.structure;

import java.util.Collection;
import java.util.LinkedList;

import org.codemap.util.geom.Point2D;

import edu.stanford.hci.flowmap.cluster.Cluster;

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
	protected String name;
	protected LinkedList<Edge> inEdges;
	protected LinkedList<Edge> outEdges;
	
	/** the cluster that encloses this node */
	protected Cluster parentCluster;
	
	/** the cluster that this node has outgoing edges towards */ 
	protected Cluster childCluster;
	
	protected Node routingParent;
	
	protected boolean isRootNode;

    private Point2D prevControlPoint;

    private double weight;

	/***********************************************************************
	 * CONSTRUCTORS
	 * @param location 
	 **********************************************************************/

	public Node(Point2D loc) {
		m_id = Node.nodeCount++;
		location = loc;
		name = Node.getUniqueName(m_id);
		inEdges = new LinkedList<Edge>();
		outEdges = new LinkedList<Edge>();
		parentCluster = null;
		childCluster = null;
	}
	
    public Node(Point2D loc, double weight, String name) {
        this(loc);
        this.weight = weight;
        this.name = name;
    }	

    public Node(double x, double y) {
        this(makeLocation(x, y));
    }
    
    public Node(double x, double y, double weight, String name) {
        this(makeLocation(x, y), weight, name);
    }    

    static Point2D makeLocation(double x, double y) {
        return new Point2D.Double(x, y);
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
		return "id:" + m_id;
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
	
	public void setRootNode(boolean b) {
		isRootNode = b;
	}
	
	public Node getRoutingParent() {
		return routingParent;
	}
	
	public void setRoutingParent(Node n) {
		routingParent = n;
	}

    public Point2D getPrevControlPoint() {
        return prevControlPoint;
    }

    public void setPrevControlPoint(Point2D ctrlP2) {
        prevControlPoint = ctrlP2;
    }

    public double getWeight() {
        return weight;
    }
	

}
