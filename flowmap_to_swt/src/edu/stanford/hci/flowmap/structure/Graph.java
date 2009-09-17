package edu.stanford.hci.flowmap.structure;

import java.util.Collection;
import java.util.LinkedList;

/**
 * This software is distributed under the Berkeley Software Distribution License.
 * Please see http://graphics.stanford.edu/~dphan/code/bsd.license.html
 *
 */
public class Graph {
	
	private Node rootNode;
	private LinkedList<Node> allNodes;
	
	public Graph() {
		rootNode = null;
		allNodes = new LinkedList<Node>();
	}
	
	public Node getRootNode() {
		return rootNode;
	}
	
	public void setRootNode(Node n) {
		rootNode = n;
	}
	
	public Collection<Node> getAllNodes() {
		return allNodes;
	} 

	public void addNode(Node n) {
		allNodes.add(n);
	}

}
