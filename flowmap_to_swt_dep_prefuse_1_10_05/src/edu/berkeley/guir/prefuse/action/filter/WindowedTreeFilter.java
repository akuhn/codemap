package edu.berkeley.guir.prefuse.action.filter;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import edu.berkeley.guir.prefuse.EdgeItem;
import edu.berkeley.guir.prefuse.ItemRegistry;
import edu.berkeley.guir.prefuse.NodeItem;
import edu.berkeley.guir.prefuse.graph.DefaultTree;
import edu.berkeley.guir.prefuse.graph.Edge;
import edu.berkeley.guir.prefuse.graph.Graph;
import edu.berkeley.guir.prefuse.graph.Node;
import edu.berkeley.guir.prefuse.graph.Tree;

/**
 * 
 * Mar 24, 2004 - jheer - Created class
 *
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public class WindowedTreeFilter extends Filter {

    public static final String[] ITEM_CLASSES = 
        {ItemRegistry.DEFAULT_NODE_CLASS, ItemRegistry.DEFAULT_EDGE_CLASS};
    
    public static final int DEFAULT_MIN_DOI = -2;
    
    // determines if filtered edges are visible by default
    private boolean m_edgesVisible;
    private boolean m_useFocusAsRoot;
    private double m_minDOI;
    private Node m_root;
    private List m_queue = new LinkedList();
    
    // ========================================================================
    // == CONSTRUCTORS ========================================================
    
    public WindowedTreeFilter() {
        this(DEFAULT_MIN_DOI);
    } //
    
    public WindowedTreeFilter(int minDOI) {
        this(minDOI, false);
    } //
    
    public WindowedTreeFilter(int minDOI, boolean useFocusAsRoot) {
        this(minDOI, useFocusAsRoot, true);
    } //
    
    public WindowedTreeFilter(int minDOI, boolean useFocusAsRoot, boolean edgesVisible) {
        this(minDOI, useFocusAsRoot, edgesVisible, true);
    } //
    
    public WindowedTreeFilter(int minDOI, boolean useFocusAsRoot, boolean edgesVisible, boolean gc) {
        super(ITEM_CLASSES, gc);
        m_minDOI = minDOI;
        m_useFocusAsRoot = useFocusAsRoot;
        m_edgesVisible = edgesVisible;
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
        } else {
            fgraph = ftree = new DefaultTree();
        }
        
        NodeItem froot = null;
        
        // get the current focus node, if there is one
        Iterator fiter = registry.getDefaultFocusSet().iterator();
        NodeItem focus = null;
        if ( fiter.hasNext() ) {
            focus = registry.getNodeItem((Node)fiter.next(), true);
        }
        
        // determine root node for filtered tree
        if ( m_root != null ) {
            // someone has set a root for us to use
            Node r = (m_root instanceof NodeItem ? m_root : 
                registry.getNodeItem(m_root,true));
            froot = (NodeItem)r;
        } else if ( focus != null && m_useFocusAsRoot ) {
            // use the current focus as the root
            froot = focus;
        } else if ( isTree ) {
            // the backing graph is a tree, so use its root
            froot = registry.getNodeItem(((Tree)graph).getRoot(),true);
        } else {
            // no root is specified so let's just use the first thing we find
            Iterator iter = graph.getNodes();
            if ( iter.hasNext() )
                froot = registry.getNodeItem((Node)iter.next(),true);
        }
        
        if (froot == null) {
            // couldn't find any nodes, so bail!
            throw new IllegalStateException("No root for the filtered tree "
                + "has been specified.");
        }
        ftree.setRoot(froot);
        
        // do a breadth first crawl from the root
        froot.setDOI(0);
        m_queue.add(froot);
        while ( !m_queue.isEmpty() ) {
            NodeItem ni = (NodeItem)m_queue.remove(0);
            Node n = (Node)ni.getEntity();
            
            double doi = ni.getDOI()-1;
            if ( doi >= m_minDOI ) {                    
                Iterator iter = n.getEdges();
                int i = 0;
                while ( iter.hasNext() ) {
                    Edge ne = (Edge)iter.next();
                    Node nn = (Node)ne.getAdjacentNode(n);
                    NodeItem nni = registry.getNodeItem(nn);
                    
                    boolean recurse = (nni==null || nni.getDirty()>0);
                    if ( recurse )
                        nni = registry.getNodeItem(nn, true);
                    
                    EdgeItem nne = registry.getEdgeItem(ne,true);
                    
                    if ( recurse ) {
                        ni.addChild(nne);
                        nni.setDOI(doi);
                        m_queue.add(nni);
                    } else {
                        nne.getFirstNode().addEdge(nne);
                        nne.getSecondNode().addEdge(nne);
                    }
                }
            }
        } // elihw
        
        // update the registry's filtered graph
        registry.setFilteredGraph(ftree);
        
        // optional garbage collection
        super.run(registry, frac);
    } //
    
} // end of class WindowedTreeFilter
