package flowmap.swt.main;

import java.awt.Shape;
import java.awt.geom.PathIterator;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Path;

import edu.berkeley.guir.prefuse.VisualItem;
import edu.stanford.hci.flowmap.db.QueryRecord;
import edu.stanford.hci.flowmap.prefuse.item.FlowEdgeItem;
import edu.stanford.hci.flowmap.prefuse.item.FlowNodeItem;
import edu.stanford.hci.flowmap.prefuse.render.FlowScale;
import edu.stanford.hci.flowmap.prefuse.render.SimpleFlowEdgeRenderer;

public class HackishSWTFlowEdgeRenderer extends SimpleFlowEdgeRenderer implements SWTRenderer{

    public HackishSWTFlowEdgeRenderer(Options userOptions, QueryRecord flowRecord) {
        super(userOptions, flowRecord);
    }
    
    @Override
    public void renderSWT(GC gc, VisualItem vi) {
        renderHelperSWT(gc, (FlowEdgeItem) vi, getUserScaling());        
    }

    private void renderHelperSWT(GC gc, FlowEdgeItem edgeItem, FlowScale scale) {

        double displayWidth;
        FlowNodeItem n1, n2;
        n1 = (FlowNodeItem) edgeItem.getFirstNode();
        n2 = (FlowNodeItem) edgeItem.getSecondNode();
        if (((n1.getPrevControlPoint() == null) && (n1.getRoutingParent() == null) && 
                (n2.getPrevControlPoint() == null) && (n2.getRoutingParent() == null))) {
            displayWidth = computeStraightEdge(edgeItem, scale);
        } else {
            displayWidth = computeEdge(edgeItem, scale);
        }
        Shape shape = edgeItem.getShape();
        gc.setLineWidth((int) displayWidth);
        drawShape(gc, shape);
    }
    
    /**
     * Converts an AWT <code>Shape</code> into a SWT <code>Path</code>.
     * @param gc 
     *
     * @param shape  the shape (<code>null</code> not permitted).
     *
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
