package edu.stanford.hci.flowmap.structure;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * This software is distributed under the Berkeley Software Distribution License.
 * Please see http://graphics.stanford.edu/~dphan/code/bsd.license.html
 *
 */
public class Graph {
    
    /**
     * Records the min, max, and total values for the weight values.
     */
    protected class MinMaxTotal {
        private double low, high, total;
        
        protected MinMaxTotal() {
            low = Double.MAX_VALUE;
            high = Double.MIN_VALUE;
            total = 0;
        }
        
        protected double getMin() {
            return low;
        }
        
        protected double getMax() {
            return high;
        }
        
        protected double getTotal() {
            return total;
        }
        
        protected void update(double val) {
            if (val < low) {
                low = val;
            } 
            if (val > high) {
                high = low;
            }
            total += val;
        }
    }    
    
	
	private Node rootNode;
	private LinkedList<Node> allNodes;
    private MinMaxTotal minMaxTotal;
	
	public Graph() {
		rootNode = null;
		allNodes = new LinkedList<Node>();
		minMaxTotal = new MinMaxTotal();
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
		minMaxTotal.update(n.getWeight());
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

    public double getTotalWeightValue() {
        return minMaxTotal.getTotal();
    }

    public double getMinWeightValue() {
       return minMaxTotal.getMin();
    }

}
