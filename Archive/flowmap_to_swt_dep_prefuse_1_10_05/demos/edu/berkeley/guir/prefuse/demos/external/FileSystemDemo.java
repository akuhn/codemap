package edu.berkeley.guir.prefuse.demos.external;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Paint;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Iterator;

import javax.swing.JFrame;

import edu.berkeley.guir.prefuse.Display;
import edu.berkeley.guir.prefuse.EdgeItem;
import edu.berkeley.guir.prefuse.VisualItem;
import edu.berkeley.guir.prefuse.ItemRegistry;
import edu.berkeley.guir.prefuse.NodeItem;
import edu.berkeley.guir.prefuse.action.AbstractAction;
import edu.berkeley.guir.prefuse.action.RepaintAction;
import edu.berkeley.guir.prefuse.action.assignment.ColorFunction;
import edu.berkeley.guir.prefuse.action.filter.FisheyeGraphFilter;
import edu.berkeley.guir.prefuse.action.filter.TreeEdgeFilter;
import edu.berkeley.guir.prefuse.activity.ActionList;
import edu.berkeley.guir.prefuse.event.ControlAdapter;
import edu.berkeley.guir.prefuse.event.FocusEvent;
import edu.berkeley.guir.prefuse.event.FocusListener;
import edu.berkeley.guir.prefuse.graph.DefaultTree;
import edu.berkeley.guir.prefuse.graph.Entity;
import edu.berkeley.guir.prefuse.graph.Node;
import edu.berkeley.guir.prefuse.graph.Tree;
import edu.berkeley.guir.prefuse.graph.TreeNode;
import edu.berkeley.guir.prefuse.graph.event.GraphLoaderListener;
import edu.berkeley.guir.prefuse.graph.external.FileSystemLoader;
import edu.berkeley.guir.prefuse.graph.external.GraphLoader;
import edu.berkeley.guir.prefuse.render.DefaultEdgeRenderer;
import edu.berkeley.guir.prefuse.render.DefaultRendererFactory;
import edu.berkeley.guir.prefuse.render.TextItemRenderer;
import edu.berkeley.guir.prefusex.controls.DragControl;
import edu.berkeley.guir.prefusex.controls.FocusControl;
import edu.berkeley.guir.prefusex.controls.NeighborHighlightControl;
import edu.berkeley.guir.prefusex.controls.PanControl;
import edu.berkeley.guir.prefusex.controls.ZoomControl;
import edu.berkeley.guir.prefusex.force.DragForce;
import edu.berkeley.guir.prefusex.force.ForceSimulator;
import edu.berkeley.guir.prefusex.force.NBodyForce;
import edu.berkeley.guir.prefusex.force.SpringForce;
import edu.berkeley.guir.prefusex.layout.ForceDirectedLayout;

