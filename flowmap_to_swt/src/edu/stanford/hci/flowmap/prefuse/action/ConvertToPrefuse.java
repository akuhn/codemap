package edu.stanford.hci.flowmap.prefuse.action;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;

import edu.berkeley.guir.prefuse.ItemRegistry;
import edu.berkeley.guir.prefuse.graph.DefaultGraph;
import edu.stanford.hci.flowmap.prefuse.item.FlowEdgeItem;
import edu.stanford.hci.flowmap.prefuse.item.FlowNodeItem;
import edu.stanford.hci.flowmap.prefuse.structure.FlowMapStructure;
import edu.stanford.hci.flowmap.prefuse.structure.FlowNode;
import edu.stanford.hci.flowmap.structure.Edge;
import edu.stanford.hci.flowmap.structure.Node;

/**
 * Converts from the nodes in structure to the nodes used in prefuse.structure
 * and render.
 * 
 * This software is distributed under the Berkeley Software Distribution License.
 * Please see http://graphics.stanford.edu/~dphan/code/bsd.license.html
 *
 */
public class ConvertToPrefuse {

	private Node rootNode;
	
	/** maps our nodes to FlowNodes */
	private HashMap<Node, FlowNode> node2FlowNodes;
	
	/** maps FlowNodes to FlowNodeItems to keep track of mapping ourselves*/
	private HashMap<FlowNode, FlowNodeItem> flowNode2Item;
	
	private ItemRegistry registry;
	
	public ConvertToPrefuse(ItemRegistry registry, Node rootNode) {
		this.rootNode = rootNode;
		this.registry = registry;
		node2FlowNodes = new HashMap<Node, FlowNode>();	
		flowNode2Item = new HashMap<FlowNode, FlowNodeItem>();
	}
	
	public FlowNodeItem convert() {
		FlowNodeItem rootItem = convertToNodeItem();
		//printTree(rootItem);
		return rootItem;
		
	}
	
	private void printTree(FlowNodeItem rootItem) {
		System.out.println("ConvertToPrefuse.PrintingTree");
		LinkedList<FlowNodeItem> queue = new LinkedList<FlowNodeItem>();
		queue.add(rootItem);
		while(queue.size() > 0 ) {
			FlowNodeItem n = queue.removeFirst();
			System.out.println(n);
			Iterator i = n.getOutEdges().iterator();
			while (i.hasNext()) {
				Object o = i.next();
				FlowEdgeItem edge = (FlowEdgeItem)o;
				System.out.println("\t"+o);
				queue.add((FlowNodeItem) edge.getSecondNode());
			}
 		}
		System.out.println("DonePrintingTree\n");
		
		
	}
	
	/**
	 * Traverse the tree and collapses edges which are too short.
	 */
	public void removeTooClose() {
		LinkedList<Node> queue = new LinkedList<Node>();
		
		LinkedList<Edge> edgesToAdd = new LinkedList<Edge>();
		LinkedList<Edge> edgesToRemove = new LinkedList<Edge>();
		
		queue.add(rootNode);
		
		Node n1, n2, n3;
		Edge newEdge;
		while (queue.size() > 0) {
			edgesToRemove.clear();
			edgesToAdd.clear();
			
			n1 = queue.removeFirst();
			System.out.println("remove2Close - got " + n1);
			for(Edge e1 : n1.getOutEdges()) {
				System.out.println("remove2Close edge: " + e1);
				n2 = e1.getSecondNode();
				
				// if n2 is too close, get rid of it! 
				if ((n1.getLocation().distance(n2.getLocation()) < 15) &&
						(n2.getOutEdges().size() > 0)){
					System.out.println("remove2Close " + n2 + " is too close.");
					
					for (Edge e2 : n2.getOutEdges()) {
						n3 = e2.getSecondNode();
						System.out.println("remove2Close setting " + n3 + " to have parent " + n1);
						n3.setRoutingParent(n1);
						n3.removeEdge(e2);
						
						newEdge = new Edge(n1, n3, e2.getDefaultType(), e2.getTypes2Weights());
						n3.addInEdge(newEdge);
						
						edgesToAdd.add(newEdge);
						
						// need to add this node to the queue again - if the children are also close
					}
					n2.getInEdges().clear();
					n2.getOutEdges().clear();
					
					edgesToRemove.add(e1);
					queue.addFirst(n1);
				
				} else {
					queue.add(n2);
				}
			}
			for (Edge e: edgesToRemove) {
				n1.removeEdge(e);
			}
	
			for(Edge e: edgesToAdd) {
				n1.addOutEdge(e);
			}
			
			System.out.println();
		}
		
	}
	
