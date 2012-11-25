package edu.berkeley.guir.prefuse.action.filter;

import java.util.Iterator;

import edu.berkeley.guir.prefuse.EdgeItem;
import edu.berkeley.guir.prefuse.ItemRegistry;
import edu.berkeley.guir.prefuse.NodeItem;
import edu.berkeley.guir.prefuse.graph.Edge;
import edu.berkeley.guir.prefuse.graph.TreeNode;

/**
 * The TreeEdgeFilter determines which edges to visualize based on the nodes
 *  selected for visualization and the underlying tree structure. By default,
 *  garbage collection on edge items is performed.
 * 
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public class TreeEdgeFilter extends Filter {
    
    private boolean m_edgesVisible;
    
    /**
     * Filters tree edges, connecting filtered graph nodes into a
     * tree structure. Filtered edges are visible by default.
     */
    public TreeEdgeFilter() {
        this(true);
    } //
    
    /**
     * Filters tree edges, connecting filtered graph nodes into a
     * tree structure. DefaultEdge visibility can be controlled.
     * @param edgesVisible determines whether or not the filtered
     *  edges are visible in the display.
     */
    public TreeEdgeFilter(boolean edgesVisible) {
        super(ItemRegistry.DEFAULT_EDGE_CLASS, true);
        m_edgesVisible = edgesVisible;
    } //
    
	public void run(ItemRegistry registry, double frac) {
		Iterator nodeIter = registry.getNodeItems();
		while ( nodeIter.hasNext() ) {
			NodeItem nitem  = (NodeItem)nodeIter.next();
			TreeNode node   = (TreeNode)registry.getEntity(nitem);
            
            if ( node.getChildCount() > 0 ) {
                Iterator iter = node.getChildEdges();
                while ( iter.hasNext() ) {
                    Edge e = (Edge)iter.next();
                    TreeNode c = (TreeNode)e.getAdjacentNode(node);
                    if ( registry.isVisible(c) ) {
                        EdgeItem eitem = registry.getEdgeItem(e,true);
                        nitem.addChild(eitem);
                        if ( !m_edgesVisible ) eitem.setVisible(false);
                    }
                }
            }
		}
        
        // optionally perform garbage collection
        super.run(registry, frac);
	} //

} // end of class TreeEdgeFilter
