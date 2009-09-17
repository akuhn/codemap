package flowmap.swt.main;

import java.awt.geom.Point2D;
import java.util.Iterator;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;

import edu.stanford.hci.flowmap.cluster.ClusterLayout;
import edu.stanford.hci.flowmap.db.QueryRecord;
import edu.stanford.hci.flowmap.db.QueryRow;
import edu.stanford.hci.flowmap.prefuse.render.FlowScale;
import edu.stanford.hci.flowmap.structure.Edge;
import edu.stanford.hci.flowmap.structure.Graph;
import edu.stanford.hci.flowmap.structure.Node;


/**
 * This software is distributed under the Berkeley Software Distribution License.
 * Please see http://graphics.stanford.edu/~dphan/code/bsd.license.html
 *
 */
public class GraphPainter implements PaintListener {

    private Options options;
    private Graph graph;
    private SWTEdgeRenderer edgeRenderer;
    private FlowScale scale;

	public GraphPainter(QueryRecord queryRecord) {
		initOptions();		
		initRenderer(queryRecord);

		graph = createNodes(queryRecord);
        prepareTheNewWay();
        
	}

    private void prepareTheNewWay() {
        ClusterLayout clusterLayout = new ClusterLayout(graph);
        clusterLayout.doLayout();
        
//      if (Globals.runNodeEdgeRouting) {
//      //System.out.println("Adjusting Edge Routing");
//      NodeEdgeRouting router = new NodeEdgeRouting(newRoot);
//      router.routeNodes();
//  }
        edgeRenderer.initializeRenderTree(graph);
    }

    private void initRenderer(QueryRecord flowRecord) {
        scale = new FlowScale.Linear(options, flowRecord);        
        edgeRenderer = new SWTEdgeRenderer(options, scale);
    }

    private void initOptions() {
        options = new Options();
        options.putDouble(Options.MIN_DISPLAY_WIDTH, 1);
        options.putDouble(Options.MAX_DISPLAY_WIDTH, 10);
        // make sure to have only one scale boolean set to true
        options.putBoolean(Options.LINEAR_SCALE, true);
    }
    
    private Graph createNodes(QueryRecord flowRecord) {
        // says that we should read the width of the splines from the field
        // 'Value' stored in the scheme of each node.
        options.putString(Options.CURRENT_FLOW_TYPE, flowRecord.getSourceRow().getRowSchema().getDefaultValueId());
        
        Graph originalGraph = new Graph();
        // add root
        Node rootNode = new Node(200, 200, 0, "root");
        rootNode.setRootNode(true);
        
        originalGraph.addNode(rootNode);
        originalGraph.setRootNode(rootNode);
        
        double values[][] = {
                {0, 0, 1},
                {0, 300, 1},
                {300, 0, 1},
                {500, 0, 10},
                {500, 150, 10},
                {300, 200, 5},
                {400, 250, 25},
                {400, 350, 25},                                
                {550, 300, 30},
                {600, 350, 30},                                
                {100, 400, 20}, 
                {200, 500, 10}, 
                {400, 600, 40}, 
                {550, 650, 30}, 
                {600, 400, 40}
            };
        
        int index = 0;
        for (double[] each: values) {
            Node dest = new Node(each[0], each[1], each[2], "node" + ++index);
            originalGraph.addNode(dest);            
        }        
        return originalGraph;
    }	

    @Override
    public void paintControl(PaintEvent e) {
        paintTheNewWay(e);
    }

    private void paintTheNewWay(PaintEvent e) {
        for (Edge edge : graph.getEdges()) {
            edgeRenderer.renderSWT(e.gc, edge);
        }
    }
}