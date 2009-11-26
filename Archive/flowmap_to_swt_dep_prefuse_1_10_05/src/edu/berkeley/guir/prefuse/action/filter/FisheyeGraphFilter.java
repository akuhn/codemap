package edu.berkeley.guir.prefuse.action.filter;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import edu.berkeley.guir.prefuse.EdgeItem;
import edu.berkeley.guir.prefuse.ItemRegistry;
import edu.berkeley.guir.prefuse.NodeItem;
import edu.berkeley.guir.prefuse.graph.DefaultGraph;
import edu.berkeley.guir.prefuse.graph.Edge;
import edu.berkeley.guir.prefuse.graph.Graph;
import edu.berkeley.guir.prefuse.graph.Node;

/**
 * <p>Filters nodes in a graph using the original Furnas fisheye calculation,
 * and sets DOI (degree-of-interest) values for each filtered node. This 
 * function filters current focus nodes, and includes neighbors only in a 
 * limited window around these foci. The size of this window is determined
 * by the minimum DOI value set for this action. By convention, DOI values
 * start at zero for focus nodes, and becoming decreasing negative numbers for
 * each hop away from a focus. This filter also performs garbage collection
 * of node and edge items by default.</p>
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
public class FisheyeGraphFilter extends Filter {

    public static final String[] ITEM_CLASSES = 
        {ItemRegistry.DEFAULT_NODE_CLASS, ItemRegistry.DEFAULT_EDGE_CLASS};
    
    public static final int DEFAULT_MIN_DOI = -2;
	public static final String ATTR_CENTER  = "center";

	private int m_minDOI;
    private boolean m_edgesVisible = true;
    private List m_queue = new LinkedList();
    
    // ========================================================================
    // == CONSTRUCTORS ========================================================
    
    public FisheyeGraphFilter() {
        this(DEFAULT_MIN_DOI);
    } //
    
    public FisheyeGraphFilter(int minDOI) {
        this(minDOI, true);
    } //
    
    public FisheyeGraphFilter(int minDOI, boolean edgesVisible) {
        this(minDOI, edgesVisible, true);
    } //
    
    public FisheyeGraphFilter(int minDOI, boolean edgesVisible, boolean gc) {
        super(ITEM_CLASSES, gc);
        m_minDOI = minDOI;
        m_edgesVisible = edgesVisible;
    } //
    
    // ========================================================================
    // == FILTER METHODS ======================================================
    
    /**
     * Returns an iterator over the Entities in the default focus set.
     * Override this method to control what Entities are passed to the
     * filter as foci of the fisheye.
     */
    protected Iterator getFoci(ItemRegistry registry) {
        Iterator iter = registry.getDefaultFocusSet().iterator();
        if ( !iter.hasNext() )
            iter = Collections.EMPTY_LIST.iterator();
        return iter;
    } //
    
    /**
     * @see edu.berkeley.guir.prefuse.action.Action#run(edu.berkeley.guir.prefuse.ItemRegistry, double)
     */
	public void run(ItemRegistry registry, double frac) {
		Graph graph = registry.getGraph();
		// initialize filtered graph
        Graph fgraph = registry.getFilteredGraph();
        if ( fgraph instanceof DefaultGraph )
            ((DefaultGraph)fgraph).reinit(graph.isDirected());
        else
            fgraph = new DefaultGraph(graph.isDirected());

        // filter the nodes using a depth-limited breadth first search
        // around the focus nodes
		Iterator focusIter = getFoci(registry);
		while ( focusIter.hasNext() ) {
            Object focus = focusIter.next();
            if ( !(focus instanceof Node) ) continue;
            
            Node fnode = (Node)focus;
            NodeItem fitem = registry.getNodeItem(fnode);

            boolean recurse = false;
            recurse = ( fitem==null || fitem.getDirty()>0 || fitem.getDOI()<0 );
            
            if (fitem==null || fitem.getDirty() > 0)
                fitem = registry.getNodeItem(fnode, true);
            
            fgraph.addNode(fitem);
            
            if ( !recurse )
                continue;
            
            fitem.setDOI(0);
            m_queue.add(fitem);
            
            while ( !m_queue.isEmpty() ) {
                NodeItem ni = (NodeItem)m_queue.remove(0);
                Node n = (Node)ni.getEntity();
                
                double doi = ni.getDOI()-1;
                if ( doi >= m_minDOI ) {                    
                    Iterator niter = n.getNeighbors();
                    int i = 0;
                    while ( niter.hasNext() ) {
                        Node nn = (Node)niter.next();
                        NodeItem nni = registry.getNodeItem(nn);
                        
                        recurse = ( nni==null ||  nni.getDirty()>0 || nni.getDOI()<doi );
                        if ( nni == null || nni.getDirty() > 0)
                            nni = registry.getNodeItem(nn, true);
                        fgraph.addNode(nni);
                        
                        if ( recurse ) {
                            nni.setDOI(doi);
                            m_queue.add(nni);
                        }
                    }
                }
            } // elihw
		}
        
        // now filter the graph edges
        Iterator nodeIter = registry.getNodeItems();
        while ( nodeIter.hasNext() ) {
            NodeItem nitem  = (NodeItem)nodeIter.next();
            if ( nitem.getDirty() > 0 ) continue;
            Node node = (Node)nitem.getEntity();
            Iterator edgeIter = node.getEdges();
            while ( edgeIter.hasNext() ) {
                Edge edge = (Edge)edgeIter.next();
                Node n = edge.getAdjacentNode(node);
                NodeItem item = registry.getNodeItem(n);
                if ( item != null && item.getDirty()==0 ) {
                    EdgeItem eitem = registry.getEdgeItem(edge, true);
                    fgraph.addEdge(eitem);
                    if ( !m_edgesVisible ) eitem.setVisible(false);
                }
            }
        }
        
        // update the registry's filtered graph
        registry.setFilteredGraph(fgraph);
        
        // optional garbage collection
        super.run(registry, frac);
	} //

} // end of class FisheyeTreeFilter
