package edu.berkeley.guir.prefuse.demos;

import java.awt.geom.Rectangle2D;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JFrame;

import edu.berkeley.guir.prefuse.Display;
import edu.berkeley.guir.prefuse.ItemRegistry;
import edu.berkeley.guir.prefuse.NodeItem;
import edu.berkeley.guir.prefuse.VisualItem;
import edu.berkeley.guir.prefuse.action.ActionMap;
import edu.berkeley.guir.prefuse.action.RepaintAction;
import edu.berkeley.guir.prefuse.action.assignment.ColorFunction;
import edu.berkeley.guir.prefuse.action.assignment.Layout;
import edu.berkeley.guir.prefuse.action.filter.GraphFilter;
import edu.berkeley.guir.prefuse.activity.ActionList;
import edu.berkeley.guir.prefuse.activity.ActivityMap;
import edu.berkeley.guir.prefuse.event.FocusEvent;
import edu.berkeley.guir.prefuse.event.FocusListener;
import edu.berkeley.guir.prefuse.graph.Graph;
import edu.berkeley.guir.prefuse.graph.GraphLib;
import edu.berkeley.guir.prefuse.graph.Node;
import edu.berkeley.guir.prefuse.render.DefaultEdgeRenderer;
import edu.berkeley.guir.prefuse.render.DefaultRendererFactory;
import edu.berkeley.guir.prefuse.render.TextItemRenderer;
import edu.berkeley.guir.prefusex.controls.DragControl;
import edu.berkeley.guir.prefusex.controls.FocusControl;
import edu.berkeley.guir.prefusex.controls.NeighborHighlightControl;
import edu.berkeley.guir.prefusex.controls.ZoomingPanControl;

/**
 * Demonstration illustrating the use of a zooming pan control to
 *  navigate a large space.
 *
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public class ZoomingPanDemo extends JFrame {

    private ItemRegistry registry;
    private ActivityMap  activityMap = new ActivityMap();
    private ActionMap    actionMap   = new ActionMap();
    
    public static void main(String argv[]) {
        new ZoomingPanDemo();
    } //
    
    public ZoomingPanDemo() {
        super("ZoomingPan Demo");
        
        Graph g = GraphLib.getGrid(30,30);
        registry = new ItemRegistry(g);

        Display display = new Display();
        display.setItemRegistry(registry);
        display.setSize(600,600);
        display.setBorder(BorderFactory.createEmptyBorder(50,50,50,50));
        display.addControlListener(new DragControl());
        display.addControlListener(new NeighborHighlightControl());
        display.addControlListener(new FocusControl(0));
        display.addControlListener(new ZoomingPanControl());
        
        registry.setRendererFactory(new DefaultRendererFactory(
            new TextItemRenderer() {
                public int getRenderType(VisualItem item) {
                    return RENDER_TYPE_FILL;
                } //
            },
            new DefaultEdgeRenderer(), 
            null));
        
        ActionList filter = new ActionList(registry);
        filter.add(new GraphFilter());
        filter.add(actionMap.put("grid", new GridLayout()));
        
        final ActionList update = new ActionList(registry);
        update.add(new ColorFunction());
        update.add(new RepaintAction());
        
        ((Layout)actionMap.get("grid")).setLayoutBounds(
                new Rectangle2D.Double(-1200,-1200,2400,2400));
        
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().add(display);
        pack();
        setVisible(true);
        
        registry.getDefaultFocusSet().addFocusListener(
            new FocusListener() {
                public void focusChanged(FocusEvent e) {
                    update.runNow();
                } //  
            }
        );
        
        // run filter and layout
        filter.runNow(); update.runNow();
    } //
    
    class GridLayout extends Layout {
        public void run(ItemRegistry registry, double frac) {
            Rectangle2D b = getLayoutBounds(registry);
            double bx = b.getMinX(), by = b.getMinY();
            double w = b.getWidth(), h = b.getHeight();
            int m, n;
            Graph g = (Graph)registry.getGraph();
            Iterator iter = g.getNodes(); iter.next();
            for ( n=2; iter.hasNext(); n++ ) {
                Node nd = (Node)iter.next();
                if ( nd.getEdgeCount() == 2 )
                    break;
            }
            m = g.getNodeCount() / n;
            iter = g.getNodes();
            for ( int i=0; iter.hasNext(); i++ ) {
                Node nd = (Node)iter.next();
                NodeItem ni = registry.getNodeItem(nd);
                double x = bx + w*((i%n)/(double)(n-1));
                double y = by + h*((i/n)/(double)(m-1));
                
                // add some jitter, just for fun
                x += (Math.random()-0.5)*(w/n);
                y += (Math.random()-0.5)*(h/m);
                
                setLocation(ni,null,x,y);
            }
        } //
    } // end of inner class GridLayout
    
} // end of class ZoomingPanDemo
