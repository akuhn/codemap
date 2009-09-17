package edu.stanford.hci.flowmap.cluster;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import edu.stanford.hci.flowmap.structure.Edge;
import edu.stanford.hci.flowmap.structure.Node;
import edu.stanford.hci.flowmap.utils.GraphicsGems;

/**
 * This version uses nodes in the structure package instead of
 * the extended prefuse nodes (which are now only used for rendering)
 *
 * This software is distributed under the Berkeley Software Distribution License.
 * Please see http://graphics.stanford.edu/~dphan/code/bsd.license.html
 */
public class ClusterLayout extends FlowLayout {
	
	Collection<Cluster> allClusters;
	protected HierarchicalCluster original_Cluster;
	
	protected String flowTypeStr;
	
	public ClusterLayout(Node source, Collection<Node> allNodes) {
		super(source, allNodes);
		
		assert(source != null);
		assert(source.getQueryRow() != null);
		assert(source.getQueryRow().getRowSchema() != null);
		original_Cluster = new HierarchicalCluster();
		
		flowTypeStr = source.getQueryRow().getRowSchema().getDefaultValueId();
	}
	
	public Node doLayout(){
		
		allClusters = original_Cluster.doCluster(source, allNodes);
		toFlowTree(source, allClusters);
		return source;
	}

	public Collection getClusterCollection() {
		return allClusters;
	}
	
	/**
	 * Constructs a tree that FlowRender can use to draw the tree
	 * @param manyClusters the clusters attached to this node
	 * @return the root of the tree
	 */
	private void toFlowTree(Node rootN, Collection<Cluster> manyClusters) {
		LinkedList<Node> queue = new LinkedList<Node>();
		Node2Cluster node2Cluster = new Node2Cluster();
		
		queue.add(rootN);
		node2Cluster.put(rootN, manyClusters);
		
		while(queue.size() > 0) {
			Node n = (Node) queue.removeFirst();
			//System.out.println("ToFlowTree got: " + n);
			LinkedList clusterList = node2Cluster.get(n);
			assert(clusterList != null && clusterList.size() > 0);
			
			Node newNode;
			Cluster c1, c2;
			//System.out.println("ToFlowTree clusterListSize " + clusterList.size());
			if (clusterList.size() == 1) {
				c1 = (Cluster) clusterList.removeFirst();
				newNode = processCluster(n, c1, node2Cluster);
				if (newNode != null) {
					//System.out.println("toFlowTree newNode: " + newNode);
					newNode.setChildCluster(c1);
					c1.setRenderedNode(newNode);
					queue.add(newNode);
				}
			} else { // more than one cluster per node
				Iterator i = clusterList.iterator();
				while (i.hasNext()) {
					Cluster clus = (Cluster) i.next();
					newNode = processCluster(n, clus, node2Cluster);
										
					if (newNode != null) {
						//System.out.println("toFlowTree newNode: " + newNode);
						((Node)newNode).setChildCluster(clus);
						clus.setRenderedNode(newNode);
						queue.add(newNode);
					} 
				}
			}
			
		}
	}
	
	/**
	 * Adds edges from a parent node that is outside the cluster to the elements
	 * of the cluster.
	 * @param parent the parent node of the cluster
	 * @param clus the cluster that we are drawing to
	 * @return the new node we created
	 */
	private Node processCluster(Node parent, Cluster clus, Node2Cluster node2Cluster) {
		Node newNode = null;
		//System.out.println("ClusterLayout.processCluster got parent:" + parent + " and cluster " + clus);
		// now check all the conditions for the cluster

		
		// simple node, just add an edge. Shouldn't happen too often
		if ( clus.isNodeCluster()) { 
			//System.out.println("SimpleNode case");
			Node clusNode = clus.getRenderedNode();
			Edge e = new Edge(parent, clusNode, flowTypeStr, clus.getWeightTypes());
			
			parent.addOutEdge(e);
			clusNode.addInEdge(e);
			clusNode.setRoutingParent(parent);

		} 
		// we have two clusters
		else {
			assert (clus.oneCluster != null && clus.twoCluster != null);
			// we have two leaf nodes. 
			if ((clus.oneCluster.isNodeCluster()) && (clus.twoCluster.isNodeCluster())) {
				processLeafCluster(parent, clus);
			} 
			// one is leaf, two is cluster. In this case draw a direct edge towards	
			else if ((clus.oneCluster.isNodeCluster()) && (!clus.twoCluster.isNodeCluster())) {
				newNode = processMixedCluster(parent, clus, clus.oneCluster, clus.twoCluster);
				// now add some info to the node2Cluster map
				if (newNode == parent)
					node2Cluster.remove(parent);
				
				node2Cluster.put(newNode, clus.twoCluster);
			}
			// one is cluster, two is leaf
			else if ((!clus.oneCluster.isNodeCluster()) && (clus.twoCluster.isNodeCluster())) {
				newNode = processMixedCluster(parent, clus, clus.twoCluster, clus.oneCluster);
				if (newNode == parent)
					node2Cluster.remove(parent);
				
				node2Cluster.put(newNode, clus.oneCluster);
			}
			// both are clusters
			else {
				newNode = processTwoCluster(parent, clus, clus.oneCluster, clus.twoCluster);
				if (newNode == parent)
					node2Cluster.remove(parent);
				
				node2Cluster.put(newNode, clus.oneCluster);
				node2Cluster.put(newNode, clus.twoCluster);
			}
			
		}
		return newNode;
	}
	
