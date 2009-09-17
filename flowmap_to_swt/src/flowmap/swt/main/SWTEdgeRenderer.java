package flowmap.swt.main;

import java.awt.BasicStroke;
import java.awt.Shape;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import javax.vecmath.Vector3d;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Path;

import edu.stanford.hci.flowmap.cluster.Vector2D;
import edu.stanford.hci.flowmap.prefuse.render.BezierSpline;
import edu.stanford.hci.flowmap.prefuse.render.FlowScale;
import edu.stanford.hci.flowmap.structure.Edge;
import edu.stanford.hci.flowmap.structure.Graph;
import edu.stanford.hci.flowmap.structure.Node;
import edu.stanford.hci.flowmap.utils.GraphicsGems;

public class SWTEdgeRenderer {
    
    private boolean m_additiveEdges = true;

    private Options options;
    private FlowScale scale;
    
    protected Point2D grandParent, parent, child, grandChild;
    protected Point2D fourPoints[];


    public SWTEdgeRenderer(Options options, FlowScale scale) {
        this.options = options;
        this.scale = scale;
        fourPoints = new Point2D[4];
    }

    public void renderSWT(GC gc, Edge edge) {
        double displayWidth;
        Node n1 = edge.getFirstNode();
        Node n2 = edge.getSecondNode();
        
        if (((n1.getPrevControlPoint() == null) && (n1.getRoutingParent() == null) && 
                (n2.getPrevControlPoint() == null) && (n2.getRoutingParent() == null))) {
            displayWidth = computeStraightEdge(edge);
        } else {
            displayWidth = computeEdge(edge);
        }
        Shape shape = edge.getShape();
        gc.setLineWidth((int) displayWidth);
        renderShape(gc, shape);
    }

    
   
    
    public void initializeRenderTree(Graph graph) {
        LinkedList<Node> nodeQeue = new LinkedList<Node>();
        nodeQeue.add(graph.getRootNode());
        
        while(nodeQeue.size() > 0) {
            Node nodeItem = nodeQeue.removeFirst();
            for(Edge each: nodeItem.getOutEdges()) {
                computeEdge(each);
                nodeQeue.add(each.getSecondNode());
            }
        }
    }
    
    protected double computeStraightEdge(Edge edge) {
        double displayWidth = scale.getDisplayWidth( edge.getWeight(), 
                options.getString(Options.CURRENT_FLOW_TYPE));
        Node n1 = edge.getFirstNode();
        Node n2 = edge.getSecondNode();
        Shape shape = edge.getShape();
        if ((shape == null) || !(shape instanceof Line2D)) {
            shape = new Line2D.Double(n1.getLocation(), n2.getLocation());
            edge.setShape(shape);
        } else {
            Line2D line = (Line2D)shape;
            line.setLine(n1.getLocation(), n2.getLocation());
            //System.out.println(n1.getLocation() + ", " + n2.getLocation());
        } 
        
        return displayWidth;
    }    
    
