package edu.stanford.hci.flowmap.prefuse.action;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;

import javax.vecmath.Vector2d;

import edu.stanford.hci.flowmap.cluster.Cluster;
import edu.stanford.hci.flowmap.structure.Edge;
import edu.stanford.hci.flowmap.structure.Node;
import edu.stanford.hci.flowmap.utils.GraphicsGems;
/**
 * Instead of only moving the nodes, we will add new routing nodes to make the spline 
 * curve around the other cluster.
 *
 * This software is distributed under the Berkeley Software Distribution License.
 * Please see http://graphics.stanford.edu/~dphan/code/bsd.license.html
 *
 */
public class NodeEdgeRouting {
	
	Node rootNode;
	
	public NodeEdgeRouting(Node rootNode){
		this.rootNode = rootNode;
	}
	
	public void routeNodes(){
		
		Node root = rootNode;
		Collection rootEdges = root.getOutEdges();
		
		LinkedList queue = new LinkedList();
		
		// add the nodes that are 1 away from the starting node
		for(Iterator rootEdgeIter = root.getOutEdges().iterator(); rootEdgeIter.hasNext();){
			Edge edge = (Edge)rootEdgeIter.next();
			Node nextNode = (Node)edge.getSecondNode();
			
			if(nextNode.isDummyNode()){
				queue.addLast(nextNode);
			}
		}
		
		Edge edgeItem;
		Node parent;
		
		// represents the children of the node that we are currently processing
		// that we need to add the queue of nodes to process
		Collection newNodes = null;
		
		// while we still have nodes to route
		while(queue.size() > 0) {
			parent = (Node) queue.removeFirst();
			//System.out.println("\nNodeEdgeRoute: Processing Dummy: " + parent);
			// this is the cluster that nodeItem is drawing to
			Cluster clus = parent.getChildCluster();
			
			//	if just a cluster that contains 1 node, no need to route anything here
			//  if clus == null it is a leaf node
			if (clus == null){
				continue;
			} else if (clus.isNodeCluster()) {
				//System.out.println("NodeEdgeRouting. clus is not null but is a node cluster (has 1 node)");
				continue;
			// otherwise get the children of the parent and add it to the 
			} else {
				for(Iterator edgeIter = parent.getOutEdges().iterator(); edgeIter.hasNext();){
					Edge edge = (Edge)edgeIter.next();
					//System.out.println("NodeEdgeRoute: Looking at: " + edge);
					if(edge.getSecondNode().isDummyNode())
						queue.addLast(edge.getSecondNode());
						//System.out.println("RouteLayerEdge - Added to queue: " + edge.getSecondNode());
				}
			}
			
			// see which case this is
			
			assert (clus.oneCluster != null && clus.twoCluster != null);
			// we have two leaf nodes. 
			if ((clus.oneCluster.isNodeCluster()) && (clus.twoCluster.isNodeCluster())) {
				processLeafCluster(parent, clus);
			} 
			// one is leaf, two is cluster. 
			else if ((clus.oneCluster.isNodeCluster()) && (!clus.twoCluster.isNodeCluster())) {
				//for mixed clusters, don't run this
				//processTwoCluster(parent, clus, clus.oneCluster, clus.twoCluster, layer);
			}
			// one is cluster, two is leaf
			else if ((!clus.oneCluster.isNodeCluster()) && (clus.twoCluster.isNodeCluster())) {
				//for mixed clusters, don't run this
				//processTwoCluster(parent, clus, clus.twoCluster, clus.oneCluster, layer);
			}
			// both are clusters
			else {
				processTwoCluster(parent, clus, clus.oneCluster, clus.twoCluster);
			}
			
		}
	}
	
