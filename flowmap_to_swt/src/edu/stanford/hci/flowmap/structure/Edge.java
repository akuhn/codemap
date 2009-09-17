package edu.stanford.hci.flowmap.structure;

import java.util.Hashtable;


/**
 * This software is distributed under the Berkeley Software Distribution License.
 * Please see http://graphics.stanford.edu/~dphan/code/bsd.license.html
 *
 */
public class Edge {

	private Node node1, node2;
	
	private String defaultType;
	private Hashtable<String, Double> types2Weights;
	
	public Edge(Node from, Node to, String defaultType, Hashtable<String, Double> str2Weight){
		this(from,to);
		this.defaultType = defaultType;
		this.types2Weights = str2Weight;
	}
	
	private Edge(Node from, Node to) {
		node1 = from;
		node2 = to;
		types2Weights = null;
	}
	
	public void setWeight(String type, double weight){
		assert (types2Weights.containsKey(type));
		types2Weights.put(type, weight);
	}
	
	public double getWeight() {
		return types2Weights.get(defaultType);
	}
	
	public double getWeight(String type){
		return types2Weights.get(type);
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
	
	public Hashtable<String, Double> getTypes2Weights() {
		return types2Weights;
	}
	
	public String getDefaultType() {
		return defaultType;
	}
}