	/**
	 * Constructs part of the layout tree in the case when we have a node that
	 * is connected to a cluster which consists of two nodes.
	 * In particular, we create an newNode between the parent and the leaf
	 * node with the most weight. The new node is halfway between the
	 * parent node and the closest leaf node (not necessarily the one
	 * with the most weight)
	 * 
	 * @param parent the parent node of this cluster
	 * @param clus the cluster we are drawing to (has 2 nodes)
	 */
	private void processLeafCluster(Node parent, Cluster clus) {
		//System.out.println("LeafCluster Process: " + parent);
		Point2D parentPt = parent.getLocation();
		Point2D onePt = clus.oneCluster.getRenderedNode().getLocation();
		Point2D twoPt = clus.twoCluster.getRenderedNode().getLocation();
		
		GraphicsGems.checkNaN(onePt);
		GraphicsGems.checkNaN(twoPt);
		
		// figure out which leaf node is closer
		double oneDist, twoDist, closerDist;
		oneDist = parentPt.distance(onePt);
		twoDist = parentPt.distance(twoPt);
		
		if (oneDist < twoDist) {
			closerDist = oneDist;
		} else {
			closerDist = twoDist;
		}
		
		closerDist /= 2;
		
		// figure out which sub-cluster is bigger
		Point2D biggerPt;
		Cluster biggerClus, smallerClus;
		// find the distance between the parent and the node with more weight
		if (clus.oneCluster.getWeight(flowTypeStr) > clus.twoCluster.getWeight(flowTypeStr)) {
			biggerPt = onePt;
			biggerClus = clus.oneCluster;
			smallerClus = clus.twoCluster;
		} else {
			biggerPt = twoPt;
			biggerClus = clus.twoCluster;
			smallerClus = clus.oneCluster;
		}
		
		double biggerDist = parentPt.distance(biggerPt);
		
		// create the new, intermediate node
		Node newNode = new Node();
		newNode.setChildCluster(clus);
		clus.setRenderedNode(newNode);
		
		// if closerDist is small, bigFraction is 0, so we want
		// the parentFraction to be big.
		double bigFraction = closerDist/biggerDist;
		double parentFraction = 1 - bigFraction;
		
		// set the newPt location to be newFraction of the way
		// between parentPt and biggerPt
		double x = parentPt.getX()*parentFraction+biggerPt.getX()*bigFraction;
		double y = parentPt.getY()*parentFraction+biggerPt.getY()*bigFraction;
		newNode.setLocation(x, y);
		
		// update the edge information
		Edge parent2New, new2Big, new2Small;
		
		parent2New = new Edge(parent, newNode, flowTypeStr, clus.getWeightTypes());
		new2Big = new Edge(newNode, biggerClus.getRenderedNode(), flowTypeStr,
				biggerClus.getWeightTypes());
		new2Small = new Edge(newNode, smallerClus.getRenderedNode(), flowTypeStr,
				smallerClus.getWeightTypes());
				
		parent.addOutEdge(parent2New);
		newNode.addInEdge(parent2New);
		newNode.addOutEdge(new2Small);
		smallerClus.getRenderedNode().addInEdge(new2Small);
		newNode.addOutEdge(new2Big);
		biggerClus.getRenderedNode().addInEdge(new2Big);
		
		newNode.setRoutingParent(parent);
		smallerClus.getRenderedNode().setRoutingParent(newNode);
		biggerClus.getRenderedNode().setRoutingParent(newNode);
		//System.out.println("processLeafCluster: " + newNode);
	}
	
