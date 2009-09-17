package edu.stanford.hci.flowmap.prefuse.action;

import java.util.Collection;

import edu.berkeley.guir.prefuse.ItemRegistry;
import edu.berkeley.guir.prefuse.action.AbstractAction;
import edu.stanford.hci.flowmap.cluster.ClusterLayout;
import edu.stanford.hci.flowmap.main.Globals;
import edu.stanford.hci.flowmap.main.Options;
import edu.stanford.hci.flowmap.prefuse.item.FlowNodeItem;
import edu.stanford.hci.flowmap.prefuse.render.FlowEdgeRenderer;
import edu.stanford.hci.flowmap.structure.Node;

/**
 * This software is distributed under the Berkeley Software Distribution License.
 * Please see http://graphics.stanford.edu/~dphan/code/bsd.license.html
 *
 */
public class FlowMapLayoutAction extends AbstractAction {


	protected ClusterLayout clusterLayout = null;
	
	protected Options userOptions;
	
	protected Node rootNode;
	protected Collection<Node> allNodes;
	
	protected FlowEdgeRenderer edgeRenderer = null;
	
	/**
	 * Creates a new GraphFilter.
	 */
	public FlowMapLayoutAction(Options userOptions, Node rootNode, Collection<Node> allNodes,
			FlowEdgeRenderer renderer) {
		this.userOptions = userOptions;
		this.rootNode = rootNode;
		this.allNodes = allNodes;
	
		this.edgeRenderer = renderer;

	} 

	/**
	 * @return the clusters computed by MultiRoot_ClusterLayout
	 */
	public Collection getClusterCollection() {
		if (clusterLayout != null)
			return clusterLayout.getClusterCollection();
		else
			return null;
	}
	
	public void run(ItemRegistry registry, double frac) {
		if (Globals.useLayoutAdjustment) {
			//System.out.println("Adjusting Node Positions");
			NodeForceScanLayout scanLayout = new NodeForceScanLayout(allNodes);
			scanLayout.shiftNodes();
		}

		// first run clustering
		clusterLayout = new ClusterLayout(rootNode, allNodes);
		Node newRoot = clusterLayout.doLayout();
		
		
		if (Globals.runNodeEdgeRouting) {
			//System.out.println("Adjusting Edge Routing");
			NodeEdgeRouting router = new NodeEdgeRouting(newRoot);
			router.routeNodes();
		}
		
		// then convert to a flow tree
		ConvertToPrefuse converter = new ConvertToPrefuse(registry, newRoot);
		FlowNodeItem rootNodeItem = converter.convert();
		
		edgeRenderer.setRootNodeItem(rootNodeItem);
		// initialize edges
		edgeRenderer.initializeRenderTree(rootNodeItem);
	}
	
		

}