	/**
	 * Routes an edge around a leaf node that occludes another leaf node
	 * @param parent
	 * @param clus
	 */
	private void processLeafCluster(Node parent, Cluster clus) {
		Cluster oneCluster = clus.oneCluster;
		Cluster twoCluster = clus.twoCluster;
		
//		 get the two nodes associated with the clusters
		Node oneChild = (Node) oneCluster.getRenderedNode();
		Node twoChild = (Node) twoCluster.getRenderedNode();
		
		Line2D parentToOne = new Line2D.Double(parent.getLocation(), oneChild.getLocation());
		Line2D parentToTwo = new Line2D.Double(parent.getLocation(), twoChild.getLocation());
		Rectangle2D oneBox = oneCluster.bounds;
		Rectangle2D twoBox = twoCluster.bounds;
		
		//System.out.println("LeafparentBox:" + clus.bounds + " oneBox: " + oneBox + " twoBox: " + twoBox);
		
		boolean oneIntersectsTwoBox = parentToOne.intersects(twoBox);
		boolean twoIntersectsOneBox = parentToTwo.intersects(oneBox);
		boolean oneInsideTwoBox = twoBox.contains(oneChild.getLocation());
		boolean twoInsideOneBox = oneBox.contains(twoChild.getLocation());
		
		Node createdNode;
		boolean outsideBox = true;
		
		if (oneIntersectsTwoBox && !twoIntersectsOneBox) {
			//System.out.println("LeafIntersects TwoBox");
			outsideBox = moveOutFromBoundingBox(parent, oneChild, parentToOne, twoCluster, oneCluster);
			if (outsideBox) {
				// don't forget to update the line that we're passing around
				parentToOne = new Line2D.Double(parent.getLocation(), oneChild.getLocation());
				createdNode = routeAroundClusterAddNode(parent, oneChild, parentToOne, twoCluster, oneCluster);
				if (createdNode == null) {
					//System.out.println("Created node was null");
					return;
				}
				// check if we need another level of routing 
				Line2D newLine = new Line2D.Double(createdNode.getLocation(), oneChild.getLocation());
				if (newLine.intersects(twoBox)) {
					routeAroundClusterAddNode(createdNode, oneChild, newLine, twoCluster, oneCluster);
				}
			}
		} else if (!oneIntersectsTwoBox && twoIntersectsOneBox) {
			//System.out.println("LeafIntersects OneBox");
			outsideBox = moveOutFromBoundingBox(parent, twoChild, parentToTwo, oneCluster, twoCluster);
			if (outsideBox) {
				parentToTwo = new Line2D.Double(parent.getLocation(), twoChild.getLocation());
				createdNode = routeAroundClusterAddNode(parent, twoChild, parentToTwo, oneCluster, twoCluster);
				if (createdNode == null) {
					//System.out.println("Created node was null");
					return;
				}
				// check if we need another level of routing
				Line2D newLine = new Line2D.Double(createdNode.getLocation(), twoChild.getLocation());
				if (newLine.intersects(oneBox)) {
					routeAroundClusterAddNode(createdNode, twoChild, newLine, oneCluster, twoCluster);
				}
			}
		}
	}
	
