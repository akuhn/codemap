package flowmap.swt.main;

import java.awt.Shape;
import java.awt.geom.PathIterator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Path;

import edu.berkeley.guir.prefuse.VisualItem;
import edu.stanford.hci.flowmap.db.QueryRecord;
import edu.stanford.hci.flowmap.main.Options;
import edu.stanford.hci.flowmap.prefuse.item.FlowEdgeItem;
import edu.stanford.hci.flowmap.prefuse.item.FlowNodeItem;
import edu.stanford.hci.flowmap.prefuse.render.FlowScale;
import edu.stanford.hci.flowmap.prefuse.render.SimpleFlowEdgeRenderer;

public class SWTFlowEdgeRenderer extends SimpleFlowEdgeRenderer {

    private boolean m_additiveEdges = true;
    private boolean m_straightEdges = false;

    public SWTFlowEdgeRenderer(Options userOptions, QueryRecord flowRecord) {
        super(userOptions, flowRecord);
    }

    public void renderSWT(GC gc, VisualItem vi) {
        renderHelper(gc, (FlowEdgeItem) vi, getUserScaling());        
    }

    private void renderHelper(GC gc, FlowEdgeItem edgeItem, FlowScale scale) {

        double displayWidth;
        
        // anti-alias the edges
        gc.setAntialias(SWT.ON);
        
//        switch(Globals.currentType) {
//        case FOOTPRINT:
//            g2d.setColor(GoodColorChooser.goodEdgeColors[3]);
//            break;
//        case INTERACTIVE:
//            g2d.setColor(Globals.currentColor);
//            break;
//        case EXPLORE:
//            g2d.setColor(GoodColorChooser.goodEdgeColors[2]);
//            break;
//        }
        
        FlowNodeItem n1, n2;
        n1 = (FlowNodeItem) edgeItem.getFirstNode();
        n2 = (FlowNodeItem) edgeItem.getSecondNode();
        if (m_straightEdges || ((n1.getPrevControlPoint() == null) && (n1.getRoutingParent() == null) && 
                (n2.getPrevControlPoint() == null) && (n2.getRoutingParent() == null))) {
            displayWidth = computeStraightEdge(edgeItem, scale);
        } else {
            displayWidth = computeEdge(edgeItem, scale);
        }
        
//        if(edgeItem.isHighlighted()){
//            g2d.setColor(Color.BLUE);
//        }
        
        Shape shape = edgeItem.getShape();
        gc.setLineWidth((int) displayWidth);
//        g2d.setStroke(StrokeUtilities.retrieveStroke((float) displayWidth));
        drawShape(gc, shape);
    }
    
    /**
     * Converts an AWT <code>Shape</code> into a SWT <code>Path</code>.
     * @param gc 
     *
     * @param shape  the shape (<code>null</code> not permitted).
     *
     * @return The path.
     */
    private void drawShape(GC gc, Shape shape) {
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
