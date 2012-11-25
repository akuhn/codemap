package edu.stanford.hci.flowmap.cluster;

import java.util.Collection;

import edu.stanford.hci.flowmap.structure.Node;


/**
 * Abstract class for running a layout algorithm on a collection of nodes.
 * What layout means in this instance is constructing a Flow Tree from a collection
 * of nodes that have already been positioned with a NodeLayout and a ForceLayout.
 *
 * This software is distributed under the Berkeley Software Distribution License.
 * Please see http://graphics.stanford.edu/~dphan/code/bsd.license.html
 *
 */
public abstract class FlowLayout {
	protected Node source;
	protected Collection<Node> allNodes;
	
	public FlowLayout(Node source, Collection<Node> allNodes) {
		this.source = source;
		this.allNodes = allNodes;
	}
	
	/**
	 * Runs the layout algorithm on the given FlowRecord using the FlowScale operator
	 * @return the source node. 
	 */
	public abstract Node doLayout();
}