	/**
	 * Constructs part of the layout tree in the case when we have a node that
	 * is connected to a cluster that has a node and smaller cluster. 
	 * @param parent the parent node of this cluster
	 * @param parentCluster the enclosing cluster
	 * @param leafCluster the sibling cluster of clus
	 * @param clus the subcluster we are drawing to
	 * @return a new node
	 */
	private Node processMixedCluster(Node parent, Cluster parentCluster, 
											 Cluster leafCluster, Cluster clus) {
		//System.out.println("MixedCluster Process: " + parent);
		
		Point2D parentPt = parent.getLocation();
		GraphicsGems.checkNaN(parentPt);
		//System.out.println("MixedCluster leafCluster " + leafCluster);
		Point2D onePt = leafCluster.getRenderedNode().getLocation();
		GraphicsGems.checkNaN(onePt);

		//twoPoint is a point between the parentPoint and the centerPoint
		Line2D parent2Center = new Line2D.Double(parentPt, clus.center);
		//System.out.println("MixedCluster parent2Center " + parentPt + " to " + clus.center);
		//System.out.println("MixedCluster: clus: " + clus + " " + clus.bounds);
		Point2D twoPt = GraphicsGems.closestIntersectBox(clus.bounds, parent2Center, parentPt);
		GraphicsGems.checkNaN(twoPt);
		
		//System.out.println("onePt " + onePt + " twoPt: " + twoPt);
		
		// figure out which leaf node is closer
		double oneDist, twoDist, closerDist;
		oneDist = parentPt.distance(onePt);
		twoDist = parentPt.distance(twoPt);
		
		if (oneDist < twoDist) {
			closerDist = oneDist;
		} else {
			closerDist = twoDist;
		}
		
		closerDist /= 2;
		
		// figure out which sub-cluster is bigger
		Point2D biggerPt;
		Cluster biggerClus, smallerClus;
		// find the distance between the parent and the node with more weight
		if (leafCluster.getWeight(flowTypeStr) > clus.getWeight(flowTypeStr)) {
			biggerPt = onePt;
			biggerClus = leafCluster;
			smallerClus = clus;
		} else {
			biggerPt = twoPt;
			biggerClus = clus.twoCluster;
			smallerClus = clus.oneCluster;
		}
		
		double biggerDist = parentPt.distance(biggerPt);
	  	
		// create the new, intermediate node
		Node newNode = new Node();
		//newNode.setChildCluster(clus);
		//clus.setRenderedNode(newNode);
		
		// if closerDist is small, bigFraction is 0, so we want
		// the parentFraction to be big.
		double bigFraction, parentFraction;
		if ((closerDist == 0) && (biggerDist == 0)) {
			System.out.println("Returning parent! " + parent + " with clus " + parentCluster);
			return parent;
		} else {
			bigFraction = closerDist/biggerDist;
			parentFraction = 1 - bigFraction;
		
		
			// set the newPt location to be newFraction of the way
			// between parentPt and biggerPt
			double x = parentPt.getX()*parentFraction+biggerPt.getX()*bigFraction;
			double y = parentPt.getY()*parentFraction+biggerPt.getY()*bigFraction;
			newNode.setLocation(x, y);

			//System.out.println("MixedCluster1: x: " + x + " y:" + y + " "+ newNode);
			
			
			// update the edge information
			Edge parent2New = new Edge(parent, newNode, flowTypeStr,
					parentCluster.getWeightTypes());
			
	
			//FlowEdgeItem new2Leaf = FlowEdgeItem.getNewItem(registry,parent, leafCluster.node, leafCluster.weight, null);
	
			
			// dphan. this is a weird error. Shouldn't this be from newNode to Leaf? we have parent to leaf here)
			Edge new2Leaf = new Edge(newNode, leafCluster.getRenderedNode(), flowTypeStr, 
					leafCluster.getWeightTypes());
		
			//System.out.println("MixedCluster wants to add: p2n " + parent2New);
			//System.out.println("MixedCluster wants to add: n2L " + new2Leaf);
			parent.addOutEdge(parent2New);
			newNode.addInEdge(parent2New);
			newNode.addOutEdge(new2Leaf);
			leafCluster.getRenderedNode().addInEdge(new2Leaf);
			
			newNode.setRoutingParent(parent);
			leafCluster.getRenderedNode().setRoutingParent(newNode);
			
			//System.out.println("MixedCluster2: " + newNode);
			return newNode;
		}
	}
	
