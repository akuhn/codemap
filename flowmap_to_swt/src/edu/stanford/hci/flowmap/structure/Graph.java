package edu.stanford.hci.flowmap.structure;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

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
	
	public Collection<Edge> getEdges() {
	    HashSet<Edge> edges = new HashSet<Edge>();
	    Deque<Node> queue = new ArrayDeque<Node>();
	    queue.add(getRootNode());
	    
	    while(!queue.isEmpty()) {
	        Node node = queue.pop();
	        Collection<Edge> outEdges = node.getOutEdges();
	        for(Edge each: outEdges) {
	            queue.add(each.getSecondNode());
	        }
	        edges.addAll(outEdges);
	    }
	    return edges;
	}

}