	/**
	 * Routes an edge around a mixed cluster
	 * @param parent
	 * @param parentCluster
	 * @param leafCluster
	 * @param clus
	 */
	private void processMixedCluster(Node parent, Cluster parentCluster, 
			 Cluster leafCluster, Cluster clus) {
	}
	
	
	/**
	 * Adds nodes to the routing if one cluster occludes the other
	 * @param parent the flow node item that contains thes two clusters
	 * @param parentCluster the containing cluster
	 * @param oneCluster the first child cluster
	 * @param twoCluster the secord child cluster
	 */
	private void processTwoCluster(Node parent, Cluster parentCluster, 
			Cluster oneCluster, Cluster twoCluster) {
		
		// get the two nodes associated with the clusters
		Node oneChild = (Node) oneCluster.getRenderedNode();
		Node twoChild = (Node) twoCluster.getRenderedNode();
		
		Line2D parentToOne = new Line2D.Double(parent.getLocation(), oneChild.getLocation());
		Line2D parentToTwo = new Line2D.Double(parent.getLocation(), twoChild.getLocation());
		Rectangle2D oneBox = oneCluster.bounds;
		Rectangle2D twoBox = twoCluster.bounds;
		
		//System.out.println("TwoCluster: parentBox:" + parentCluster.bounds + " oneBox: " + oneBox + " twoBox: " + twoBox);
		
		boolean oneIntersectsTwoBox = parentToOne.intersects(twoBox);
		boolean twoIntersectsOneBox = parentToTwo.intersects(oneBox);
		
		Node createdNode;
		boolean outsideBox = true;
		//System.out.println("p2One: " + parentToOne + " p2two: " + parentToTwo + " 1Isect2: " + oneIntersectsTwoBox + " 2Isect1: " + twoIntersectsOneBox);;
		
		if (oneIntersectsTwoBox && !twoIntersectsOneBox) {
				//need to ensure parentToOne's line is completely outside of two's bounding box
				if (twoBox.contains(oneChild.getLocation())) {
					// compute a new point that lies on the ray from parent to One that is outside 2's bounding box
		
					Vector2d direction = new Vector2d(oneChild.getLocation().getX() - parent.getLocation().getX(), 
							oneChild.getLocation().getY() - parent.getLocation().getY());
					direction.normalize();
					Point2D rayPt = GraphicsGems.rayBox2DIntersect(oneChild.getLocation(), direction, twoBox);
					//System.out.println("raypt: " + rayPt);
					Point2D newPoint = new Point2D.Double(rayPt.getX() + 2*direction.x, rayPt.getY()+2*direction.y);
					//System.out.println("ParentToOne was: " + parentToOne.getP1() + "->" + parentToOne.getP2());
					parentToOne = new Line2D.Double(parent.getLocation(), newPoint);
					//System.out.println("ParentToOne became: " + parentToOne.getP1() + "->" + parentToOne.getP2());
				} // else just use the existing line
				
			
				createdNode = routeAroundClusterAddNode(parent, oneChild, parentToOne, twoCluster, oneCluster);
				if (createdNode == null) {
					//System.out.println("Created node was null");
					return;	
				}
				
				// we need to relayout the tree again
				processTreeCluster(createdNode.getLocation(), oneChild, oneCluster);
				
				//System.out.println("\n\nChecking if we need to route again");
				// check if we need another level of routing 
				Line2D newLine;
				if (twoBox.contains(oneChild.getLocation())) {
					// compute a new point that lies on the ray from parent to One that is outside 2's bounding box
		
					Vector2d direction = new Vector2d(oneChild.getLocation().getX() - createdNode.getLocation().getX(), 
							oneChild.getLocation().getY() - createdNode.getLocation().getY());
					direction.normalize();
					Point2D rayPt = GraphicsGems.rayBox2DIntersect(oneChild.getLocation(), direction, twoBox);
					//System.out.println("raypt: " + rayPt);
					Point2D newPoint = new Point2D.Double(rayPt.getX() + 2*direction.x, rayPt.getY()+2*direction.y);
					newLine = new Line2D.Double(createdNode.getLocation(), newPoint);
				} 
				else {
					newLine = new Line2D.Double(createdNode.getLocation(), oneChild.getLocation());
				}
				if (newLine.intersects(twoBox)) {
					//System.out.println("Routing around twoBox again");
					routeAroundClusterMoveNode(createdNode, oneChild, newLine, twoCluster, oneCluster);
					processTreeCluster(oneChild.getLocation(), oneCluster.oneCluster.getRenderedNode(), oneCluster.oneCluster);
					processTreeCluster(oneChild.getLocation(), oneCluster.twoCluster.getRenderedNode(), oneCluster.twoCluster);
				} else {
					//System.out.println("Don't need to route again");
					processTreeCluster(createdNode.getLocation(), oneChild, oneCluster);
				}

		} else if (!oneIntersectsTwoBox && twoIntersectsOneBox) {
			if (oneBox.contains(twoChild.getLocation())) {
				// compute a new point that lies on the ray from parent to One that is outside 2's bounding box
	
				Vector2d direction = new Vector2d(twoChild.getLocation().getX() - parent.getLocation().getX(), 
						twoChild.getLocation().getY() - parent.getLocation().getY());
				direction.normalize();
				Point2D rayPt = GraphicsGems.rayBox2DIntersect(twoChild.getLocation(), direction, oneBox);
				//System.out.println("rayPt: " + rayPt);
				Point2D newPoint = new Point2D.Double(rayPt.getX() + 2*direction.x, rayPt.getY()+2*direction.y);
				//System.out.println("ParentToTwo was: " + parentToTwo.getP1() + "->" + parentToTwo.getP2());
				parentToTwo = new Line2D.Double(parent.getLocation(), newPoint);
				//System.out.println("ParentToTwo became: " + parentToTwo.getP1() + "->" + parentToTwo.getP2());
		
			} // else just use the existing line
			
				createdNode = routeAroundClusterAddNode(parent, twoChild, parentToTwo, oneCluster, twoCluster);
				if (createdNode == null) {
					//System.out.println("Created Node was null");
					return;
				}
				processTreeCluster(createdNode.getLocation(), twoChild, twoCluster);
				
				//System.out.println("\n\nChecking if we need to route again");
				Line2D newLine;
				if (oneBox.contains(twoChild.getLocation())) {
					// compute a new point that lies on the ray from parent to One that is outside 2's bounding box
		
					Vector2d direction = new Vector2d(twoChild.getLocation().getX() - createdNode.getLocation().getX(), 
							twoChild.getLocation().getY() - createdNode.getLocation().getY());
					direction.normalize();
					Point2D rayPt = GraphicsGems.rayBox2DIntersect(twoChild.getLocation(), direction, oneBox);
					//System.out.println("rayPt: " + rayPt);
					Point2D newPoint = new Point2D.Double(rayPt.getX() + 2*direction.x, rayPt.getY()+2*direction.y);
					newLine = new Line2D.Double(createdNode.getLocation(), newPoint);
				} else { 
					// check if we need another level of routing
					newLine = new Line2D.Double(createdNode.getLocation(), twoChild.getLocation());
				}
				if (newLine.intersects(oneBox)) {
					//System.out.println("Routing around oneBox again");
					
					routeAroundClusterMoveNode(createdNode, twoChild, newLine, oneCluster, twoCluster);
					processTreeCluster(twoChild.getLocation(), twoCluster.oneCluster.getRenderedNode(), twoCluster.oneCluster);
					processTreeCluster(twoChild.getLocation(), twoCluster.twoCluster.getRenderedNode(), twoCluster.twoCluster);
				} else {
					//System.out.println("Don't need to route again");
					processTreeCluster(createdNode.getLocation(), twoChild, twoCluster);
				}
		
		// no need to do any intersection
		} else if (!oneIntersectsTwoBox && !twoIntersectsOneBox) {
			return;			
		} else if (oneIntersectsTwoBox && twoIntersectsOneBox) {
			//System.err.println("NodeEdgeRouting - processTwoCluster - Both segments intersect the other box!");
		}
		
	}