	private Node processTwoCluster(Node parent, Cluster parentCluster, 
										Cluster oneCluster, Cluster twoCluster) {
		//System.out.println("TwoCluster Process: " + parent);
		//System.out.println(" oneCluster: " + oneCluster + " twoCluster " + twoCluster);
		
		
		Point2D parentPt = parent.getLocation();
		GraphicsGems.checkNaN(parentPt);

//		twoPoint is a point between the parentPoint and the centerPoint
		Line2D parent2OneCenter = new Line2D.Double(parentPt, oneCluster.center);
		Point2D onePt = GraphicsGems.closestIntersectBox(oneCluster.bounds, parent2OneCenter, parentPt);
		//System.out.println("Multiroot_clusterlayout " + oneCluster.bounds + " " + parent2OneCenter + " " + parentPt);
		GraphicsGems.checkNaN(onePt);
		
//		twoPoint is a point between the parentPoint and the centerPoint
		Line2D parent2TwoCenter = new Line2D.Double(parentPt, twoCluster.center);
		Point2D twoPt = GraphicsGems.closestIntersectBox(twoCluster.bounds, parent2TwoCenter, parentPt);
		GraphicsGems.checkNaN(twoPt);
		
		//System.out.println("onePt: " + onePt + " twoPt: " + twoPt);
		// figure out which leaf node is closer
		double oneDist, twoDist, closerDist;
		oneDist = parentPt.distance(onePt);
		twoDist = parentPt.distance(twoPt);
		
		if (oneDist < twoDist) {
			closerDist = oneDist;
		} else {
			closerDist = twoDist;
		}
		
		closerDist /= 2;
		
		// figure out which sub-cluster is bigger
		Point2D biggerPt;
		Cluster biggerClus, smallerClus;
		// find the distance between the parent and the node with more weight
		if (oneCluster.getWeight(flowTypeStr) > twoCluster.getWeight(flowTypeStr)) {
			biggerPt = onePt;
			biggerClus = oneCluster;
			smallerClus = twoCluster;
		} else {
			biggerPt = twoPt;
			biggerClus = twoCluster;
			smallerClus = oneCluster;
		}
		
		double biggerDist = parentPt.distance(biggerPt);
	  	//System.out.println("parentPt " + parentPt + " biggerPoint: " + biggerPt);
		// create the new, intermediate node
		Node newNode = new Node();
		//newNode.setChildCluster(biggerClus);
		//biggerClus.setRenderedNode(newNode);
		
		// if closerDist is small, bigFraction is 0, so we want
		// the parentFraction to be big.
		//System.out.println("closerDist: " + closerDist + " biggerDist: " + biggerDist);
		double bigFraction, parentFraction;
		if ((closerDist == 0) && (biggerDist == 0)) {
			//since points are so close, just return parent
			//System.out.println("Returning parent! " + parent + " with clus " + parentCluster);
			
			return parent;
		} else {
			bigFraction = closerDist/biggerDist;
			parentFraction = 1 - bigFraction;
			//System.out.println("bigFraction: " + bigFraction + " and parentFraction: " + parentFraction);
			
		
			// set the newPt location to be newFraction of the way
			// between parentPt and biggerPt
			double x = parentPt.getX()*parentFraction+biggerPt.getX()*bigFraction;
			double y = parentPt.getY()*parentFraction+biggerPt.getY()*bigFraction;
			newNode.setLocation(x, y);
	
			
			// update the edge information
			Edge parent2New = new Edge(parent, newNode, flowTypeStr, parentCluster.getWeightTypes());
		
			//System.out.println(parent2New);
			parent.addOutEdge(parent2New);
			newNode.addInEdge(parent2New);
			
			newNode.setRoutingParent(parent);
			
			//System.out.println("TwoCluster: " + newNode);
			
			return newNode;
		}
	}
	
	protected class Node2Cluster {
		private HashMap<Node, LinkedList<Cluster>> map;
		
		public Node2Cluster() {
			map = new HashMap<Node, LinkedList<Cluster>>();
		}
		
		public LinkedList<Cluster> get(Node key) {
			LinkedList<Cluster> c = (LinkedList<Cluster>) map.get(key);
			return c;
		}
		
		public void put(Node key, Cluster clus) {
			LinkedList<Cluster> coll = get(key);
			if (coll == null) {
				coll = new LinkedList<Cluster>();
				map.put(key, coll);
			} 
			coll.add(clus);
		}
		
		public void put(Node key, Collection<Cluster> manyClus) {
			LinkedList<Cluster> coll = get(key);
			if (coll == null) {
				coll = new LinkedList<Cluster>();
				map.put(key, coll);
			} 
			coll.addAll(manyClus);
		}
		
		public void remove(Node key) {
			map.remove(key);
		}
 	}
}
