package edu.berkeley.guir.prefuse.action.filter;

import java.util.Iterator;

import edu.berkeley.guir.prefuse.EdgeItem;
import edu.berkeley.guir.prefuse.ItemRegistry;
import edu.berkeley.guir.prefuse.NodeItem;
import edu.berkeley.guir.prefuse.collections.SingleElementIterator;
import edu.berkeley.guir.prefuse.graph.DefaultTree;
import edu.berkeley.guir.prefuse.graph.Edge;
import edu.berkeley.guir.prefuse.graph.Graph;
import edu.berkeley.guir.prefuse.graph.Node;
import edu.berkeley.guir.prefuse.graph.Tree;
import edu.berkeley.guir.prefuse.graph.TreeNode;

/**
 * <p>Filters nodes in a tree using the original Furnas fisheye calculation,
 * and sets DOI (degree-of-interest) values for each filtered node. This 
 * function filters current focus nodes, and includes neighbors only in a 
 * limited window around these foci. The size of this window is determined
 * by the minimum DOI value set for this action. All ancestors of a focus up
 * to the root of the tree are considered foci as well. By convention, DOI 
 * values start at zero for focus nodes, and becoming decreasing negative 
 * numbers for each hop away from a focus. This filter also performs garbage
 * collection of node items by default.</p>
 * 
 * <p>For more information about Furnas' fisheye view calculation and DOI values,
 * take a look at G.W. Furnas, "The FISHEYE View: A New Look at Structured 
 * Files," Bell Laboratories Tech. Report, Murray Hill, New Jersey, 1981. 
 * Available online at <a href="http://citeseer.nj.nec.com/furnas81fisheye.html">
 * http://citeseer.nj.nec.com/furnas81fisheye.html</a>.</p>
 * 
 * <p>For a more recent example of fisheye views and DOI functions in information
 * visualization check out S.K. Card and D. Nation. "Degree-of-Interest 
 * Trees: A Component of an Attention-Reactive User Interface," Advanced 
 * Visual Interfaces, Trento, Italy, 2002. Available online at
 * <a href="http://www2.parc.com/istl/projects/uir/pubs/items/UIR-2002-11-Card-AVI-DOITree.pdf">
 * http://www2.parc.com/istl/projects/uir/pubs/items/UIR-2002-11-Card-AVI-DOITree.pdf</a>
 * </p>
 * 
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> - prefuse(AT)jheer.org
 */
public class FisheyeTreeFilter extends Filter {

    public static final String[] ITEM_CLASSES = 
        {ItemRegistry.DEFAULT_NODE_CLASS, ItemRegistry.DEFAULT_EDGE_CLASS};
    
    public static final int DEFAULT_MIN_DOI = -2;
	public static final String ATTR_CENTER  = "center";

	private int m_minDOI;
    private boolean m_edgesVisible = true;
    
    private Node m_froot;
    
    // temporary member variables
    private ItemRegistry m_registry;
    private double m_localDOIDivisor;
	private TreeNode m_root;
    
    
    // ========================================================================
    // == CONSTRUCTORS ========================================================

    public FisheyeTreeFilter() {
        this(DEFAULT_MIN_DOI);
    } //
    
    public FisheyeTreeFilter(int minDOI) {
        this(minDOI, true);
    } //
    
    public FisheyeTreeFilter(int minDOI, boolean edgesVisible) {
        this(minDOI, edgesVisible, true);
    } //
    
    public FisheyeTreeFilter(int minDOI, boolean edgesVisible, boolean gc) {
        super(ITEM_CLASSES, gc);
        m_edgesVisible = edgesVisible;
        m_minDOI = minDOI;
    } //
    
    // ========================================================================
    // == FILTER METHODS ======================================================
    
    public void setTreeRoot(Node r) {
        m_froot = r;
    } //
    
    /**
     * Returns an iterator over the Entities in the default focus set.
     * Override this method to control what Entities are passed to the
     * filter as foci of the fisheye.
     */
    protected Iterator getFoci(ItemRegistry registry) {
        Iterator iter = registry.getDefaultFocusSet().iterator();
        if ( !iter.hasNext() )
            iter = new SingleElementIterator(m_root);
        return iter;
    } //
    
