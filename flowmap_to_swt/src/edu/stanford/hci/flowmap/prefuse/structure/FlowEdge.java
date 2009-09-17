package edu.stanford.hci.flowmap.prefuse.structure;

import java.util.Hashtable;

import edu.berkeley.guir.prefuse.graph.DefaultEdge;


/**
 * FlowEdge with the understanding that
 * - First Node is the source
 * - Second Node is the target
 *
 * This software is distributed under the Berkeley Software Distribution License.
 * Please see http://graphics.stanford.edu/~dphan/code/bsd.license.html
 *
 */
public class FlowEdge extends DefaultEdge {

	private String defaultType = null;
	private Hashtable<String, Double> types2Weights = null;
	
	public FlowEdge(FlowNode from, FlowNode to, String defaultType,	Hashtable<String, Double> types2Weights){
		super(from, to, false);
		this.defaultType = defaultType;
		this.types2Weights = types2Weights;
	}
	
	public FlowEdge(FlowNode from, FlowNode to) {
		super(from,to,false);
	}
	
	public void setWeight(String type, double weight){
		assert(types2Weights.containsKey(type));
		types2Weights.put(type, weight);
	}
	
	public double getWeight(){
		if (types2Weights == null)
			return -1;
		else
			return types2Weights.get(defaultType);
	}
	
	public boolean equals(FlowEdge e) {
		return m_node1.equals(e.m_node1) && m_node2.equals(e.m_node2);
	}
	
	public String toString() {
		if ((m_node1 != null) && (m_node2 != null))
			return m_node1.toString() + "->" + m_node2.toString() + " w:" + getWeight();
		else
			return "FlowEdge.toString something is null";
	}
}