	/**
	 * This method changes the position of endItem and the routeAroundClusterAddNode 
	 *  adds a new node between start and endItem
	 * @param startItem the parent cluster's dummy node 
	 * @param endItem the dummy node item of the cluster that is not being intersected
	 * @param line a line from startItem to endItem
	 * @param clusToAvoid the cluster we're going around
	 * @param destClust the cluster we're going to
	 */
	private Node routeAroundClusterMoveNode(Node startItem, 
												Node endItem, Line2D line, 
												Cluster clusToAvoid, Cluster destClus) {
		// get corner point on bounding box of clusToAvoid that is closest to the startItem 
		Point2D cornerPoint = GraphicsGems.closestCornerBox(clusToAvoid.bounds, line, startItem.getLocation());
		if (cornerPoint == null) {
			//System.out.println("routeAroundCluster - Corner Point was null");
			return null;
		}
		
		// now take that line and find the perpendicular vector to it.
		// shift the point away from the box by some number of units (so it is outside the box)
		// we want to avoid the corner case
		
		double x = line.getP1().getX() - line.getP2().getX();
		double y = line.getP1().getY() - line.getP2().getY();
		Vector2d lineVec = new Vector2d(x,y);
		lineVec.normalize();
		
		// now get the perpendicular components
		Vector2d orthoLine = new Vector2d(-lineVec.y, lineVec.x);
		
		// just compute two possible points. The one that's not in the box
		// is the right one :)
		double distance = 0;
		
		Point2D one = new Point2D.Double(cornerPoint.getX()+distance, cornerPoint.getY()+distance);
		Point2D two = new Point2D.Double(cornerPoint.getX()-distance, cornerPoint.getY()-distance);
		Point2D three = new Point2D.Double(cornerPoint.getX()+distance, cornerPoint.getY()-distance);;
		Point2D four = new Point2D.Double(cornerPoint.getX()-distance, cornerPoint.getY()+distance);;
		if (clusToAvoid.bounds.contains(one))
			cornerPoint = two;
		else if (clusToAvoid.bounds.contains(two))
			cornerPoint = one;
		else if (clusToAvoid.bounds.contains(three))
			cornerPoint = four;
		else 
			cornerPoint = three;
		
		//assert(!clusToAvoid.bounds.contains(cornerPoint));
		
		Node newNode = null;
		
		// create a new dummy node between startItem and endItem
		//newNode = addPrevNode(endItem, cornerPoint, destClus.getWeight(layer),layer);
		//System.out.println("routeAroundCorner moving point to " + cornerPoint);
		endItem.setLocation(cornerPoint);
		
		return newNode;
	}
	
	
	/**
	 * Adds dummy node items between startItem and endItem
	 * @param startItem the parent cluster's dummy node 
	 * @param endItem the dummy node item of the cluster that is not being intersected
	 * @param line a line from startItem to endItem
	 * @param clusToAvoid the cluster we're going around
	 * @param destClust the cluster we're going to
	 * @param layer the layer we are in
	 */
	private Node routeAroundClusterAddNode(Node startItem, 
												Node endItem, Line2D line, 
												Cluster clusToAvoid, Cluster destClus) {
		//System.out.println("clusToAvoid: " + clusToAvoid.bounds + " line: " + line.getP1() + "->" + line.getP2() + " startItem: " + startItem.getLocation());
		// get corner point on bounding box of clusToAvoid that is closest to the startItem 
		Point2D cornerPoint = GraphicsGems.closestCornerBox(clusToAvoid.bounds, line, startItem.getLocation());
		if (cornerPoint == null)
			return null;
		
		// now take that line and find the perpendicular vector to it.
		// shift the point away from the box by some number of units (so it is outside the box)
		// we want to avoid the corner case
		
		double x = line.getP1().getX() - line.getP2().getX();
		double y = line.getP1().getY() - line.getP2().getY();
		Vector2d lineVec = new Vector2d(x,y);
		lineVec.normalize();
		
		// now get the perpendicular components
		Vector2d orthoLine = new Vector2d(-lineVec.y, lineVec.x);
		
		// just compute two possible points. The one that's not in the box
		// is the right one :)
		double distance = 0;
		
		Point2D one = new Point2D.Double(cornerPoint.getX()+distance, cornerPoint.getY()+distance);
		Point2D two = new Point2D.Double(cornerPoint.getX()-distance, cornerPoint.getY()-distance);
		Point2D three = new Point2D.Double(cornerPoint.getX()+distance, cornerPoint.getY()-distance);;
		Point2D four = new Point2D.Double(cornerPoint.getX()-distance, cornerPoint.getY()+distance);;
		if (clusToAvoid.bounds.contains(one))
			cornerPoint = two;
		else if (clusToAvoid.bounds.contains(two))
			cornerPoint = one;
		else if (clusToAvoid.bounds.contains(three))
			cornerPoint = four;
		else 
			cornerPoint = three;
		
		//assert(!clusToAvoid.bounds.contains(cornerPoint));
		
		Node newNode = null;
		
		// create a new dummy node between startItem and endItem
		newNode = addPrevNode(endItem, cornerPoint, destClus.getDefaultType(),
				destClus.getWeightTypes());
		
		return newNode;
	}
	
