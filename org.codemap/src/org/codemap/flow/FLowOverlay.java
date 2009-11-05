package org.codemap.flow;

import java.util.HashMap;
import java.util.Map;

import org.codemap.flow.vizualization.EdgeRenderer;
import org.codemap.flow.vizualization.Options;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;

import ch.deif.meander.Location;
import ch.deif.meander.map.MapValues;
import ch.deif.meander.swt.SWTLayer;
import edu.stanford.hci.flowmap.cluster.ClusterLayout;
import edu.stanford.hci.flowmap.prefuse.render.FlowScale;
import edu.stanford.hci.flowmap.structure.Edge;
import edu.stanford.hci.flowmap.structure.Graph;
import edu.stanford.hci.flowmap.structure.Node;

public class FLowOverlay extends SWTLayer {
    
    private FlowModel model;
    private Integer mapSize;
    private Graph graph;
    private Options options;
    private EdgeRenderer edgeRenderer;
    
    public FLowOverlay(FlowModel flowModel) {
        model = flowModel;
    }

    @Override
    public void paintMap(MapValues map, GC gc) {
        if (map.mapSize.getValue() != mapSize || model.needsUpdate() || graph == null) {
            mapSize = map.mapSize.getValue();
            model.setNeedsNoUpdate();
            
            Iterable<Location> locations = map.mapInstance.getValue().locations();
            graph = createGraph(locations);
            options = initOptions();        
            edgeRenderer = initRenderer();
            prepare();
        }
        
        gc.setAlpha(255);
        Color white = gc.getDevice().getSystemColor(SWT.COLOR_WHITE);
        gc.setForeground(white);
        gc.setBackground(white);
        for (Edge edge : graph.getEdges()) {
            edgeRenderer.renderEdge(gc, edge);
        }
    }
    
    private void prepare() {
        // if there is just the root node clustering will raise an
        // assertion error, so don't cluster at all
        if (graph.getAllNodes().size() <= 1) return;
        
        ClusterLayout clusterLayout = new ClusterLayout(graph);
        clusterLayout.doLayout();
        
//      if (Globals.runNodeEdgeRouting) {
//          //System.out.println("Adjusting Edge Routing");
//          NodeEdgeRouting router = new NodeEdgeRouting(newRoot);
//          router.routeNodes();
//      }
        edgeRenderer.initializeRenderTree(graph);
    }

    private EdgeRenderer initRenderer() {
        return new EdgeRenderer(new FlowScale.Linear(options, graph));
    }

    private Options initOptions() {
        Options opts = new Options();
        opts.putDouble(Options.MIN_DISPLAY_WIDTH, 1);
        opts.putDouble(Options.MAX_DISPLAY_WIDTH, 10);
        // make sure to have only one scale boolean set to true
        opts.putBoolean(Options.LINEAR_SCALE, true);
        return opts;
    }
    
    private Graph createGraph(Iterable<Location> locations) {
        
        Map<String, Location> byName = new HashMap<String, Location>();
        for(Location each: locations) {
            byName.put(each.getDocument(), each);
        }
        GraphConversionVisitor visitor = new GraphConversionVisitor(byName);
        model.accept(visitor);
        return visitor.createGraph();
    }   
    
}
