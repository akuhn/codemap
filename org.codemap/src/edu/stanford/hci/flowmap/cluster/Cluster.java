package edu.stanford.hci.flowmap.cluster;



import org.codemap.util.geom.Line2D;
import org.codemap.util.geom.Point2D;
import org.codemap.util.geom.Rectangle2D;

import edu.stanford.hci.flowmap.structure.Node;

/**
 * This software is distributed under the Berkeley Software Distribution License.
 * Please see http://graphics.stanford.edu/~dphan/code/bsd.license.html
 */
public class Cluster {

	/** records the spatial extent of this cluster */
	public Rectangle2D bounds = null;
	/** the center of the cluster */
	public Point2D center = new Point2D.Double();
	
	/** backpointer to the enclosing cluster */
	public Cluster parentCluster = null;
	/** first child cluster */
	public Cluster oneCluster = null;
	/** second child cluster */
	public Cluster twoCluster = null;
	
	/** not-null if this cluster is just composed of a node. code uses test if node == null a lot */
	private Node leafNode = null;
	
	/** the node that would be drawn for this cluster. Every Cluster object should eventually
	 * have this assigned or created for them in the course of running the MultiRoot_ClusterLayout.
	 * This even includes Clusters where leafNode != null (to be consistent)
	 */
	private Node renderedNode = null;

    private double weight;
	
	/**
	 * Constructor for a real node item
	 * @param n - the real node item
	 */
	public Cluster(Node n) {
		center.setLocation(n.getLocation());
		leafNode = n;
		bounds = new Rectangle2D.Double(n.getLocation().getX()-5, n.getLocation().getY()-5, 10, 10);

		weight = n.getWeight();
		//System.out.println("Cluster() " + n.getQueryRow());
		
		renderedNode = new Node(n.getLocation());
		//renderedNode.setName(n.getName());
		
	}
	
	public Cluster(Cluster one, Cluster two) {
		oneCluster = one;
		twoCluster = two;
			
		// merging leaf nodes
		if ((one.leafNode != null) && (two.leafNode != null)) {
			// make a rectangle that bounds the two nodes
			Point2D onePt = one.leafNode.getLocation();
			bounds = new Rectangle2D.Double(onePt.getX(), onePt.getY(), 0, 0);
			bounds.add(two.leafNode.getLocation());
		} else { // merging non-leaf nodes
			bounds = new Rectangle2D.Double();	
			
			if (one.bounds != null && two.bounds != null) {
				bounds.setRect(one.bounds);
				bounds.add(two.bounds);
			} else if ((one.bounds == null) && (two.bounds == null)) {
				bounds.setRect(one.center.getX(), one.center.getY(), 0, 0);
				bounds.add(two.center);
			} else if (one.bounds != null) {
				bounds.setRect(one.bounds);
				bounds.add(two.center);
			} else {
				bounds.setRect(two.bounds);
				bounds.add(one.center);
			}
			
			// update the weight of the cluster
		}
		weight = one.getWeight() + two.getWeight();
		
		// create a new center point which is the center of the bounding box  
		// there are other ways to do this, but this is easy and consistent
		center.setLocation(bounds.getCenterX(), bounds.getCenterY());
	}
	
	public double getWeight() {
	    return weight;
	}
	
	public Point2D getCenter(){
		if(leafNode != null){
			return leafNode.getLocation();
		}else{
			return center;
		}
	}
	
	public Cluster getOtherChild(Cluster aChild){
		if(aChild == oneCluster){
			return twoCluster;
		}else if(aChild == twoCluster){
			return oneCluster;
		}else{
			return null;
		}
	}
	
	public boolean intersectWithLine(Line2D line) {
		if (bounds == null) {
			return (line.ptLineDist(center) == 0);				
		} else {
			return line.intersects(bounds);
		}
	}
	
	/**
	 * 
	 * @param other the other cluster
	 * @return distance to the other cluster
	 */
	public double distTo(Cluster other) {
		return center.distance(other.center);
	}
	
	public boolean equals(Object o) {
		Cluster c = (Cluster)o;
		if ((leafNode != null) && (c.leafNode != null)) {
			return leafNode.equals(c.leafNode);
		}
		else if ((oneCluster != null) && (c.oneCluster != null) && 
				(twoCluster != null) && (c.twoCluster != null)) {
			return (oneCluster.equals(c.oneCluster) && twoCluster.equals(c.twoCluster));
		}
		else
			return false;
		
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (leafNode != null) {
			sb.append(leafNode.getName() + " " + center.getX() + "," + center.getY());
		}
		
		if ((oneCluster != null) && (twoCluster != null)) {
			//sb.append(bounds.toString());
			sb.append("( ");
			sb.append(oneCluster.toString());
			sb.append(", ");
			sb.append(twoCluster.toString());
			sb.append(" )");
		}
		return sb.toString();
	}
	
	public void setLeafNode(Node n){
		leafNode = n;
	}
	
	public Node getLeafNode(){
		return leafNode;
	}
	
	public void setRenderedNode(Node renderN) {
		renderedNode = renderN;
	}

	public Node getRenderedNode() {
		if (leafNode != null) //return leafnode so it has the right name
			return leafNode;
		else
			return renderedNode;
		
	}
	
	public boolean isNodeCluster(){
		return (leafNode != null);
	}
} 
