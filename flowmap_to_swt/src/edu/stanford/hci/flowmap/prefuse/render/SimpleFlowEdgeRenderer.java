package edu.stanford.hci.flowmap.prefuse.render;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.Iterator;

import javax.vecmath.Vector3d;

import edu.berkeley.guir.prefuse.graph.Edge;
import edu.stanford.hci.flowmap.cluster.Vector2D;
import edu.stanford.hci.flowmap.db.QueryRecord;
import edu.stanford.hci.flowmap.main.Globals;
import edu.stanford.hci.flowmap.main.Options;
import edu.stanford.hci.flowmap.prefuse.item.FlowEdgeItem;
import edu.stanford.hci.flowmap.prefuse.item.FlowNodeItem;
import edu.stanford.hci.flowmap.utils.GoodColorChooser;
import edu.stanford.hci.flowmap.utils.GraphicsGems;
import edu.stanford.hci.flowmap.utils.GraphicsUtilities;
import edu.stanford.hci.flowmap.utils.StrokeUtilities;


/**
 * This software is distributed under the Berkeley Software Distribution License.
 * Please see http://graphics.stanford.edu/~dphan/code/bsd.license.html
 *
 */
public class SimpleFlowEdgeRenderer extends FlowEdgeRenderer {
	
	private boolean m_additiveEdges = true;
	
	private boolean m_straightEdges = false;

	protected Point2D grandParent, parent, child, grandChild;

	protected Point2D fourPoints[];

	public SimpleFlowEdgeRenderer(Options userOptions, QueryRecord flowRecord) {
		super(userOptions, flowRecord);
		fourPoints = new Point2D[4];
	}

	protected void renderHelper(Graphics2D g2d, FlowEdgeItem edgeItem,
			FlowScale scale) {

		double displayWidth;
		
		// anti-alias the edges
		g2d.setRenderingHints(GraphicsUtilities.getGreatRenderingHints());
		
		switch(Globals.currentType) {
		case FOOTPRINT:
			g2d.setColor(GoodColorChooser.goodEdgeColors[3]);
			break;
		case INTERACTIVE:
			g2d.setColor(Globals.currentColor);
			break;
		case EXPLORE:
			g2d.setColor(GoodColorChooser.goodEdgeColors[2]);
			break;
			
		}
		
		FlowNodeItem n1, n2;
		n1 = (FlowNodeItem) edgeItem.getFirstNode();
		n2 = (FlowNodeItem) edgeItem.getSecondNode();
		if (m_straightEdges || ((n1.getPrevControlPoint() == null) && (n1.getRoutingParent() == null) && 
				(n2.getPrevControlPoint() == null) && (n2.getRoutingParent() == null))) {
			displayWidth = computeStraightEdge(edgeItem, scale);
		} else {
			displayWidth = computeEdge(edgeItem, scale);
		}
		
		if(edgeItem.isHighlighted()){
			g2d.setColor(Color.BLUE);
		}
		
		Shape shape = edgeItem.getShape();
		g2d.setStroke(StrokeUtilities.retrieveStroke((float) displayWidth));

		g2d.draw(shape);
/*
		if (shape instanceof CubicCurve2D) {
			CubicCurve2D myCurve = (CubicCurve2D) shape;

			// uncomment to see control points
			g2d.setStroke(new BasicStroke(1));
			g2d.setColor(Color.red);
			g2d.drawOval((int) myCurve.getP1().getX(), (int) myCurve.getP1()
					.getY(), 3, 3);
			g2d.fillOval((int) myCurve.getP1().getX(), (int) myCurve.getP1()
					.getY(), 3, 3);

			g2d.setColor(Color.blue);
			g2d.drawOval((int) myCurve.getCtrlP1().getX(), (int) myCurve
					.getCtrlP1().getY(), 3, 3);
			g2d.fillOval((int) myCurve.getCtrlP1().getX(), (int) myCurve
					.getCtrlP1().getY(), 3, 3);

			g2d.setColor(Color.magenta);
			g2d.drawOval((int) myCurve.getCtrlP2().getX(), (int) myCurve
					.getCtrlP2().getY(), 3, 3);
			g2d.fillOval((int) myCurve.getCtrlP2().getX(), (int) myCurve
					.getCtrlP2().getY(), 3, 3);

			g2d.setColor(Color.green);
			g2d.drawOval((int) myCurve.getP2().getX(), (int) myCurve.getP2()
					.getY(), 3, 3);
			g2d.fillOval((int) myCurve.getP2().getX(), (int) myCurve.getP2()
					.getY(), 3, 3);
			g2d.setColor(Color.black);
		}
		*/
		

	}
	