	/**
	 * Inserts a dummy node in front of oldNode
	 * 
 	 * @param oldNode the node that we want to add a FlowDummyNodeItem in front of
	 * @param newLocation the location of the FlowDummyNodeItem
	 * @param defaultType the string that represents the type of the value that is being plotted
	 * @param type2Weights maps the names of types to actual weights 
	 * @return the newNode that was added
	 */
	protected Node addPrevNode(Node oldNode, Point2D newLocation, String defaultType,
			Hashtable<String,Double> types2Weights){

//		this is the case where you need to add a dummy node infront of a node
		Node newNode = new Node();
		
		Collection OldInEdges = oldNode.getInEdges();
		LinkedList<Edge> edges2BeRemoved = new LinkedList<Edge>();
		
		//change the edges from the previous nodes to newNode
		for(Iterator oldInIter = OldInEdges.iterator(); oldInIter.hasNext();){
			Edge edge = (Edge)oldInIter.next();
			edges2BeRemoved.add(edge);
		}
		for (Edge edge : edges2BeRemoved) {
			Node prevNode = (Node)edge.getFirstNode();
			
			//remove the old edge
			oldNode.removeEdge(edge);
			prevNode.removeEdge(edge);
			
			//create new edges
			Edge newEdge = new Edge(edge.getFirstNode(), newNode , defaultType, types2Weights);
			newNode.addInEdge(newEdge);
			prevNode.addOutEdge(newEdge);
		}

		//System.out.println("Adding previous node " + newNode.getID() + " at: " + newLocation);
		
		// change the edges from newNode to oldNode
		Edge newEdge = new Edge(newNode ,oldNode, defaultType, types2Weights);
		oldNode.addInEdge(newEdge);
		newNode.addOutEdge(newEdge);
		
		newNode.setRoutingParent(oldNode.getRoutingParent());
		newNode.setLocation(newLocation);
		
		return newNode;
	}
	
