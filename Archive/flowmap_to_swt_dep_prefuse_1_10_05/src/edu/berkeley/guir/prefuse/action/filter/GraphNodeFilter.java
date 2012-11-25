package edu.berkeley.guir.prefuse.action.filter;

import java.util.Iterator;

import edu.berkeley.guir.prefuse.ItemRegistry;
import edu.berkeley.guir.prefuse.NodeItem;
import edu.berkeley.guir.prefuse.graph.Node;

/**
 * Filters graph nodes, allowing all nodes in the graph to be visualized. By
 * default, garbage collection on node items is performed.
 * 
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public class GraphNodeFilter extends Filter {

    /**
     * Constructor.
     */
    public GraphNodeFilter() {
        super(ItemRegistry.DEFAULT_NODE_CLASS, true);
    } //
    
	public void run(ItemRegistry registry, double frac) {
		Iterator nodeIter = registry.getGraph().getNodes();
		while ( nodeIter.hasNext() ) {
			NodeItem item = registry.getNodeItem((Node)nodeIter.next(), true);
        }
        
		// optional garbage collection
        super.run(registry, frac);
	} //

} // end of class GraphNodeFilter
