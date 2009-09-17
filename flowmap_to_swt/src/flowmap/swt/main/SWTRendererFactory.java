package flowmap.swt.main;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import edu.berkeley.guir.prefuse.EdgeItem;
import edu.berkeley.guir.prefuse.VisualItem;
import edu.berkeley.guir.prefuse.render.Renderer;
import edu.berkeley.guir.prefuse.render.RendererFactory;
import edu.stanford.hci.flowmap.prefuse.render.SimpleFlowEdgeRenderer;

public class SWTRendererFactory implements RendererFactory {
    
    Renderer emptyRenderer = new Renderer() {
        
        @Override
        public void render(Graphics2D g, VisualItem item) {
           // do nothing
        }
        
        @Override
        public boolean locatePoint(Point2D p, VisualItem item) {
            return false;
        }
        
        @Override
        public Rectangle2D getBoundsRef(VisualItem item) {
            return new Rectangle(-1,-1,0,0);
        }
    };    

    private SimpleFlowEdgeRenderer edgeRenderer;

    public SWTRendererFactory(SimpleFlowEdgeRenderer mEdgeRenderer) {
        edgeRenderer = mEdgeRenderer;
    }

    @Override
    public Renderer getRenderer(VisualItem item) {
        if(item instanceof EdgeItem ) {           
            return edgeRenderer;
        }
        return emptyRenderer;
    }

}