/**
 *
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public class FileSystemDemo extends JFrame {

    private ItemRegistry   registry;
    private ActionList forces, filter;
    private FileSystemLoader loader;
    
    public static void main(String[] argv) {
        new FileSystemDemo();
    } //
    
    public FileSystemDemo() {
        super("File System Demo");
        
        Tree t = new DefaultTree();
        registry = new ItemRegistry(t);
        
        loader = new FileSystemLoader(registry);
        Node root = loader.loadNode(GraphLoader.LOAD_CHILDREN, 
                                    null, new File("."));
        t.setRoot((TreeNode)root);
        
        loader.addGraphLoaderListener(new GraphLoaderListener() {
            int unloaded = 0;
            public void entityLoaded(GraphLoader loader, Entity e) {
                forces.cancel();
                filter.runNow();
                forces.runNow();
            } //
            public void entityUnloaded(GraphLoader loader, Entity e) {
                System.out.println((++unloaded)+" unloaded - "+e);
                forces.cancel();
                filter.runNow();
                forces.runNow();
            } //
        });
        
        ForceSimulator fsim = new ForceSimulator();
        fsim.addForce(new NBodyForce(-0.4f, -1f, 0.9f));
        fsim.addForce(new SpringForce(5E-5f, 150f));
        fsim.addForce(new DragForce(-0.005f));
        
        TextItemRenderer    nodeRenderer = new TextItemRenderer();
        nodeRenderer.setRenderType(TextItemRenderer.RENDER_TYPE_FILL);
        nodeRenderer.setRoundedCorner(8,8);
        nodeRenderer.setTextAttributeName("label");
        DefaultEdgeRenderer edgeRenderer = new DefaultEdgeRenderer();    
        registry.setRendererFactory(new DefaultRendererFactory(
                nodeRenderer, edgeRenderer, null));
        
        filter = new ActionList(registry);
        filter.add(new FisheyeGraphFilter(-3));
        filter.add(new TreeEdgeFilter());
        
        forces = new ActionList(registry,-1,20);
        forces.add(new AbstractAction() {
            public void run(ItemRegistry registry, double frac) {
                Iterator iter = registry.getNodeItems();
                while ( iter.hasNext() ) {
                    NodeItem item = (NodeItem)iter.next();
                    if ( item.isFocus() )
                        item.setFixed(true);
                }
            } //
        });
        forces.add(new ForceDirectedLayout(fsim, false, false) {
            protected float getSpringLength(NodeItem n1, NodeItem n2) {
                if (n1.getEdgeCount() == 1 || n2.getEdgeCount() == 1)
                    return 75.f;
                double doi = Math.max(n1.getDOI(), n2.getDOI());
                return 200.f/Math.abs((float)doi-1);
            } //
        });
        forces.add(new DemoColorFunction());
        forces.add(new RepaintAction());
        
        Display display = new Display();
        display.setItemRegistry(registry);
        display.setSize(800,700);
        display.pan(350,350);
        display.addControlListener(new FocusControl(2));
        display.addControlListener(new NeighborHighlightControl());
        display.addControlListener(new DragControl(false));
        display.addControlListener(new MouseOverControl());
        display.addControlListener(new PanControl(false));
        display.addControlListener(new ZoomControl(false));
        registry.getDefaultFocusSet().addFocusListener(new FocusListener() {
            public void focusChanged(FocusEvent e) {
                NodeItem n = registry.getNodeItem((Node)e.getFirstRemoved());
                if ( n != null ) n.setFixed(false);
                filter.runNow();
            } //
        });
        registry.getDefaultFocusSet().set(root);
        
        
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().add(display);
        pack();
        setVisible(true);
        
        // wait until graphics are available
        while ( display.getGraphics() == null );
        filter.runNow();
        forces.runNow();
    }
    
    public class DemoColorFunction extends ColorFunction {
        private Color pastelRed = new Color(255,125,125);
        private Color pastelOrange = new Color(255,200,125);
        private Color lightGray = new Color(220,220,255);
        public Paint getColor(VisualItem item) {
            if ( item instanceof EdgeItem ) {
                if ( item.isHighlighted() )
                    return pastelOrange;
                else
                    return Color.LIGHT_GRAY;
            } else {
                return Color.BLACK;
            }
        } //
        public Paint getFillColor(VisualItem item) {
            if ( item.isHighlighted() )
                return pastelOrange;
            else if ( item instanceof NodeItem ) {
                if ( item.isFixed() )
                    return pastelRed;
                else
                    return lightGray;
            } else {
                return Color.BLACK;
            }
        } //        
    } // end of inner class DemoColorFunction
    
    /**
     * Tags and fixes the node under the mouse pointer.
     */
    public class MouseOverControl extends ControlAdapter {
        
        private boolean wasFixed = false;
        
        public void itemEntered(VisualItem item, MouseEvent e) {
            ((Display)e.getSource()).setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            wasFixed = item.isFixed();
            item.setFixed(true);
        } //
        
        public void itemExited(VisualItem item, MouseEvent e) {
            ((Display)e.getSource()).setCursor(Cursor.getDefaultCursor());
            item.setFixed(wasFixed);
        } //
        
        public void itemReleased(VisualItem item, MouseEvent e) {
            item.setFixed(wasFixed);
        } //
        
    } // end of inner class FocusControl
    
} // end of class FileSystemDemo