	/**
	 * Attempts to move the endItem further out from the clusToAvoid if there is room between clusToAvoid and destClus
	 * It does this by modifying the position of endItem
	 * @param startItem the node that we start from
	 * @param endItem the node we are going to
	 * @param line a line from startItem to enditem
	 * @param clusToAvoid the cluster we're trying to avoid
	 * @param destClus the cluster we're trying to get to (associated with endItem)
	 * @param layer the current layer we are in
	 * @return true if the endItem is outside of the clusToAvoid, false if it was not
	 */
	public boolean moveOutFromBoundingBox(Node startItem, Node endItem, Line2D line, Cluster clusToAvoid, 
			Cluster destClus) {
		// check to make sure starItem is not in
		//System.out.println("MoveOutEnter: clusToAvoid is: " + clusToAvoid.bounds + " destClus is: " + destClus.bounds);
		
		if (clusToAvoid.bounds.contains(startItem.getLocation())) {
			//System.out.println("MoveOut: clusToAvoid contains startItem: clusToAvoid is: " + clusToAvoid + " destClus is: " + destClus);
			return false;
		}
		// don't have to do anything if the end item is not inside the cluster to avoid
		if (!clusToAvoid.bounds.contains(endItem.getLocation()))
			return true;
		
		// create a ray from endItem, going in the same direction as endItem-startItem
		Point2D origin = endItem.getLocation();
		//System.out.println("moveOut startItem: " + startItem + " endItem: " + endItem);
		Vector2d direction = new Vector2d(origin.getX() - startItem.getLocation().getX(), origin.getY() - startItem.getLocation().getY());
		direction.normalize();
		
		// find out the intersection of this ray with the clusToAvoid
		Point2D intersection = GraphicsGems.rayBox2DIntersect(origin, direction, clusToAvoid.bounds);
		//assert(intersection != null);
		if (intersection == null) {
			//System.out.println("moveOut intersection was null");
			return false;
		}
		
		// intersection should be on the boundary. Let's find the intersection of a new ray with destClus
		Point2D nextIntersection = GraphicsGems.rayBox2DIntersect(intersection, direction, destClus.bounds);
		//assert(nextIntersection != null);
		if (nextIntersection == null) {
			//System.out.println("next intersection was null");
			return false;
		}
		
		if (destClus.bounds.contains(intersection)) {
			//System.out.println("\nMoveout intersection was at: " + intersection + "\n");
			return false;
		}
		// find the distance between the two intersections
		double distance = intersection.distance(nextIntersection);
		
		Point2D newPoint;
		if (distance < 10) {
			//System.out.println("NodeEdgeRouting - move out - too close distance is " + distance);
			newPoint = new Point2D.Double(nextIntersection.getX(), nextIntersection.getY());
		} else {
			// let's move it a fraction of the way to the next box
			double x = intersection.getX() + (distance/5)*direction.x;
			double y = intersection.getY() + (distance/5)*direction.y;
			newPoint = new Point2D.Double(x,y);

		}
		//System.out.println("NodeEdgeRouting - moving node from inside bounding box for " + startItem + " from: " + endItem.getLocation() + " to: " +  newPoint + "where distance is: " + distance);
		
		endItem.setLocation(newPoint);
		
		assert(!clusToAvoid.bounds.contains(endItem.getLocation()));
		
		return true;
	}
	
