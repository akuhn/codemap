package edu.berkeley.guir.prefuse.demos;

import java.awt.geom.Rectangle2D;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JFrame;

import edu.berkeley.guir.prefuse.Display;
import edu.berkeley.guir.prefuse.ItemRegistry;
import edu.berkeley.guir.prefuse.VisualItem;
import edu.berkeley.guir.prefuse.action.ActionMap;
import edu.berkeley.guir.prefuse.action.RepaintAction;
import edu.berkeley.guir.prefuse.action.assignment.ColorFunction;
import edu.berkeley.guir.prefuse.action.assignment.Layout;
import edu.berkeley.guir.prefuse.action.filter.GraphFilter;
import edu.berkeley.guir.prefuse.activity.ActionList;
import edu.berkeley.guir.prefuse.activity.ActivityMap;
import edu.berkeley.guir.prefuse.graph.Graph;
import edu.berkeley.guir.prefuse.graph.GraphLib;
import edu.berkeley.guir.prefuse.render.DefaultRendererFactory;
import edu.berkeley.guir.prefuse.render.TextItemRenderer;
import edu.berkeley.guir.prefusex.controls.AnchorUpdateControl;
import edu.berkeley.guir.prefusex.controls.FocusControl;
import edu.berkeley.guir.prefusex.distortion.FisheyeDistortion;

/**
 * 
 *
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public class FisheyeMenuDemo extends JFrame {

    private ActivityMap  activityMap = new ActivityMap();
    private ActionMap    actionMap   = new ActionMap();
    
    public FisheyeMenuDemo() {
        super("FisheyeMenu");
        
        Graph g = GraphLib.getNodes(201);
        
        ItemRegistry registry = new ItemRegistry(g);
        
        TextItemRenderer nodeRenderer = new TextItemRenderer();
        nodeRenderer.setRenderType(TextItemRenderer.RENDER_TYPE_NONE);
        nodeRenderer.setHorizontalPadding(0);
        nodeRenderer.setVerticalPadding(1);
        nodeRenderer.setHorizontalAlignment(TextItemRenderer.ALIGNMENT_LEFT);
        registry.setRendererFactory(new DefaultRendererFactory(
            nodeRenderer, null, null));
        
        Display display = new Display(registry);
        display.setSize(100,470);
        display.setBorder(BorderFactory.createEmptyBorder(5,15,5,5));
        display.addControlListener(new FocusControl(0));
        
        ActionList init = new ActionList(registry);
        init.add(new GraphFilter());
        init.add(new VerticalLineLayout());
        init.add(new RepaintAction());

        ActionList distort = new ActionList(registry);
        FisheyeDistortion feye = new FisheyeDistortion(0,8);
        distort.add(feye);
        distort.add(new ColorFunction());
        distort.add(new RepaintAction());
        
        // create and display application window
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().add(display);
        pack();
        setVisible(true);
        
        init.runNow();
        
        // enable distortion mouse-over
        AnchorUpdateControl auc = new AnchorUpdateControl(feye, distort);
        display.addMouseListener(auc);
        display.addMouseMotionListener(auc);
    } //
    
    public static void main(String[] args) {
        new FisheyeMenuDemo();
    } //
    
    public class VerticalLineLayout extends Layout {
        public void run(ItemRegistry registry, double frac) {
            // first pass
            double h = 0;
            Iterator iter = registry.getNodeItems();
            while ( iter.hasNext() ) {
                VisualItem item = (VisualItem)iter.next();
                h += item.getBounds().getHeight();
            }
            
            Rectangle2D bounds = getLayoutBounds(registry);
            double scale = bounds.getHeight() / h;
            
            // second pass
            h = bounds.getMinY();
            double ih, y, x=bounds.getMinX();
            iter = registry.getNodeItems();
            while ( iter.hasNext() ) {
                VisualItem item = (VisualItem)iter.next();
                item.updateSize(scale);
                item.setSize(scale);
                ih = item.getBounds().getHeight();
                y = h+(ih/2);
                setLocation(item,null,x,y);
                h += ih;
            }
        }
    } //
    
} // end of class FisheyeMenuDemo
