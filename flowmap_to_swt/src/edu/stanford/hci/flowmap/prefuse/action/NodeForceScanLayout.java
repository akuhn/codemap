package edu.stanford.hci.flowmap.prefuse.action;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import edu.stanford.hci.flowmap.structure.Node;

/**
 * Implements the Force Scan algorithm by Kazuo Misue et al.
 *
 * "Layout Adjustment and the Mental Map"
 *
 * This software is distributed under the Berkeley Software Distribution License.
 * Please see http://graphics.stanford.edu/~dphan/code/bsd.license.html
 *
 */
public class NodeForceScanLayout {

	
	private Collection<Node> allNodes;
	
	public NodeForceScanLayout(Collection<Node> allNodes) {
		this.allNodes = allNodes;
	}
	
	public void shiftNodes() {
		//System.out.println("Adjusting node positions");
			
		Comparator<Node> verticalComp = new Comparator<Node>() {
			// returns  -1 if o1.y < o2.y
			// returns 0 if o1.y == o2.y
			// returns 1 if o1.y > o2.y
			public int compare(Node o1, Node o2) {
				Point2D pt1 = o1.getLocation();
				Point2D pt2 = o2.getLocation();
				if (pt1.getY() < pt2.getY())
					return -1;
				else if (pt1.getY() > pt2.getY())
					return 1;
				else
					return 0;
			}
			
		};
		
		Comparator<Node> horizontalComp = new Comparator<Node>() {
			
			// returns  -1 if o1.x < o2.x
			// returns 0 if o1.x == o2.x
			// returns 1 if o1.x > o2.x
			public int compare(Node o1, Node o2) {
				Point2D pt1 = o1.getLocation();
				Point2D pt2 = o2.getLocation();
				if (pt1.getX() < pt2.getX())
					return -1;
				else if (pt1.getX() > pt2.getX())
					return 1;
				else
					return 0;
			}
		};
		
		ArrayList<Node> verticalSort = new ArrayList<Node>();
		
		ArrayList<Node> horizontalSort = new ArrayList<Node>();
		
		
		for(Node n : allNodes) {
			verticalSort.add(n);
			horizontalSort.add(n);
		}
		
		Collections.sort(verticalSort, verticalComp);
		Collections.sort(horizontalSort, horizontalComp);
		
		horizontalScan(horizontalSort);
		verticalScan(verticalSort);

		
	}
	
	private void horizontalScan(ArrayList<Node> list) {
		Node nodeItem;
		double prevX, currX;
		int prevXIndex;
		double delta;
		Point2D location;
		
		double minSeparation = 20;
		
		nodeItem =  list.get(0);
		prevX = nodeItem.getLocation().getX();
		prevXIndex = 0;
		delta = 0;
		for(int i=1; i<list.size(); i++) {
			nodeItem = list.get(i);
			currX = nodeItem.getLocation().getX();
			if (currX == prevX) { //find all the nodes with the same x
				continue;
			} else {
				// compute delta, the minimum separating distance.
				delta = Math.max(0,minSeparation - (currX-prevX));
								
				// update the location of the next group
				prevX = currX;
				prevXIndex = i;
				
				for (int j=i; j < list.size(); j++) {
					nodeItem = list.get(j);
					location = nodeItem.getLocation();
					nodeItem.setLocation(location.getX() + delta, location.getY());
				}
			}
		}
	}
	
	private void verticalScan(ArrayList<Node> list) {
		Node nodeItem;
		double prevY, currY;
		int prevYIndex;
		double delta;
		Point2D location;
		
		double minSeparation = 20;
		
		nodeItem = list.get(0);
		prevY = nodeItem.getLocation().getY();
		prevYIndex = 0;
		delta = 0;
		for(int i=1; i<list.size(); i++) {
			nodeItem = list.get(i);
			currY = nodeItem.getLocation().getY();
			if (currY == prevY) { //find all the nodes with the same x
				continue;
			} else {
				// compute delta, the minimum separating distance.
				delta = Math.max(0,minSeparation - (currY-prevY));
								
				// update the location of the next group
				prevY = currY;
				prevYIndex = i;
				
				for (int j=i; j < list.size(); j++) {
					nodeItem = list.get(j);
					location = nodeItem.getLocation();
					nodeItem.setLocation(location.getX(), location.getY()+delta);
				}
			}
		}
	}
	
	
}
