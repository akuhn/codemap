package flowmap.swt.main;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;

import edu.stanford.hci.flowmap.cluster.ClusterLayout;
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

    private final Options options;
    private final Graph graph;
    private final SWTEdgeRenderer edgeRenderer;

	public GraphPainter() {
	    graph = createGraph();
		options = initOptions();		
		edgeRenderer = initRenderer();
        // says that we should read the width of the splines from the field
        // 'Value' stored in the scheme of each node.
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

    private SWTEdgeRenderer initRenderer() {
        return new SWTEdgeRenderer(new FlowScale.Linear(options, graph));
    }

    private Options initOptions() {
        Options opts = new Options();
        opts.putDouble(Options.MIN_DISPLAY_WIDTH, 1);
        opts.putDouble(Options.MAX_DISPLAY_WIDTH, 10);
        // make sure to have only one scale boolean set to true
        opts.putBoolean(Options.LINEAR_SCALE, true);
        return opts;
    }
    
    private Graph createGraph() {
        Graph originalGraph = new Graph();
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