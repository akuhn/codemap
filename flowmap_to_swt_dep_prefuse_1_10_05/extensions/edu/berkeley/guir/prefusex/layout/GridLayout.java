package edu.berkeley.guir.prefusex.layout;

import java.awt.geom.Rectangle2D;
import java.util.Iterator;

import edu.berkeley.guir.prefuse.EdgeItem;
import edu.berkeley.guir.prefuse.ItemRegistry;
import edu.berkeley.guir.prefuse.NodeItem;
import edu.berkeley.guir.prefuse.action.assignment.Layout;
import edu.berkeley.guir.prefuse.graph.Graph;
import edu.berkeley.guir.prefuse.graph.Node;

/**
 * Implements a uniform grid-based layout. This component can either use
 * preset grid dimensions or analyze a grid-shaped graph to determine them
 * automatically.
 * 
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org</a>
 */
public class GridLayout extends Layout {

    protected int rows;
    protected int cols;
    protected boolean sorted = false;
    protected boolean analyze = false;
    
    /**
     * Create a new GridLayout without preset dimensions. The layout will
     * attempt to analyze an input graph to determine grid parameters.
     */
    public GridLayout() {
        analyze = true;
    } //
    
    /**
     * Create a new GridLayout using the specified grid dimensions. If the
     * input data has more elements than the grid dimensions can hold, the
     * left over elements will not be visible.
     * @param nrows the number of rows of the grid
     * @param ncols the number of columns of the grid
     */
    public GridLayout(int nrows, int ncols) {
        this(nrows, ncols, false);
    } //
    
    /**
     * Create a new GridLayout using the specified grid dimensions. If the
     * input data has more elements than the grid dimensions can hold, the
     * left over elements will not be visible. Additionally, a sorted flag
     * determines if the grid elements should be ordered based on their
     * order in the original graph data (sorted=false) or ordered based on
     * their rendering sort order in the ItemRegistry (sorted=true).
     * @param nrows the number of rows of the grid
     * @param ncols the number of columns of the grid
     * @param sorted if true, uses the sort order of items in the
     * ItemRegistry as the grid layout order, otherwise uses the order of
     * elements in the original abstract data.
     */
    public GridLayout(int nrows, int ncols, boolean sorted) {
        rows = nrows;
        cols = ncols;
        sorted = true;
        analyze = false;
    } //
    
    /**
     * @see edu.berkeley.guir.prefuse.action.Action#run(edu.berkeley.guir.prefuse.ItemRegistry, double)
     */
    public void run(ItemRegistry registry, double frac) {
        Rectangle2D b = getLayoutBounds(registry);
        double bx = b.getMinX(), by = b.getMinY();
        double w = b.getWidth(), h = b.getHeight();
        
        Graph g = (Graph)registry.getFilteredGraph();
        int m = rows, n = cols;
        if ( analyze ) {
            int[] d = analyzeGraphGrid(g);
            m = d[0]; n = d[1];
        }
        
        Iterator iter = (sorted?registry.getNodeItems():g.getNodes());
        // layout grid contents
        for ( int i=0; iter.hasNext() && i < m*n; i++ ) {
            NodeItem ni = (NodeItem)iter.next();
            ni.setVisible(true);
            setEdgeVisibility(ni, true);
            double x = bx + w*((i%n)/(double)(n-1));
            double y = by + h*((i/n)/(double)(m-1));
            setLocation(ni,null,x,y);
        }
        // set left-overs invisible
        while ( iter.hasNext() ) {
            NodeItem ni = (NodeItem)iter.next();
            ni.setVisible(false);
            setEdgeVisibility(ni, false);
        }
    } //
    
    private void setEdgeVisibility(NodeItem n, boolean val) {
        Iterator eiter = n.getEdges();
        while ( eiter.hasNext() ) {
            EdgeItem e = (EdgeItem)eiter.next();
            e.setVisible(val);
        }
    } //
    
    /**
     * Analyzes a graph to try and determine it's grid dimensions. Currently
     * looks for the edge count on anode to drop to 2 to determine the end of
     * a row.
     * @param g the input graph
     * @return
     */
    public static int[] analyzeGraphGrid(Graph g) {
        // TODO: more robust grid analysis?
        int m, n;
        Iterator iter = g.getNodes(); iter.next();
        for ( n=2; iter.hasNext(); n++ ) {
            Node nd = (Node)iter.next();
            if ( nd.getEdgeCount() == 2 )
                break;
        }
        m = g.getNodeCount() / n;
        return new int[] {m,n};
    } //
    
    /**
     * @return Returns the number of columns.
     */
    public int getNumCols() {
        return cols;
    } //
    
    /**
     * @param cols Sets the number of columns.
     */
    public void setNumCols(int cols) {
        this.cols = cols;
    } //
    
    /**
     * @return Returns the numerb of rows.
     */
    public int getNumRows() {
        return rows;
    } //
    
    /**
     * @param rows Sets the number of rows.
     */
    public void setNumRows(int rows) {
        this.rows = rows;
    } //
    
    /**
     * @return Indicates if the item registry's sort order is used (true) or
     * the ordering in the original source data (false).
     */
    public boolean isSorted() {
        return sorted;
    } //
    
    /**
     * @param sorted Sets if the item registry's sort order is used (true) or
     * the ordering in the original source data (false).
     */
    public void setSorted(boolean sorted) {
        this.sorted = sorted;
    } //
    
} // end of class GridLayout
