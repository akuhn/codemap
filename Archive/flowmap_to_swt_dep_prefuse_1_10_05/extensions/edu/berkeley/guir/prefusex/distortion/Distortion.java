package edu.berkeley.guir.prefusex.distortion;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;

import edu.berkeley.guir.prefuse.ItemRegistry;
import edu.berkeley.guir.prefuse.VisualItem;
import edu.berkeley.guir.prefuse.action.assignment.Layout;

/**
 * Abstract class providing skeletal implementation for space-distortion
 * techniques.
 *
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public abstract class Distortion extends Layout {

    private Point2D m_tmp = new Point2D.Double();
    private boolean m_sizeDistorted = true;
    private final boolean useFilteredGraph;
    
    /**
     * 
     */
    public Distortion(final boolean useFilteredGraph) {
        this.useFilteredGraph = useFilteredGraph;
    }
    /**
     * Controls whether item sizes are distorted 
     * along with the item locations.
     * @param s true to distort size, false to distort positions only
     */
    public void setSizeDistorted(boolean s) {
        m_sizeDistorted = s;
    } //
    
    /**
     * Indicates whether the item sizes are distorted 
     * along with the item locations.
     * @return true if item sizes are distorted by this action, false otherwise
     */
    public boolean isSizeDistorted() {
        return m_sizeDistorted;
    } //
    
    /**
     * @see edu.berkeley.guir.prefuse.action.Action#run(edu.berkeley.guir.prefuse.ItemRegistry, double)
     */
    public void run(ItemRegistry registry, double frac) {
        Rectangle2D bounds = getLayoutBounds(registry);
        Point2D anchor = correct(getLayoutAnchor(), bounds);
        final Iterator iter;
        if (useFilteredGraph) {
            //registry.printAllEntries();
            iter = registry.getFilteredGraph().getNodes();
        } else {
            iter = registry.getNodeItems();
        }
        int i = 0;
        while ( iter.hasNext() ) {
            //System.out.println(i++);
            VisualItem item = (VisualItem)iter.next();
            if ( item.isFixed() ) continue;
            
            // reset distorted values
            item.getLocation().setLocation(item.getEndLocation());
            item.setSize(item.getEndSize());
            
            // compute distortion if we have a distortion focus
            if ( anchor != null ) {
                Rectangle2D bbox = item.getBounds();
                Point2D loc = item.getLocation();
                transformPoint(item.getEndLocation(), 
                        loc, anchor, bounds);
                if ( m_sizeDistorted ) {
                    double sz = transformSize(bbox, loc, anchor, bounds);
                    item.setSize(sz*item.getEndSize());
                }
            }
        }
    } //
    
    /**
     * Corrects the anchor position, such that if the anchor is outside the
     * layout bounds, the anchor is adjusted to be the nearest point on the
     * edge of the bounds.
     * @param anchor the un-corrected anchor point
     * @param bounds the layout bounds
     * @return the corrected anchor point
     */
    protected Point2D correct(Point2D anchor, Rectangle2D bounds) {
        if ( anchor == null ) return anchor;
        double x = anchor.getX(), y = anchor.getY();
        double x1 = bounds.getMinX(), y1 = bounds.getMinY();
        double x2 = bounds.getMaxX(), y2 = bounds.getMaxY();
        x = (x < x1 ? x1 : (x > x2 ? x2 : x));
        y = (y < y1 ? y1 : (y > y2 ? y2 : y));
        
        m_tmp.setLocation(x,y);
        return m_tmp;
    } //
    
    /**
     * Transforms the undistorted point <code>o</code> to the distorted point
     * <code>p</code>, subject to the given layout anchor (or focus) and
     * bounds.
     * @param o the original, undistorted point
     * @param p Point2D in which to store coordinates of the transformed point
     * @param anchor the anchor or focus point of the display
     * @param bounds the layout bounds
     */
    protected abstract void transformPoint(Point2D o, Point2D p, 
            Point2D anchor, Rectangle2D bounds);
    
    /**
     * Returns the scaling factor by which to transform the size of an 
     * undistorted item to a distorted item.
     * @param bbox the bounding box of the undistorted item
     * @param pf the location of the distorted item
     * @param anchor the anchor or focus point of the display
     * @param bounds the layout bounds
     * @return the scaling factor by which to change the size
     */
    protected abstract double transformSize(Rectangle2D bbox, Point2D pf, 
            Point2D anchor, Rectangle2D bounds);

} // end of abstract class Distortion