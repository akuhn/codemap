package edu.berkeley.guir.prefuse.action.assignment;

import edu.berkeley.guir.prefuse.ItemRegistry;
import edu.berkeley.guir.prefuse.NodeItem;
import edu.berkeley.guir.prefuse.graph.Graph;
import edu.berkeley.guir.prefuse.graph.Tree;

/**
 * Abstract class providing convenience methods for tree layout algorithms.
 *
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public abstract class TreeLayout extends Layout {

    protected NodeItem m_root;
    
    public NodeItem getLayoutRoot() {
        return m_root;
    } //
    
    public void setLayoutRoot(NodeItem root) {
        m_root = root;
    } //
    
    public NodeItem getLayoutRoot(ItemRegistry registry) {
        if ( m_root != null )
            return m_root;
        Graph g = registry.getFilteredGraph();
        if ( g instanceof Tree )
            return (NodeItem)((Tree)g).getRoot();
        else
            throw new IllegalStateException("The filtered graph returned by"
             +  " ItemRegistry.getFilteredGraph() must be a Tree instance for"
             +  " a TreeLayout to work. Try using a different filter (e.g."
             +  " edu.berkeley.guir.prefuse.action.filter.TreeFilter).");
    } //

} // end of abstract class TreeLayout
