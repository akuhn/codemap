package edu.berkeley.guir.prefuse.action.filter;

import java.util.Iterator;

import edu.berkeley.guir.prefuse.EdgeItem;
import edu.berkeley.guir.prefuse.ItemRegistry;
import edu.berkeley.guir.prefuse.NodeItem;
import edu.berkeley.guir.prefuse.graph.DefaultTree;
import edu.berkeley.guir.prefuse.graph.Edge;
import edu.berkeley.guir.prefuse.graph.Graph;
import edu.berkeley.guir.prefuse.graph.GraphLib;
import edu.berkeley.guir.prefuse.graph.Node;
import edu.berkeley.guir.prefuse.graph.Tree;
import edu.berkeley.guir.prefuse.graph.TreeNode;

/**
 * Filters a tree in it's entirety. If the backing graph is not a tree,
 * a tree structure will still be imposed on the filtered graph.
 * By default, garbage collection on node and edge items is performed.
 *
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public class TreeFilter extends Filter {

    public static final String[] ITEM_CLASSES = 
        {ItemRegistry.DEFAULT_NODE_CLASS, ItemRegistry.DEFAULT_EDGE_CLASS};
    
    // determines if filtered edges are visible by default
    private boolean m_edgesVisible;
    private boolean m_useFocusAsRoot;
    private Node m_root;
    
    // ========================================================================
    // == CONSTRUCTORS ========================================================
    
    public TreeFilter() {
        this(false, true, true);
    } //
    
    public TreeFilter(boolean useFocusAsRoot) {
        this(useFocusAsRoot, true, true);
    } //
    
    public TreeFilter(boolean useFocusAsRoot, boolean edgesVisible) {
        this(useFocusAsRoot, edgesVisible, true);
    } //
    
    public TreeFilter(boolean useFocusAsRoot, boolean edgesVisible, boolean garbageCollect) {
        super(ITEM_CLASSES, garbageCollect);
        m_edgesVisible = edgesVisible;
        m_useFocusAsRoot = useFocusAsRoot;
    } //
    
    // ========================================================================
    // == FILTER METHODS ======================================================
    
    public void setTreeRoot(Node r) {
        m_root = r;
    } //
    
    public void run(ItemRegistry registry, double frac) {
        Graph graph = registry.getGraph();
        boolean isTree = (graph instanceof Tree);
        
        // initialize filtered graph
        Graph fgraph = registry.getFilteredGraph();
        Tree  ftree = null;
        if ( isTree && fgraph instanceof DefaultTree ) {
            ftree = (DefaultTree)fgraph;
            ftree.setRoot(null);
        } else if ( isTree )
            fgraph = ftree = new DefaultTree();
        
        NodeItem root = null;
        
        // filter the nodes
        Iterator nodeIter = graph.getNodes();
        while ( nodeIter.hasNext() ) {
            NodeItem item = registry.getNodeItem((Node)nodeIter.next(), true);
            if ( root == null ) root = item;
        }
        
        Iterator fiter = registry.getDefaultFocusSet().iterator();
        NodeItem focus = null;
        if ( fiter.hasNext() ) {
            focus = registry.getNodeItem((Node)fiter.next(), true);
        }
        
        // update root node as necessary
        if ( m_root != null ) {
            Node r = (m_root instanceof NodeItem ? m_root : 
                        registry.getNodeItem(m_root));
            if ( r != root)
                ftree = null;
            root = (NodeItem)r;
        } else if ( focus != null && m_useFocusAsRoot ) {
            root = focus;
        } else if ( isTree ) {
            root = registry.getNodeItem(((Tree)graph).getRoot());
            if ( ftree != null ) ftree.setRoot(root);
        }
        
        // process each node's edges
        nodeIter = registry.getNodeItems();
        while ( nodeIter.hasNext() ) {
            NodeItem item = (NodeItem)nodeIter.next();
            Node     node = (Node)item.getEntity();
            Iterator edgeIter = node.getEdges();
            while ( edgeIter.hasNext() ) {
                Edge edge = (Edge)edgeIter.next();
                Node n = edge.getAdjacentNode(node);
                // filter the edge
                EdgeItem eitem = registry.getEdgeItem(edge, true);
                if ( edge.isTreeEdge() ) {
                    TreeNode c = (TreeNode)eitem.getAdjacentNode(item);
                    TreeNode p = ((TreeNode)n).getParent()==node?item:c;
                    p.addChild(eitem);
                } else {
                    eitem.getFirstNode().addEdge(eitem);
                    eitem.getSecondNode().addEdge(eitem);
                }
                if ( !m_edgesVisible ) eitem.setVisible(false);
            }
        }
        // if original graph was not a tree, we need to tree-ify things now
        if ( ftree == null )
            ftree = GraphLib.breadthFirstTree(root);
        
        // update the registry's filtered graph
        registry.setFilteredGraph(ftree);
        
        // optional garbage collection
        super.run(registry, frac);
    } //    
    
}
