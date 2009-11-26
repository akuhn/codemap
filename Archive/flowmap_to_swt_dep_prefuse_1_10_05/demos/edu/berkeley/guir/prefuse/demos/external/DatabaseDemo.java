package edu.berkeley.guir.prefuse.demos.external;

import java.awt.Color;
import java.awt.Paint;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
import edu.berkeley.guir.prefuse.action.filter.GraphEdgeFilter;
import edu.berkeley.guir.prefuse.activity.ActionList;
import edu.berkeley.guir.prefuse.event.FocusEvent;
import edu.berkeley.guir.prefuse.event.FocusListener;
import edu.berkeley.guir.prefuse.event.ItemRegistryListener;
import edu.berkeley.guir.prefuse.graph.DefaultGraph;
import edu.berkeley.guir.prefuse.graph.Entity;
import edu.berkeley.guir.prefuse.graph.Graph;
import edu.berkeley.guir.prefuse.graph.Node;
import edu.berkeley.guir.prefuse.graph.event.GraphLoaderListener;
import edu.berkeley.guir.prefuse.graph.external.DatabaseLoader;
import edu.berkeley.guir.prefuse.graph.external.ExternalNode;
import edu.berkeley.guir.prefuse.graph.external.ExternalTreeNode;
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
 * Mar 12, 2004 - jheer - Created class
 *
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public class DatabaseDemo extends JFrame {

    private ItemRegistry   registry;
    private ActionList forces, filter;
    private DatabaseLoader loader;
    
    private static final String sQuery = "select * from nodes where id = ";
    private static final String nQuery = "select nodes.* from nodes, edges where " +
            "(edges.id1 = ? AND nodes.id = edges.id2) OR  (edges.id2 = ? AND nodes.id = edges.id1)";
    private static final String cQuery = "select nodes.* from nodes, edges where edges.id1 = ? AND nodes.id = edges.id2";
    private static final String pQuery = "select nodes.* from nodes, edges where edges.id2 = ? AND nodes.id = edges.id1";
    
    public static void main(String[] argv) {
        new DatabaseDemo();
    } //
    
    public DatabaseDemo() {
        super("DatabaseDemo");
        try {
        
        //Tree t = new DefaultTree();
        Graph g = new DefaultGraph();
        registry = new ItemRegistry(g);
        registry.addItemRegistryListener(new ItemRegistryListener() {
            public void registryItemAdded(VisualItem item) {}
            public void registryItemRemoved(VisualItem item) {
                //System.out.println("registry remove: "+item);
            }
        });
        
        loader = new DatabaseLoader(registry, new String[] {"id","label","value"}) {
            protected void prepareNeighborQuery(PreparedStatement s, ExternalNode n) {
                try {
                    s.clearParameters();
                    int id = Integer.parseInt(n.getAttribute("id"));
                    s.setInt(1, id);
                    s.setInt(2, id);
                } catch ( SQLException e ) { e.printStackTrace(); }
            } //
            protected void prepareChildrenQuery(PreparedStatement s, ExternalTreeNode n) {
                try {
                    s.clearParameters();
                    s.setInt(1, Integer.parseInt(n.getAttribute("id")));
                } catch ( SQLException e ) { e.printStackTrace(); }
            } //
            protected void prepareParentQuery(PreparedStatement s, ExternalTreeNode n) {
                try {
                    s.clearParameters();
                    s.setInt(1, Integer.parseInt(n.getAttribute("id")));
                } catch ( SQLException e ) { e.printStackTrace(); }
            } //
        };
        loader.setNeighborQuery(nQuery);
        loader.setChildrenQuery(cQuery);
        loader.setParentQuery(pQuery);
        loader.connect("com.mysql.jdbc.Driver",
            "jdbc:mysql://localhost/trial","jheer","msql-121");
        loader.addGraphLoaderListener(new GraphLoaderListener() {
            int unloaded = 0;
            public void entityLoaded(GraphLoader loader, Entity e) {
                System.out.println("loaded - "+e);
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
        
        // initialize renderers
        TextItemRenderer    nodeRenderer = new TextItemRenderer();
        nodeRenderer.setRenderType(TextItemRenderer.RENDER_TYPE_FILL);
        nodeRenderer.setRoundedCorner(8,8);
        nodeRenderer.setTextAttributeName("label");
        DefaultEdgeRenderer edgeRenderer = new DefaultEdgeRenderer();    
        registry.setRendererFactory(new DefaultRendererFactory(
                nodeRenderer, edgeRenderer, null));
        
        // initialize force simulator and action lists
        filter = new ActionList(registry);
        filter.add(new FisheyeGraphFilter(-1));
        filter.add(new GraphEdgeFilter());
        
        ForceSimulator fsim = new ForceSimulator();
        fsim.addForce(new NBodyForce(-0.4f, -1f, 0.9f));
        fsim.addForce(new SpringForce(5E-5f, 150f));
        fsim.addForce(new DragForce(-0.005f));
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
        
        // load the root of the graph from the database
        Statement s = loader.getConnection().createStatement();
        ResultSet rs = s.executeQuery(sQuery+"1"); rs.first();
        Node root = loader.loadNode(GraphLoader.LOAD_NEIGHBORS,rs,null);
        g.addNode(root);
        //t.setRoot((TreeNode)root);
        
        // initialize the display
        Display display = new Display();
        display.setItemRegistry(registry);
        display.setSize(800,700);
        display.pan(350,350);
        display.addControlListener(new FocusControl(2));
        display.addControlListener(new NeighborHighlightControl());
        display.addControlListener(new DragControl(false));
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
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    } //
    
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
    
} // end of class DatabaseDemo
