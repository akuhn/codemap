package flowmap.swt.main;

import java.util.Collection;
import java.util.Iterator;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;

import edu.berkeley.guir.prefuse.ItemRegistry;
import edu.berkeley.guir.prefuse.VisualItem;
import edu.berkeley.guir.prefuse.activity.ActionList;
import edu.berkeley.guir.prefuse.render.Renderer;
import edu.stanford.hci.flowmap.cluster.ClusterLayout;
import edu.stanford.hci.flowmap.db.QueryRecord;
import edu.stanford.hci.flowmap.db.QueryRow;
import edu.stanford.hci.flowmap.prefuse.action.FlowMapLayoutAction;
import edu.stanford.hci.flowmap.prefuse.item.FlowDummyNodeItem;
import edu.stanford.hci.flowmap.prefuse.item.FlowEdgeItem;
import edu.stanford.hci.flowmap.prefuse.item.FlowRealNodeItem;
import edu.stanford.hci.flowmap.prefuse.render.FlowScale;
import edu.stanford.hci.flowmap.prefuse.render.FlowScale.Linear;
import edu.stanford.hci.flowmap.prefuse.structure.FlowDummyNode;
import edu.stanford.hci.flowmap.prefuse.structure.FlowEdge;
import edu.stanford.hci.flowmap.prefuse.structure.FlowMapStructure;
import edu.stanford.hci.flowmap.prefuse.structure.FlowNode;
import edu.stanford.hci.flowmap.structure.Edge;
import edu.stanford.hci.flowmap.structure.Graph;
import edu.stanford.hci.flowmap.structure.Node;


/**
 * This software is distributed under the Berkeley Software Distribution License.
 * Please see http://graphics.stanford.edu/~dphan/code/bsd.license.html
 *
 */
public class GraphPainter implements PaintListener {

	private ItemRegistry registry;
    private Options options;
    private QueryRecord flowRecord;
    private HackishSWTFlowEdgeRenderer hackishEdgeRenderer;
    private Graph graph;
    private SWTEdgeRenderer edgeRenderer;
    private FlowScale scale;

	public GraphPainter(QueryRecord queryRecord) {
		flowRecord = queryRecord;
		
		initOptions();		
		initRenderer();
		initRegistry();

		graph = createNodes(queryRecord);
        
//        prepareTheOldWay();
        prepareTheNewWay();
        
	}

    private void prepareTheNewWay() {
        Node root = graph.getRootNode();
        ClusterLayout clusterLayout = new ClusterLayout(root, graph.getAllNodes());
        clusterLayout.doLayout();
        edgeRenderer.initializeRenderTree(root);
    }

    private void prepareTheOldWay() {
        ActionList initFlowMap = new ActionList(registry);        
        FlowMapLayoutAction layoutAction = new FlowMapLayoutAction(
                options, graph.getRootNode(), graph.getAllNodes(),
                hackishEdgeRenderer);
        initFlowMap.add(layoutAction);
        initFlowMap.runNow();
    }

    private void initRenderer() {
        hackishEdgeRenderer = new HackishSWTFlowEdgeRenderer(options, flowRecord);
        scale = new FlowScale.Linear(options, flowRecord);        
        edgeRenderer = new SWTEdgeRenderer(options, flowRecord, scale);
    }

    private void initRegistry() {
        registry = new ItemRegistry(new FlowMapStructure());
		registry.addItemClass(FlowNode.class.getName(), FlowRealNodeItem.class);
		registry.addItemClass(FlowDummyNode.class.getName(), FlowDummyNodeItem.class);
		registry.addItemClass(FlowEdge.class.getName(), FlowEdgeItem.class);
		registry.setRendererFactory(new SWTRendererFactory(hackishEdgeRenderer));
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
        assert(flowRecord.getSourceRow() != null);
        Node rootNode = new Node(flowRecord.getSourceRow());
        rootNode.setRootNode(true);
        
        originalGraph.addNode(rootNode);
        originalGraph.setRootNode(rootNode);
        
        // add destinations
        QueryRow row;
        Node dest;
        Iterator i = flowRecord.getRowsIterator();
        
        while (i.hasNext()) {
            row = (QueryRow)i.next();
            
            // if the root node is a destination, just ignore it
            if (row.getName().equals(rootNode.getName())) {
                continue;
            }
            dest = new Node(row);
            originalGraph.addNode(dest);
        }
        return originalGraph;
    }	
	
    @Override
    public void paintControl(PaintEvent e) {
        paintTheNewWay(e);
//        paintTheOldWay(e);
    }

    private void paintTheNewWay(PaintEvent e) {
        for (Edge edge : graph.getEdges()) {
            edgeRenderer.renderSWT(e.gc, edge);
        }
    }

    private void paintTheOldWay(PaintEvent e) {
        Iterator items = registry.getItems();
        while (items.hasNext()) {
            VisualItem vi = (VisualItem) items.next();
            Renderer renderer = vi.getRenderer();
            // TODO: UHM ... NO! 
            if (renderer instanceof SWTRenderer) {
                ((SWTRenderer)renderer).renderSWT(e.gc, vi);
            }
        }
    }
}