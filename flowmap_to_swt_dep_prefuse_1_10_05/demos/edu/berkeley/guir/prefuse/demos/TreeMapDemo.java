package edu.berkeley.guir.prefuse.demos;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Comparator;
import java.util.Iterator;

import javax.swing.JFrame;

import edu.berkeley.guir.prefuse.Display;
import edu.berkeley.guir.prefuse.ItemRegistry;
import edu.berkeley.guir.prefuse.NodeItem;
import edu.berkeley.guir.prefuse.VisualItem;
import edu.berkeley.guir.prefuse.action.AbstractAction;
import edu.berkeley.guir.prefuse.action.RepaintAction;
import edu.berkeley.guir.prefuse.action.assignment.ColorFunction;
import edu.berkeley.guir.prefuse.action.filter.TreeFilter;
import edu.berkeley.guir.prefuse.activity.ActionList;
import edu.berkeley.guir.prefuse.event.ControlAdapter;
import edu.berkeley.guir.prefuse.graph.Graph;
import edu.berkeley.guir.prefuse.graph.io.HDirTreeReader;
import edu.berkeley.guir.prefuse.render.DefaultRendererFactory;
import edu.berkeley.guir.prefuse.render.ShapeRenderer;
import edu.berkeley.guir.prefuse.util.ColorMap;
import edu.berkeley.guir.prefusex.controls.PanControl;
import edu.berkeley.guir.prefusex.controls.ZoomControl;
import edu.berkeley.guir.prefusex.layout.SquarifiedTreeMapLayout;

/**
 * Demonstration showcasing a TreeMap layout of a hierarchical data
 * set and the use of a ColorMap to assign colors to items.
 *
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public class TreeMapDemo extends JFrame {

    public static final String TREE_CHI = "etc/chitest.hdir";
    
    private ItemRegistry registry;
    
    public TreeMapDemo() {
        super("prefuse TreeMap Demo");
        
        try {
            // load graph and initialize the item registry
            Graph g = (new HDirTreeReader()).loadTree(TREE_CHI);
            registry = new ItemRegistry(g);
            registry.setRendererFactory(new DefaultRendererFactory(
                new NodeRenderer(), null, null));
            // make sure we draw from larger->smaller to prevent
            // occlusion from parent node boxes
            registry.setItemComparator(new Comparator() {
                public int compare(Object o1, Object o2) {
                    double s1 = ((VisualItem)o1).getSize();
                    double s2 = ((VisualItem)o2).getSize();
                    return ( s1>s2 ? -1 : (s1<s2 ? 1 : 0));
                } //
            });
            
            // initialize our display
            Display display = new Display();
            display.setItemRegistry(registry);
            display.setUseCustomTooltips(true);
            PanControl  pH = new PanControl();
            ZoomControl zH = new ZoomControl();
            display.addMouseListener(pH);
            display.addMouseMotionListener(pH);
            display.addMouseListener(zH);
            display.addMouseMotionListener(zH);
            display.addControlListener(new ControlAdapter() {
               public void itemEntered(VisualItem item, MouseEvent e) {
                   Display d = (Display)e.getSource();
                   d.setToolTipText(item.getAttribute("label"));
               } //
               public void itemExited(VisualItem item, MouseEvent e) {
                   Display d = (Display)e.getSource();
                   d.setToolTipText(null);
               } //
            });
            display.setSize(700,700);
            
            // create the single filtering and layout action list
            ActionList filter = new ActionList(registry);
            filter.add(new TreeFilter(false));
            filter.add(new TreeMapSizeFunction());
            filter.add(new SquarifiedTreeMapLayout(4));
            filter.add(new TreeMapColorFunction());
            filter.add(new RepaintAction());
            
            // create and display application window
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            getContentPane().add(display, BorderLayout.CENTER);
            pack();
            setVisible(true);
            
            // filter graph and perform layout
            filter.runNow();
            
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    } //
    
    public static void main(String argv[]) {
        new TreeMapDemo();
    } //
    
    public class TreeMapColorFunction extends ColorFunction {
        Color c1 = new Color(0.5f,0.5f,0.f);
        Color c2 = new Color(0.5f,0.5f,1.f);
        ColorMap cmap = new ColorMap(ColorMap.getInterpolatedMap(10,c1,c2),0,9);
        public Paint getColor(VisualItem item) {
            return Color.WHITE;
        } //
        public Paint getFillColor(VisualItem item) {
            double v = (item instanceof NodeItem ? ((NodeItem)item).getDepth():0);
            return cmap.getColor(v);
        } //
    } // end of inner class TreeMapColorFunction
    
    public class TreeMapSizeFunction extends AbstractAction {
        public void run(ItemRegistry registry, double frac) {
            int leafCount = 0;
            Iterator iter = registry.getNodeItems();
            while ( iter.hasNext() ) {
                NodeItem n = (NodeItem)iter.next();
                if ( n.getChildCount() == 0 ) {
                    n.setSize(1.0);
                    NodeItem p = (NodeItem)n.getParent();
                    for (; p!=null; p=(NodeItem)p.getParent())
                        p.setSize(1.0+p.getSize());
                    leafCount++;
                }
            }
            
            Dimension d = registry.getDisplay(0).getSize();
            double area = d.width*d.height;
            double divisor = ((double)leafCount)/area;
            iter = registry.getNodeItems();
            while ( iter.hasNext() ) {
                NodeItem n = (NodeItem)iter.next();
                n.setSize(n.getSize()/divisor);
            }
            
            System.out.println("leafCount = " + leafCount);
        } //
    } // end of inner class TreeMapSizeFunction
    
    public class NodeRenderer extends ShapeRenderer {
        private Rectangle2D bounds = new Rectangle2D.Double();
        protected Shape getRawShape(VisualItem item) {
            Point2D d = (Point2D)item.getVizAttribute("dimension");
            if (d == null)
                System.out.println("uh-oh");
            bounds.setRect(item.getX(),item.getY(),d.getX(),d.getY());
            return bounds;
        } //
    } // end of inner class NodeRenderer
    
} // end of class TreeMapDemo