	protected double computeStraightEdge(FlowEdgeItem edgeItem, FlowScale scale) {
		FlowNodeItem n1, n2; //used as temporary node storage

		double displayWidth = scale.getDisplayWidth(
				edgeItem.getWeight(userOptions.getString(Options.CURRENT_FLOW_TYPE)), 
				userOptions.getString(Options.CURRENT_FLOW_TYPE));
		n1 = (FlowNodeItem) edgeItem.getFirstNode();
		n2 = (FlowNodeItem) edgeItem.getSecondNode();
		Shape shape = edgeItem.getShape();
		if ((shape == null) || !(shape instanceof Line2D)) {
			shape = new Line2D.Double(n1.getLocation(), n2.getLocation());
			edgeItem.setShape(shape);
		} else {
			Line2D line = (Line2D)shape;
			line.setLine(n1.getLocation(), n2.getLocation());
			//System.out.println(n1.getLocation() + ", " + n2.getLocation());
		} 
		
		return displayWidth;
	}

	protected double computeEdge(FlowEdgeItem edgeItem, FlowScale scale) {
		FlowNodeItem n1, n2; //used as temporary node storage
		Iterator i;
		
		String currFlowType = userOptions.getString(Options.CURRENT_FLOW_TYPE);
		
		//		Compute the display width here
		double edgeWeight = edgeItem.getWeight(currFlowType);
		double displayWidth = scale.getDisplayWidth(edgeItem.getWeight(currFlowType), 
				currFlowType);
		displayWidth = Math.round(displayWidth);
		//System.out.println("SimpleEdgeRenderer edgeWeight " +
		// edgeItem.getWeight() + " displayWidth " + displayWidth );

		n1 = (FlowNodeItem) edgeItem.getFirstNode();

		// 1. set the grandParent catmull-rom point. This is the parent of n1
		n2 = n1.getRoutingParent();
		if (n2 == null) {
			//System.out.println("n2 is null");
			Point2D n1Pt = n1.getLocation();
			// if the grandParent doesn't exist (this is the root),
			// then we want to construct a grandParent point that is suitable.
			// we do this by taking the average position of all the children
			// constructing the vector between the node and those children
			// flipping the vector, and moving it away from the parent point
			Iterator gpIter = n1.getOutEdges().iterator();
			
			//System.out.println("n1.outEdges: " + n1.getOutEdges().size());
			double avgX, avgY;
			int count = 0;
			avgX = avgY = 0;
			while (gpIter.hasNext()) {
				Edge e2 = (Edge) gpIter.next();
				Point2D e2Pt = ((FlowNodeItem) e2.getSecondNode())
						.getLocation();
				//System.out.println("averaging points: " + e2Pt);
				avgX += e2Pt.getX();
				avgY += e2Pt.getY();
				count++;
			}

			avgX /= count;
			avgY /= count;
			Vector2D gpVec = new Vector2D(n1Pt, new Point2D.Double(avgX, avgY));
			Point2D gpVecNorm = gpVec.getNormalized();
			gpVecNorm.setLocation(-1 * gpVecNorm.getX(), -1 * gpVecNorm.getY());
			grandParent = new Point2D.Double(n1Pt.getX() + 10
					* gpVecNorm.getX(), n1Pt.getY() + 10 * gpVecNorm.getY());
			

		} else {
			//System.out.println("n2 is not null");
			grandParent = n1.getPrevControlPoint();
			//System.out.println("Prev Control Point for " +
			// ((FlowNode)n1.getEntity()).toStringId() + " has prev control
			// point: " + grandParent);
		}
		//System.out.println("grandparent: " + grandParent);

		// 2. the parent catmull-rom point is n1, since we want it to
		// go through this point
		parent = n1.getLocation();
		
		Point2D shiftPoint = null;
		// additive edge code
		// only shift edges if we are not at the root
		// relies on the fact that there only 2 edges per node
		if (m_additiveEdges && (n1.getRoutingParent() != null)) {

			n2 = n1.getRoutingParent(); //redundant, but who cares
			
			// get the other edge that starts from n1
			Iterator childIter = n1.getOutEdges().iterator();
			FlowEdgeItem otherEdgeItem = null;
			//System.out.println("n1.getOutEdges().size() " + n1.getOutEdges().size());
			while (childIter.hasNext()) {
				otherEdgeItem = (FlowEdgeItem) childIter.next();
				//System.out.println("Flow Renderer2: " + otherEdgeItem);
				if (otherEdgeItem != edgeItem)
					break;
			}
			assert(otherEdgeItem != null);
			
			
//			find the parent edge from n2 to n1
			childIter = n2.getOutEdges().iterator();
			FlowEdgeItem parentEdgeItem = null;
			while (childIter.hasNext()) {
				parentEdgeItem = (FlowEdgeItem) childIter.next();
				if (parentEdgeItem.isIncident(n1) && (parentEdgeItem.isIncident(n2)))
					break;
			}
			assert(parentEdgeItem != null);
			
			// get vectors for both edges
			FlowNodeItem item1, item2;
			item1 = (FlowNodeItem) edgeItem.getFirstNode();
			item2 = (FlowNodeItem) edgeItem.getSecondNode();
			Vector2D thisEdgeVec = new Vector2D(item1.getLocation(), item2.getLocation());
			
			item1 = (FlowNodeItem) otherEdgeItem.getFirstNode();
			item2 = (FlowNodeItem) otherEdgeItem.getSecondNode();
			Vector2D otherEdgeVec = new Vector2D(item1.getLocation(), item2.getLocation());
			
			// now do the cross product of thisEdgeVec
			Vector3d thisEdgeVector = new Vector3d(thisEdgeVec.getNormalized().getX(), -1*thisEdgeVec.getNormalized().getY(), 0);
			Vector3d otherEdgeVector = new Vector3d(otherEdgeVec.getNormalized().getX(), -1*otherEdgeVec.getNormalized().getY(), 0);
			
			Vector3d result = new Vector3d();
			result.cross(thisEdgeVector, otherEdgeVector);
			//System.out.println("edges: " + parentEdgeItem + ", " + edgeItem + " , " + otherEdgeItem);
			//System.out.println(result);
			
			// compute the perpendicular vector, the direction to shift
			// we will get a vector that points to the right
			Vector2D grandToParent = new Vector2D(grandParent, parent);
			Point2D shiftDir = new Point2D.Double(-grandToParent.getNormalized().getY(), 
					-grandToParent.getNormalized().getX()); 
			
			if (result.z < 0) {
			// if z > 0 then we know that thisEdge is to the right of otherEdge
			// so we push this in the direction of shiftDir

		    // else we know that thisEdge is to the left of otherEdge
			// so we push this up, or in the negative direction of shiftDir
				shiftDir.setLocation(shiftDir.getX()*-1, shiftDir.getY()*-1);
			}
	
			
			
			double parentWidth = scale.getDisplayWidth(parentEdgeItem.getWeight(currFlowType), currFlowType);
			double otherWidth = scale.getDisplayWidth(otherEdgeItem.getWeight(currFlowType), currFlowType);
			parentWidth = Math.round(parentWidth);
			otherWidth = Math.round(otherWidth);
	/*
			System.out.println("edges: par: " + parentEdgeItem + ", edge:" + edgeItem + " , other:" + otherEdgeItem);
			System.out.println("parentActual: " + parentEdgeItem.getWeight(currFlowType) + " dispActual: " + edgeItem.getWeight(currFlowType) + " otherActual: " + otherEdgeItem.getWeight(currFlowType));
			System.out.println("parentWidth: " + parentWidth + " dispWidth: " + displayWidth + " otherWidth: " + otherWidth);
		*/	
			assert(parentWidth >= displayWidth);
			
			// compute the distance to shift this edge. It should be:
			// parentDisplayWidth/2 - displayWidth/2
			double shiftDist = (parentWidth/2) - (displayWidth/2); 
			
			shiftPoint = new Point2D.Double(shiftDist*shiftDir.getX(), 
					-1*shiftDist*shiftDir.getY());

		}

		// 6. Set the child catmull-rom pont to be e1.to
		child = ((FlowNodeItem) edgeItem.getSecondNode()).getLocation();

		// 7. Compute the grand child catmull-rom point.
		// It is either the position of the heavier child, 
		// or if there are no more child nodes, it is in a straight
		// line with parent and child, just further out
		Collection grandChildEdges = ((FlowNodeItem) edgeItem.getSecondNode())
				.getOutEdges();
		if (grandChildEdges.size() != 0) {
			double gcWeight, gcX, gcY;
			gcX = gcY = gcWeight = -1;

			// find the heaviest child
			Iterator grandChildIter = grandChildEdges.iterator();
			while (grandChildIter.hasNext()) {
				FlowEdgeItem gcEdge = (FlowEdgeItem) grandChildIter.next();
				//System.out.println("flowrender " + gcEdge);
				if (gcEdge.getWeight(currFlowType) > gcWeight) {
					gcWeight = gcEdge.getWeight(currFlowType);
					gcX = ((FlowNodeItem) gcEdge.getSecondNode()).getLocation().getX();
					gcY = ((FlowNodeItem) gcEdge.getSecondNode()).getLocation().getY();
					//System.out.println("x: " + gcX + " y:" + gcY);
				}
			}			
			assert(gcWeight != -1);
			grandChild = new Point2D.Double(gcX, gcY);
			GraphicsGems.checkNaN(grandChild);
		} else {
			/*
			System.out.println();
			System.out.println(grandParent);
			System.out.println(parent);
			System.out.println(child);
			*/
			Vector2D parentToChild = new Vector2D(parent, child);
			Point2D pToCDir = parentToChild.getNormalized();
			
		//	System.out.println(pToCDir);

			Vector2D grandParentToParent = new Vector2D(grandParent, parent);
			Point2D gpToPDir = grandParentToParent.getNormalized();
			//System.out.println(gpToPDir);

			double angleBetween = parentToChild
					.absAngleBetween(grandParentToParent);
			
			//System.out.println(edgeItem + " angleBetween is " + (180*angleBetween/Math.PI));

			// now rotate the parentToChild normalized vector by -angleBetween
			// assuming rotations are CCW

			double x = pToCDir.getX() * Math.cos(-angleBetween)
					- pToCDir.getY() * Math.sin(-angleBetween);
			double y = pToCDir.getX() * Math.sin(-angleBetween)
					+ pToCDir.getY() * Math.cos(-angleBetween);

			//System.out.println("grandChild case 2: " + x + "," + y);
			grandChild = new Point2D.Double(child.getX() + 100 * x, child
					.getY()
					+ 100 * y);
			GraphicsGems.checkNaN(grandChild);

		}

		

		// 8. Now that we know where all the catmull-rom objcts are,
		// construct a BasicStroke object with e1's thickness
		BasicStroke bs = new BasicStroke((float) displayWidth,
				BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND);

		// 9. construct a CubicCurve object with the given control points
		// by passing the four ponits object to the BezierSpline.computeSplines
		// function
		fourPoints[0] = grandParent;
		fourPoints[1] = parent;
		fourPoints[2] = child;
		fourPoints[3] = grandChild;

		Shape shape = edgeItem.getShape();
		CubicCurve2D myCurve;
		if (shape == null) {
			myCurve = new CubicCurve2D.Double();
			edgeItem.setShape(myCurve);
		} else {
			myCurve = (CubicCurve2D)shape;
		}
		
		BezierSpline.computeOneSpline(grandParent, parent, child, grandChild,
				myCurve);

		// 11. Now do some tweaking of the control points to ensure continuity.
		// need to tweak the first control point to be collinear with the parent
		// and the grandparent and have the same distance as the
		// parent-grandparent
		double parentGrandDist = grandParent.distance(parent);

		Vector2D grandToParent = new Vector2D(grandParent, parent);
		//System.out.println(grandToParent);
		//now take the grandToParent vector, scale by parentGrandDistance, add
		// it to the parent vector
		
		  Point2D collinShift = new Point2D.Double(parentGrandDist
				* grandToParent.getNormalized().getX() + parent.getX(),
				parentGrandDist * grandToParent.getNormalized().getY()
						+ parent.getY());
		//System.out.println("before: " + myCurve.getP1() + ", "
		// +myCurve.getCtrlP1() + ", " + myCurve.getCtrlP2() + ", " +
		// myCurve.getP2() );
		myCurve.setCurve(myCurve.getP1(), collinShift, myCurve.getCtrlP2(),
				myCurve.getP2());
		GraphicsGems.checkNaN(myCurve.getP1());
		GraphicsGems.checkNaN(myCurve.getP2());
		GraphicsGems.checkNaN(myCurve.getCtrlP1());
		GraphicsGems.checkNaN(myCurve.getCtrlP2());
		
		
		
		//System.out.println("after: " + myCurve.getP1() + ", "
		// +myCurve.getCtrlP1() + ", " + myCurve.getCtrlP2() + ", " +
		// myCurve.getP2() );

		//System.out.println("CollinShift dist: " +
		// collinShift.distance(parent) + " and p-GP dist: " + parentGrandDist);

		// 10. Need to set the previous control point for the child node
		FlowNodeItem otherItem = (FlowNodeItem) edgeItem.getSecondNode();
		otherItem.setPrevControlPoint(myCurve.getCtrlP2());

		//System.out.println("Prev Control Point for " +
		// ((FlowNode)otherItem.getEntity()).toStringId() + " was set to prev
		// control point: " + grandParent);
		
		if ((m_additiveEdges) && (shiftPoint != null)) {
			
		
			// 12. Now shift the bezier points to line up properly with the horizontal translation
			// that takes into account the thickness of the curve. We do this by shifting
			// the first two points of every bezier to get it to line up in the appropriate place
			Point2D grandParentShift = new Point2D.Double(myCurve.getP1().getX()+shiftPoint.getX(),
				myCurve.getP1().getY()+shiftPoint.getY());
			Point2D parentShift = new Point2D.Double(myCurve.getCtrlP1().getX()+shiftPoint.getX(),
				myCurve.getCtrlP1().getY()+shiftPoint.getY());
			myCurve.setCurve(grandParentShift, parentShift, myCurve.getCtrlP2(), myCurve.getP2());
		}
		GraphicsGems.checkNaN(myCurve.getP1());
		GraphicsGems.checkNaN(myCurve.getP2());
		GraphicsGems.checkNaN(myCurve.getCtrlP1());
		GraphicsGems.checkNaN(myCurve.getCtrlP2());

		return displayWidth;
	}
}