	public FlowNodeItem convertToNodeItem() {
		LinkedList<Edge> edgeQueue = new LinkedList<Edge>();
		
		// create graph structure
		FlowMapStructure flowGraph = new FlowMapStructure();
		registry.setGraph(flowGraph);
		
		// Create the filtered graph
		DefaultGraph filteredFlowGraph = (DefaultGraph)registry.getFilteredGraph();
	    if ( filteredFlowGraph instanceof DefaultGraph )
	        ((DefaultGraph)filteredFlowGraph).reinit(flowGraph.isDirected());
	    else
	    	filteredFlowGraph = new DefaultGraph(flowGraph.isDirected());
	    
	    registry.setFilteredGraph(filteredFlowGraph);
		
		// add root node to graph
	    FlowNode flowNodeRoot = new FlowNode(rootNode);
	    flowNodeRoot.setRootNode(true);
		
		node2FlowNodes.put(rootNode, flowNodeRoot);
		
		// create flow node item for root node
		FlowNodeItem rootNodeItem = getNodeItem(registry, null, flowNodeRoot, flowGraph, filteredFlowGraph);
		
		
		// add outgoing edges from root
		for(Edge e : rootNode.getOutEdges()) {
			//System.out.println("convert edges from root: " + e);
			edgeQueue.add(e);
		}
		
		Edge edge;
		Node n1, n2;
		FlowNode flowNode1, flowNode2, flowNode1Parent, flowNode2Parent;
		FlowNodeItem nodeItem1, nodeItem2;
		while(edgeQueue.size() > 0) {
			edge = edgeQueue.removeFirst();
			//System.out.println("Convert2Prefuse: " + edge);
			n1 = edge.getFirstNode();
			n2 = edge.getSecondNode();
			flowNode1 = getFlowNode(n1);
			flowNode2 = getFlowNode(n2);
			flowNode1Parent = getFlowNode(n1.getRoutingParent());
			flowNode2Parent = getFlowNode(n2.getRoutingParent());
			nodeItem1 = getNodeItem(registry, flowNode1Parent, flowNode1, flowGraph, filteredFlowGraph);
			nodeItem2 = getNodeItem(registry, flowNode2Parent, flowNode2, flowGraph, filteredFlowGraph);
			getEdgeItem(registry, nodeItem1, nodeItem2, edge.getDefaultType(), edge.getTypes2Weights(),
					flowGraph, filteredFlowGraph);
			for(Edge outEdge : n2.getOutEdges()) {
				edgeQueue.add(outEdge);
			}
			
			//printTree(rootNodeItem);
		}
		//System.out.println("rootNodesize: " + rootNodeItem.getOutEdges().size());
		
		return rootNodeItem;
	}
	
	private FlowNodeItem getNodeItem(ItemRegistry registry, FlowNode routingParent, FlowNode node, DefaultGraph flowGraph, 
			DefaultGraph filteredFlowGraph) {
		
		if(flowNode2Item.containsKey(node))
			return flowNode2Item.get(node);
		
		flowGraph.addNode(node);
		FlowNodeItem parentItem;
		if (routingParent == null) {
			parentItem = null;
		} else {
			if (flowNode2Item.containsKey(routingParent)) {
				parentItem = flowNode2Item.get(routingParent);
			} else {
				parentItem = (FlowNodeItem)registry.getItem(FlowNode.class.getName(), routingParent,true);
				
				flowNode2Item.put(routingParent, parentItem);
			}
		}
		FlowNodeItem item = (FlowNodeItem)registry.getItem(FlowNode.class.getName(), node,true);
        item.setAttribute(FlowNodeItem.CREATE_ID, ConvertToPrefuse.class.getName());
      
        item.setDummy(node.isDummy());
        if (!node.isDummy()) {
             item.setName(node.getName());
        } else {
        	item.setName(null);
        }
        
        filteredFlowGraph.addNode(item);
        FlowNodeItem.initializeRealNode(item);

        item.setPrevControlPoint(null);
        item.setRoutingParent(parentItem);
        
        flowNode2Item.put(node, item);
        
        return item;
 
	}
	
	private void getEdgeItem(ItemRegistry registry, FlowNodeItem node1, FlowNodeItem node2, String defaultType,
			Hashtable<String, Double> types2Weights, DefaultGraph flowGraph, DefaultGraph filteredFlowGraph) {
		
		//the last argument to this is the edge name. we could add it in here if we wanted more descriptive edge names
		FlowEdgeItem edgeItem = FlowEdgeItem.getNewItem(registry, node1, node2, defaultType,
				types2Weights, null);
		registry.getFilteredGraph().addEdge(edgeItem);
		//System.out.println("Adding " + edgeItem + " to be outedge for: " + node1 + " and inedge for: " +node2);
		node1.addOutEdge(edgeItem);
		node2.addInEdge(edgeItem);
	}

	
	public FlowNode getFlowNode(Node n) {
		if (n == null) //handle root node's parent
			return null;
		if (node2FlowNodes.containsKey(n)) {
			return node2FlowNodes.get(n);
		} else {
			FlowNode flowNode = new FlowNode(n);
			node2FlowNodes.put(n, flowNode);
			return flowNode;
		}
	}
}