    protected double computeEdge(Edge edge) {
        Node n1;
        Node n2;
        //used as temporary node storage
        
        String currFlowType = options.getString(Options.CURRENT_FLOW_TYPE);
        
        //      Compute the display width here
        double edgeWeight = edge.getWeight();
        double displayWidth = scale.getDisplayWidth(edge.getWeight(), 
                currFlowType);
        displayWidth = Math.round(displayWidth);
        //System.out.println("SimpleEdgeRenderer edgeWeight " +
        // edgeItem.getWeight() + " displayWidth " + displayWidth );

        n1 = edge.getFirstNode();

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
                Point2D e2Pt = e2.getSecondNode()
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
            Edge otherEdgeItem = null;
            //System.out.println("n1.getOutEdges().size() " + n1.getOutEdges().size());
            for (Edge other: n1.getOutEdges()) {
                if ((otherEdgeItem = other) != edge) break;
            }
            assert(otherEdgeItem != null);
            
//          find the parent edge from n2 to n1
            Edge parentEdgeItem = null;
            for (Edge parent: n2.getOutEdges()) {
                parentEdgeItem = parent;
                if (parentEdgeItem.isIncident(n1) && (parentEdgeItem.isIncident(n2))) {
                    break;                
                }
            }
            assert(parentEdgeItem != null);
            
            Node item1;
            Node item2;
            // get vectors for both edges
            item1 = edge.getFirstNode();
            item2 = edge.getSecondNode();
            Vector2D thisEdgeVec = new Vector2D(item1.getLocation(), item2.getLocation());
            
            item1 = otherEdgeItem.getFirstNode();
            item2 = otherEdgeItem.getSecondNode();
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
            
            double parentWidth = scale.getDisplayWidth(parentEdgeItem.getWeight(), currFlowType);
            double otherWidth = scale.getDisplayWidth(otherEdgeItem.getWeight(), currFlowType);
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
        child = edge.getSecondNode().getLocation();

        // 7. Compute the grand child catmull-rom point.
        // It is either the position of the heavier child, 
        // or if there are no more child nodes, it is in a straight
        // line with parent and child, just further out
        Collection<Edge> grandChildEdges = edge.getSecondNode().getOutEdges();
        if (grandChildEdges.size() != 0) {
            double gcWeight, gcX, gcY;
            gcX = gcY = gcWeight = -1;
            
            // find the heaviest child
            for(Edge gcEdge: grandChildEdges) {
                if (gcEdge.getWeight() > gcWeight) {
                    gcWeight = gcEdge.getWeight();
                    gcX = gcEdge.getSecondNode().getLocation().getX();
                    gcY = gcEdge.getSecondNode().getLocation().getY();
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
            
        //  System.out.println(pToCDir);

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

        

        // 8. Now that we know where all the catmull-rom objects are,
        // construct a BasicStroke object with e1's thickness
        BasicStroke bs = new BasicStroke((float) displayWidth,
                BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND);

        // 9. construct a CubicCurve object with the given control points
        // by passing the four points object to the BezierSpline.computeSplines
        // function
        fourPoints[0] = grandParent;
        fourPoints[1] = parent;
        fourPoints[2] = child;
        fourPoints[3] = grandChild;

        Shape shape = edge.getShape();
        CubicCurve2D myCurve;
        if (shape == null) {
            myCurve = new CubicCurve2D.Double();
            edge.setShape(myCurve);
        } else {
            myCurve = (CubicCurve2D)shape;
        }
        
        BezierSpline.computeOneSpline(grandParent, parent, child, grandChild, myCurve);

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
        Node otherItem = edge.getSecondNode();
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

    /* 
     * JFreeChart : a free chart library for the Java(tm) platform
     * 
     *
     * (C) Copyright 2000-2009, by Object Refinery Limited and Contributors.
     *
     * Project Info:  http://www.jfree.org/jfreechart/index.html
     *
     * This library is free software; you can redistribute it and/or modify it
     * under the terms of the GNU Lesser General Public License as published by
     * the Free Software Foundation; either version 2.1 of the License, or
     * (at your option) any later version.
     *
     * This library is distributed in the hope that it will be useful, but
     * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
     * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
     * License for more details.
     *
     * You should have received a copy of the GNU Lesser General Public
     * License along with this library; if not, write to the Free Software
     * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
     * USA.
     *
     * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
     * in the United States and other countries.]
     *
     * ------------------
     * SWTGraphics2D.java
     * ------------------
     * (C) Copyright 2006-2008, by Henry Proudhon and Contributors.
     *
     * Original Author:  Henry Proudhon (henry.proudhon AT ensmp.fr);
     * Contributor(s):   Cedric Chabanois (cchabanois AT no-log.org);
     *                   David Gilbert (for Object Refinery Limited);
     *                   Ronnie Duan (see bug report 2583891);
     *
     * Changes
     * -------
     * 14-Jun-2006 : New class (HP);
     * 29-Jan-2007 : Fixed the fillRect method (HP);
     * 31-Jan-2007 : Moved the dummy JPanel to SWTUtils.java,
     *               implemented the drawLine method (HP);
     * 07-Apr-2007 : Dispose some of the swt ressources,
     *               thanks to silent for pointing this out (HP);
     * 23-May-2007 : Removed resource leaks by adding a resource pool (CC);
     * 15-Jun-2007 : Fixed compile error for JDK 1.4 (DG);
     * 22-Oct-2007 : Implemented clipping (HP);
     * 22-Oct-2007 : Implemented some AlphaComposite support (HP);
     * 23-Oct-2007 : Added mechanism for storing RenderingHints (which are
     *               still ignored at this point) (DG);
     * 23-Oct-2007 : Implemented drawPolygon(), drawPolyline(), drawOval(),
     *               fillOval(), drawArc() and fillArc() (DG);
     * 27-Nov-2007 : Implemented a couple of drawImage() methods (DG);
     * 18-Nov-2008 : Check for GradientPaint in setPaint() method (DG);
     * 27-Feb-2009 : Implemented fillPolygon() - see bug 2583891 (DG);
     *
     */    
    /**
     * Converts an AWT <code>Shape</code> into a SWT <code>Path</code>.
     * copied from: http://www.java2s.com/Code/Java/SWT-JFace-Eclipse/DrawGraphics2Dstuffonaswtcomposite.htm
     */
    private void renderShape(GC gc, Shape shape) {
        int type;
        float[] coords = new float[6];
        Path path = new Path(gc.getDevice());
        PathIterator pit = shape.getPathIterator(null);
        while (!pit.isDone()) {
            type = pit.currentSegment(coords);
            switch (type) {
                case (PathIterator.SEG_MOVETO):
                    path.moveTo(coords[0], coords[1]);
                    break;
                case (PathIterator.SEG_LINETO):
                    path.lineTo(coords[0], coords[1]);
                    break;
                case (PathIterator.SEG_QUADTO):
                    path.quadTo(coords[0], coords[1], coords[2], coords[3]);
                    break;
                case (PathIterator.SEG_CUBICTO):
                    path.cubicTo(coords[0], coords[1], coords[2],
                            coords[3], coords[4], coords[5]);
                    break;
                case (PathIterator.SEG_CLOSE):
                    path.close();
                    break;
                default:
                    break;
            }
            pit.next();
        }
        gc.drawPath(path);
    }      
    
}
