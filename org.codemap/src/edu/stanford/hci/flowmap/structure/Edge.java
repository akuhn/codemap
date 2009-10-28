package edu.stanford.hci.flowmap.structure;

import java.util.Hashtable;

import org.codemap.util.geom.Shape;


/**
 * This software is distributed under the Berkeley Software Distribution License.
 * Please see http://graphics.stanford.edu/~dphan/code/bsd.license.html
 *
 */
public class Edge {

	private Node node1, node2;
	
    private Shape shape;

    private Double weight;
	
	public Edge(Node from, Node to, Double weight){
		this(from,to);
		this.weight = weight;
	}
	
	private Edge(Node from, Node to) {
		node1 = from;
		node2 = to;
	}
	
	public double getWeight() {
	    return weight;
	}
	
	public boolean equals(Edge e) {
		return node1.equals(e.node1) && node2.equals(e.node2);
	}
	
	public String toString() {
		if ((node1 != null) && (node2 != null))
			return node1.toString() + "->" + node2.toString() + " w:" + getWeight();
		else
			return "FlowEdge.toString something is null";
	}
	
	public Node getFirstNode() {
		return node1;
	}
	
	public Node getSecondNode() {
		return node2;
	}

    public boolean isIncident(Node n) {
        return (n == node1 || n == node2);
    }

    public Shape getShape() {
        return shape;
    }

    public void setShape(Shape shape) {
        this.shape = shape;
    }
}