	/**************************************************************************
	 * Code to move the children of a node that has been routed.
	 * Taken from Multiroot_ClusterLayout, but modified so that instead of creating
	 * a new node, we only modify its position
	 **************************************************************************/

	private void processTreeCluster(Point2D previousPoint, Node node, Cluster clus) {
		
		// simple node, just return
		if ( clus.isNodeCluster()) { 
			return;
		} 
		// we have two clusters
		else {
			assert (clus.oneCluster != null && clus.twoCluster != null);
			// we have two leaf nodes. 
			if ((clus.oneCluster.isNodeCluster()) && (clus.twoCluster.isNodeCluster())) {
				processTreeLeafCluster(previousPoint, node, clus);
			} 
			// one is leaf, two is cluster. In this case draw a direct edge towards	
			else if ((clus.oneCluster.isNodeCluster()) && (!clus.twoCluster.isNodeCluster())) {
				processTreeMixedCluster(previousPoint, node, clus, clus.oneCluster, clus.twoCluster);
			}
			// one is cluster, two is leaf
			else if ((!clus.oneCluster.isNodeCluster()) && (clus.twoCluster.isNodeCluster())) {
				processTreeMixedCluster(previousPoint, node, clus, clus.twoCluster, clus.oneCluster);
			}
			// both are clusters
			else {
				processTreeTwoCluster(previousPoint, node, clus);
			}		
		}

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
	private void processTreeLeafCluster(Point2D previousPoint, Node node, 
			Cluster clus) {
		//System.out.println("\nTreeLeafCluster Process: " + previousPoint + " and clus: " + clus);
		Point2D parentPt = previousPoint;
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
		if (clus.oneCluster.getDefaultWeight() > clus.twoCluster.getDefaultWeight()) {
			biggerPt = onePt;
			biggerClus = clus.oneCluster;
			smallerClus = clus.twoCluster;
		} else {
			biggerPt = twoPt;
			biggerClus = clus.twoCluster;
			smallerClus = clus.oneCluster;
		}
		
		double biggerDist = parentPt.distance(biggerPt);
		
		// if closerDist is small, bigFraction is 0, so we want
		// the parentFraction to be big.
		double bigFraction = closerDist/biggerDist;
		double parentFraction = 1 - bigFraction;
		
		// set the newPt location to be newFraction of the way
		// between parentPt and biggerPt
		double x = parentPt.getX()*parentFraction+biggerPt.getX()*bigFraction;
		double y = parentPt.getY()*parentFraction+biggerPt.getY()*bigFraction;
		
		//get the associated node here for the cluster
		//System.out.println("dummyNode id: " + node.getID() + " currentLoc: " + node.getLocation() + " and new location: " + x + "," + y);
		node.setLocation(x, y);
		
	}
	
	/**
	 * Constructs part of the layout tree in the case when we have a node that
	 * is connected to a cluster that has a node and smaller cluster. 
	 * @param parent the parent node of this cluster
	 * @param leafNode the leaf node we are drawing to
	 * @param clus the subcluster we are drawing to
	 */
	private void processTreeMixedCluster(Point2D previousPoint, Node node, Cluster containingCluster, 
											Cluster leafCluster, Cluster clus) {
		//System.out.println("\nTreeMixedCluster Process: " + previousPoint + " and leaf clus: " + leafCluster + " and other cluster " + clus);
		
		Point2D parentPt = previousPoint;
		//System.out.println("MixedCluster leafCluster " + leafCluster);
		Point2D onePt = leafCluster.getRenderedNode().getLocation();
		GraphicsGems.checkNaN(onePt);

		//twoPoint is a point between the parentPoint and the centerPoint
		Line2D parent2Center = new Line2D.Double(parentPt, clus.center);
		//System.out.println("MixedCluster parent2Center " + parentPt + " to " + clus.center);
		//System.out.println("MixedCluster: clus: " + clus + " " + clus.bounds + " " + parent2Center + " " + parentPt);
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
		if (leafCluster.getDefaultWeight() > clus.getDefaultWeight()) {
			biggerPt = onePt;
			biggerClus = leafCluster;
			smallerClus = clus;
		} else {
			biggerPt = twoPt;
			biggerClus = clus.twoCluster;
			smallerClus = clus.oneCluster;
		}
		
		double biggerDist = parentPt.distance(biggerPt);
	  	
		
		// if closerDist is small, bigFraction is 0, so we want
		// the parentFraction to be big.
		double bigFraction, parentFraction;
		if ((closerDist == 0) && (biggerDist == 0)) {
			bigFraction = 0;
			parentFraction = 1;
		} else {
			bigFraction = closerDist/biggerDist;
			parentFraction = 1 - bigFraction;
		}
		
		// set the newPt location to be newFraction of the way
		// between parentPt and biggerPt
		double x = parentPt.getX()*parentFraction+biggerPt.getX()*bigFraction;
		double y = parentPt.getY()*parentFraction+biggerPt.getY()*bigFraction;
		
		// get the associated node here for the cluster
		//System.out.println("dummyNode id: " + node.getID() + " currentLoc: " + node.getLocation() + " and new location: " + x + "," + y);
		
		
		Point2D newLocation = new Point2D.Double(x, y);
		node.setLocation(x, y);
		
		processTreeCluster(newLocation, leafCluster.getRenderedNode(), leafCluster);
		processTreeCluster(newLocation, clus.getRenderedNode(), clus);
		
	}
	
	/**
	 * Sets the location of node to be correct, taking into consideration the previous
	 * point's location and the fact that this node contains two clusters that are not leaves
	 * @param previousPoint the last point before this one in the layout tree
	 * @param node the node whose location we are setting
	 * @param clus the cluster that this node is going to
	 * @param layer the current layer
	 */
	private void processTreeTwoCluster(Point2D previousPoint, Node node, Cluster clus) {
		Cluster oneCluster = clus.oneCluster;
		Cluster twoCluster = clus.twoCluster;
		
		//System.out.println("\nTreeTwoCluster Process: " + previousPoint + " oneCluster: " + oneCluster + " twoCluster: " + twoCluster);
		//System.out.println("TreeTwoCluster Process: oneCenter" + oneCluster.center + " and twoCenter: " + twoCluster.center);
		
		Point2D parentPt = previousPoint;
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
		if (oneCluster.getDefaultWeight() > twoCluster.getDefaultWeight()) {
			biggerPt = onePt;
			biggerClus = oneCluster;
			smallerClus = twoCluster;
		} else {
			biggerPt = twoPt;
			biggerClus = twoCluster;
			smallerClus = oneCluster;
		}
		
		double biggerDist = parentPt.distance(biggerPt);
	  	
		
		// if closerDist is small, bigFraction is 0, so we want
		// the parentFraction to be big.
		
		double bigFraction, parentFraction;
		if ((closerDist == 0) && (biggerDist == 0)) {
			bigFraction = 0;
			parentFraction = 1;
		} else {
			bigFraction = closerDist/biggerDist;
			parentFraction = 1 - bigFraction;
		}
		
		// set the newPt location to be newFraction of the way
		// between parentPt and biggerPt
		double x = parentPt.getX()*parentFraction+biggerPt.getX()*bigFraction;
		double y = parentPt.getY()*parentFraction+biggerPt.getY()*bigFraction;
		
		
		// get the associated node here for the cluster
	
		//System.out.println("dummyNode id: " + node.getID() + "  currentLoc: " + node.getLocation() + " and new location: " + x + "," + y);
		
		Point2D newLocation = new Point2D.Double(x, y);
		node.setLocation(x, y);
	
		processTreeCluster(newLocation, oneCluster.getRenderedNode(), oneCluster);
		processTreeCluster(newLocation, twoCluster.getRenderedNode(), twoCluster);
		
	}
	

	
}
