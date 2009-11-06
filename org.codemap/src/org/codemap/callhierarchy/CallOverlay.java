package org.codemap.callhierarchy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codemap.callhierarchy.vizualization.EdgeRenderer;
import org.codemap.callhierarchy.vizualization.Options;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;

import ch.deif.meander.Location;
import ch.deif.meander.MapInstance;
import ch.deif.meander.map.MapValues;
import ch.deif.meander.swt.SWTLayer;
import edu.stanford.hci.flowmap.cluster.ClusterLayout;
import edu.stanford.hci.flowmap.prefuse.render.FlowScale;
import edu.stanford.hci.flowmap.structure.Edge;
import edu.stanford.hci.flowmap.structure.Graph;

public class CallOverlay extends SWTLayer {
    
    private CallModel model;
    private Integer mapSize;
    private Options options = initOptions(); 
    private List<Graph> graphs;
    private ArrayList<RenderHelper> renderers;
    
    public CallOverlay(CallModel flowModel) {
        model = flowModel;
    }

    @Override
    public void paintMap(MapValues map, GC gc) {
        if (! model.isEnabled()) return;
        if (map.mapSize.getValue() != mapSize || model.isDirty() || !isInitialized()) {
            mapSize = map.mapSize.getValue();
            model.setClean();
            
            MapInstance value = map.mapInstance.getValue();
            // map not yet available
            if (value == null) return;
            Iterable<Location> locations = value.locations();
            createGraphs(locations);
            createRenderers();
        }
        
        gc.setAlpha(255);
        Color white = gc.getDevice().getSystemColor(SWT.COLOR_WHITE);
        gc.setForeground(white);
        gc.setBackground(white);
        for (RenderHelper each: renderers) {
            each.renderEdges(gc);
        }
    }

    private boolean isInitialized() {
        return graphs != null && renderers != null;
    }
    
    private void createRenderers() {
        renderers = new ArrayList<RenderHelper>();
        for (Graph each: graphs) {
            renderers.add(new RenderHelper(each, options));
        }
        for (RenderHelper each: renderers) {
            each.prepare();
        }
    }

    private Options initOptions() {
        Options opts = new Options();
        opts.putDouble(Options.MIN_DISPLAY_WIDTH, 1);
        opts.putDouble(Options.MAX_DISPLAY_WIDTH, 10);
        // make sure to have only one scale boolean set to true
        opts.putBoolean(Options.LINEAR_SCALE, true);
        return opts;
    }
    
    private void createGraphs(Iterable<Location> locations) {
        Map<String, Location> byName = new HashMap<String, Location>();
        for(Location each: locations) {
            byName.put(each.getDocument(), each);
        }
        GraphConversionVisitor visitor = new GraphConversionVisitor(byName);
        model.accept(visitor);
        graphs = visitor.getGraphs();
    }   
    
}

class RenderHelper {

    private EdgeRenderer renderer;
    private Graph graph;

    public RenderHelper(Graph graph, Options options) {
        this.graph = graph;
        renderer = new EdgeRenderer(new FlowScale.Identity(options, graph));
    }

    public void renderEdges(GC gc) {
        for (Edge edge : graph.getEdges()) {
            renderer.renderEdge(gc, edge);
        }
    }

    public void prepare() {
        // if there is just the root node clustering will raise an
        // assertion error, so don't cluster at all
        if (graph.getAllNodes().size() <= 1) return;
        if(graph.getAllNodes().size() == 2){
            System.out.println();
        }
        try {
            ClusterLayout clusterLayout = new ClusterLayout(graph);
            clusterLayout.doLayout();
        } catch (AssertionError e) {
            System.out.println();
        }
        
//      if (Globals.runNodeEdgeRouting) {
//          //System.out.println("Adjusting Edge Routing");
//          NodeEdgeRouting router = new NodeEdgeRouting(newRoot);
//          router.routeNodes();
//      }
        renderer.initializeRenderTree(graph);
    }
    
}