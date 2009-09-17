package flowmap.swt.main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Iterator;

import javax.swing.JPanel;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Point;

import edu.berkeley.guir.prefuse.Display;
import edu.berkeley.guir.prefuse.ItemRegistry;
import edu.berkeley.guir.prefuse.VisualItem;
import edu.berkeley.guir.prefuse.action.RepaintAction;
import edu.berkeley.guir.prefuse.activity.ActionList;
import edu.berkeley.guir.prefuse.render.DefaultRendererFactory;
import edu.berkeley.guir.prefuse.render.Renderer;
import edu.stanford.hci.flowmap.db.CSVQueryRecord;
import edu.stanford.hci.flowmap.db.QueryRecord;
import edu.stanford.hci.flowmap.db.QueryRow;
import edu.stanford.hci.flowmap.main.Options;
import edu.stanford.hci.flowmap.prefuse.action.FlowMapLayoutAction;
import edu.stanford.hci.flowmap.prefuse.item.FlowDummyNodeItem;
import edu.stanford.hci.flowmap.prefuse.item.FlowEdgeItem;
import edu.stanford.hci.flowmap.prefuse.item.FlowRealNodeItem;
import edu.stanford.hci.flowmap.prefuse.render.FlowClusterRenderer;
import edu.stanford.hci.flowmap.prefuse.render.FlowNodeRenderer;
import edu.stanford.hci.flowmap.prefuse.render.SimpleFlowEdgeRenderer;
import edu.stanford.hci.flowmap.prefuse.structure.FlowDummyNode;
import edu.stanford.hci.flowmap.prefuse.structure.FlowEdge;
import edu.stanford.hci.flowmap.prefuse.structure.FlowMapStructure;
import edu.stanford.hci.flowmap.prefuse.structure.FlowNode;
import edu.stanford.hci.flowmap.structure.Graph;
import edu.stanford.hci.flowmap.structure.Node;


/**
 * This software is distributed under the Berkeley Software Distribution License.
 * Please see http://graphics.stanford.edu/~dphan/code/bsd.license.html
 *
 */
public class SWTMapDisplayPanel implements PaintListener {

	// Have a rendered image object serving as an alternate buffer
	private BufferedImage bufferImage;

	private ItemRegistry m_registry;

	/**************************************************************************
	 * Class Definition
	 ***************************************************************************/
//	SWTDisplay m_prefuseDisplay = null;
	
	FlowNodeRenderer m_nodeRenderer;
	SimpleFlowEdgeRenderer m_edgeRenderer;

    private SwtMain parent;

    private Options options;
    private QueryRecord flowRecord;

	public SWTMapDisplayPanel(SwtMain swtMain, QueryRecord queryRecord) {
		parent = swtMain;
		flowRecord = queryRecord;
		
//		bufferImage = new BufferedImage(screenSize.width, screenSize.height,
//				BufferedImage.TYPE_INT_RGB);
		
        options = new Options();
        options.putDouble(Options.MIN_DISPLAY_WIDTH, 1);
        options.putDouble(Options.MAX_DISPLAY_WIDTH, 10);
        // make sure to have only one scale boolean set to true
        options.putBoolean(Options.LINEAR_SCALE, true);		
		
		//creating the registry
		m_registry = new ItemRegistry(new FlowMapStructure());
		m_registry.addItemClass(FlowNode.class.getName(), FlowRealNodeItem.class);
		m_registry.addItemClass(FlowDummyNode.class.getName(), FlowDummyNodeItem.class);
		m_registry.addItemClass(FlowEdge.class.getName(), FlowEdgeItem.class);
		
//		m_prefuseDisplay = new SWTDisplay(m_registry);
//		m_prefuseDisplay.setSize(this.getSize());
//		m_prefuseDisplay.setBackground(Color.WHITE);
		m_nodeRenderer = new FlowNodeRenderer();
		m_edgeRenderer = new SWTFlowEdgeRenderer(options, flowRecord);
		
		/***********************************************************************
		 * Initialize the display
		 ***********************************************************************/		
		FlowClusterRenderer clusterRenderer = new FlowClusterRenderer();
		
		m_registry.setRendererFactory(new DefaultRendererFactory(m_nodeRenderer,
		        m_edgeRenderer, clusterRenderer));
        ActionList initFlowMap = new ActionList(m_registry);
        
        Graph originalGraph = createNodes(queryRecord);
        
        FlowMapLayoutAction layoutAction = new FlowMapLayoutAction(
                options, originalGraph.getRootNode(), originalGraph.getAllNodes(),
                m_edgeRenderer) ;
        initFlowMap.add(layoutAction);
        
        initFlowMap.runNow();		
	}
	
    
    private Graph createNodes(QueryRecord flowRecord) {
        // says that we should read the width of the splines from the field
        // 'Value' stored in the scheme of each node.
        options.putString(Options.CURRENT_FLOW_TYPE, flowRecord.getSourceRow().getRowSchema().getDefaultValueId());
        
        Graph originalGraph = new Graph();
        assert(flowRecord.getSourceRow() != null);
        Node rootNode = new Node(flowRecord.getSourceRow());
        rootNode.setRootNode(true);
        
        originalGraph.addNode(rootNode);
        originalGraph.setRootNode(rootNode);
            
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
        Iterator items = m_registry.getItems();
        while (items.hasNext()) {
            VisualItem vi = (VisualItem) items.next();
            Renderer renderer = vi.getRenderer();
            // TODO: NO! NO! NO! NO!
            if (renderer instanceof SWTFlowEdgeRenderer) {
                ((SWTFlowEdgeRenderer)renderer).renderSWT(e.gc, vi);
                
            }
        }
        
//        m_prefuseDisplay.paintComponent(e, e.gc);
    }

}