    /**
     * @see edu.berkeley.guir.prefuse.action.Action#run(edu.berkeley.guir.prefuse.ItemRegistry, double)
     */
	public void run(ItemRegistry registry, double frac) {
	    // initialize temp member variables, get the backing graph
        m_registry = registry;
        Graph graph = registry.getGraph();
        if ( !(graph instanceof Tree) ) {
            throw new IllegalStateException("The FisheyeTreeFilter requires "
                + "that the backing graph returned by registry.getGraph() is "
                + "a Tree instance.");
        }
		Tree tree = (Tree)graph;
        m_localDOIDivisor = tree.getNodeCount();
		m_root = tree.getRoot();
        
        // set up the filtered graph
        Graph fgraph = registry.getFilteredGraph();
        Tree  ftree = null;
        if ( fgraph instanceof DefaultTree ) {
            ftree = (DefaultTree)fgraph;
            ftree.setRoot(null);
        } else {
            fgraph = ftree = new DefaultTree();
        }

        // compute the fisheye over nodes
		Iterator focusIter = getFoci(registry);
		while ( focusIter.hasNext() ) {
            Object focus = focusIter.next();
            if ( !(focus instanceof TreeNode) ) continue;
            TreeNode fnode = (TreeNode)focus;
            NodeItem fitem = registry.getNodeItem(fnode);

			boolean recurse = false;
			recurse = ( fitem==null ||  fitem.getDirty()>0 || fitem.getDOI()<0 );
				
			fitem = registry.getNodeItem(fnode, true);
			if ( recurse ) {
				setDOI(fitem, 0, 0);
				if ( (int)fitem.getDOI() > m_minDOI ) {					
					visitDescendants(fnode, fitem, null);
				}				
				visitAncestors(fnode, fitem);
			}
		}
        
        // filter edges and build the filtered tree
        ftree.setRoot(registry.getNodeItem(m_root));
        Iterator nodeIter = registry.getNodeItems();
        while ( nodeIter.hasNext() ) {
            NodeItem item = (NodeItem)nodeIter.next();
            Node     node = (Node)item.getEntity();
            Iterator edgeIter = node.getEdges();
            while ( edgeIter.hasNext() ) {
                Edge edge = (Edge)edgeIter.next();
                Node n = edge.getAdjacentNode(node);
                
                // check if this is a filtered node
                if ( !registry.isVisible(n) ) continue;
                
                // filter the edge
                EdgeItem eitem = registry.getEdgeItem(edge, true);
                if ( edge.isTreeEdge() ) {
                    TreeNode c = (TreeNode)eitem.getAdjacentNode(item);
                    TreeNode p = (TreeNode)(c.getParent()==item?item:c);
                    p.addChild(eitem);
                } else {
                    eitem.getFirstNode().addEdge(eitem);
                    eitem.getSecondNode().addEdge(eitem);
                }
                if ( !m_edgesVisible ) eitem.setVisible(false);
            }
        }
        
        // clear temp member vars
        m_registry = null;
        m_root = null;
        
        // update the registry's filtered graph
        registry.setFilteredGraph(ftree);
        
        // optional garbage collection
        super.run(registry, frac);
	} //
	
	protected void visitDescendants(TreeNode node, NodeItem item, TreeNode skip) {
		int lidx = ( skip == null ? getCenter(item) : node.getChildIndex(skip) );		
		Iterator childIter = node.getChildren();
		int i = 0;
		while ( childIter.hasNext() ) {
			TreeNode cnode = (TreeNode)childIter.next();
			if ( cnode == skip ) { continue; }
			NodeItem citem = m_registry.getNodeItem(cnode, true);				
			
			setDOI(citem, (int)item.getDOI()-1, Math.abs(lidx-i));		
			if ( (int)citem.getDOI() > m_minDOI ) {
				visitDescendants(cnode, citem, null);	
			}
			i++;
		}
	} //
	
	protected void visitAncestors(TreeNode node, NodeItem item) {
		if ( node.getParent() == null || node == m_root ) { return; }
		TreeNode pnode = node.getParent();
		NodeItem pitem = m_registry.getNodeItem(pnode);
		
		boolean recurse = false;
		recurse = ( pitem==null || pitem.getDirty()>0 || pitem.getDOI()<0 );
		
		pitem = m_registry.getNodeItem(pnode, true);
		if ( recurse ) {
			setDOI(pitem, 0, 0);
			if ( (int)pitem.getDOI() > m_minDOI ) {
				visitDescendants(pnode, pitem, node);
			}
			visitAncestors(pnode, pitem);
		}
	} //
	
	protected void setDOI(NodeItem item, int doi, int ldist) {
		double localDOI = -ldist / (double)Math.min(1000.0,m_localDOIDivisor);
		item.setDOI(doi+localDOI);
	} //

	private int getCenter(NodeItem item) {
		TreeNode node = (TreeNode)item.getVizAttribute(ATTR_CENTER);
		if ( node != null ) {
			TreeNode parent = (TreeNode)item.getEntity();
			int idx = parent.getChildIndex(node);
			if ( idx > -1 )
				return idx;
		}
		return 0;
	} //

} // end of class FisheyeTreeFilter
