package edu.berkeley.guir.prefusex.layout;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Iterator;

import edu.berkeley.guir.prefuse.ItemRegistry;
import edu.berkeley.guir.prefuse.NodeItem;
import edu.berkeley.guir.prefuse.action.assignment.TreeLayout;

/**
 * Performs a vertical, top-down, layout of a tree.
 * 
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> - prefuse(AT)jheer.org
 */
public class VerticalTreeLayout extends TreeLayout {
    
	protected HashMap m_counts;
	protected int m_heightInc = 25;
    protected ItemRegistry m_registry;

	/**
	 * Constructor.
	 */
	public VerticalTreeLayout() {
		try {
			m_counts = new HashMap();
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	} //

    public Point2D getLayoutAnchor(ItemRegistry registry) {
        Point2D anchor = super.getLayoutAnchor();
        if ( anchor != null )
            return anchor;
        
        Rectangle2D b = getLayoutBounds(registry);
        double x = 0, y = 0;
        if ( b != null ) {
            x = b.getX()+b.getWidth()/2;
            y = b.getY()+20;
        }
        return new Point2D.Double(x,y);
    } //
    
	/**
	 * @see edu.berkeley.guir.prefuse.action.Action#run(edu.berkeley.guir.prefuse.ItemRegistry, double)
	 */
	public void run(ItemRegistry registry, double frac) {
        m_registry = registry;
        Rectangle2D  b = getLayoutBounds(registry);
		Point2D anchor = getLayoutAnchor(registry);
        NodeItem n = getLayoutRoot(registry);
		if ( n != null && n.isVisible() ) {
			countVisibleDescendants(n);
			setLocation(n, null, anchor.getX(), anchor.getY());
			layout(n, (int)anchor.getY()+m_heightInc, b.getX(), b.getX()+b.getWidth());
			m_counts.clear();			
		} else {
			System.err.println("VerticalTreeLayout: Tree root not visible!");
		}
	} //

	/**
	 * Computes the number of visible descendant leaf nodes for each visible
	 * node.
	 */
	private int countVisibleDescendants(NodeItem n) {
		int count = 0;
		Iterator childIter = n.getChildren();
		while ( childIter.hasNext() ) {
			NodeItem c = (NodeItem)childIter.next();
		    count += countVisibleDescendants(c);
		}
		if ( count == 0 ) {
			count = 1;
		}
		setVisibleDescendants(n, count);
		return count;
	} //

	/**
	 * Store the visible descendant count for a node.
	 * @param n
	 * @param count
	 */
	private void setVisibleDescendants(NodeItem n, int count) {
		m_counts.put(n,new Integer(count));
	} //
	
	/**
	 * Retrieve the visible descendant count for a node.b
	 * @param n
	 * @return int
	 */
	private int getVisibleDescendants(NodeItem n) {
		Integer count = (Integer)m_counts.get(n);
		return ( count == null ? 0 : count.intValue() );
	} //
	
	/**
	 * Recursivel compute the layout.
	 * @param n the parent NodeItem
	 * @param h the current height (depth in the display)
	 * @param x1 the minimum breadth co-ordinate
     * @param x2 the maximum breadth co-ordinate
	 */
	protected void layout(NodeItem n, int h, double x1, double x2) {
		int numDescendants = getVisibleDescendants(n), i = 0;

		if ( numDescendants == 0 )
			return;

		double dx  = (x2-x1);
		double dx2 = dx / 2.0;

		double f = 0.0;

		Iterator childIter = n.getChildren();
		while ( childIter.hasNext() ) {
			NodeItem c = (NodeItem)childIter.next();
			double frac = ((double)getVisibleDescendants(c))/numDescendants;
			setLocation(c, n, x1 + f*dx + frac*dx2, h);
			layout(c, h+m_heightInc, x1 + f*dx, x1 + (f+frac)*dx);
			f += frac;
		}
	} //

} // end of class RadialTreeLayout
