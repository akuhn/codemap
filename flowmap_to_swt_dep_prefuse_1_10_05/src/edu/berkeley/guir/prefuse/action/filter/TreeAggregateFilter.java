package edu.berkeley.guir.prefuse.action.filter;

import java.awt.geom.Point2D;
import java.util.Iterator;

import edu.berkeley.guir.prefuse.AggregateItem;
import edu.berkeley.guir.prefuse.Display;
import edu.berkeley.guir.prefuse.ItemRegistry;
import edu.berkeley.guir.prefuse.NodeItem;
import edu.berkeley.guir.prefuse.graph.Node;

/**
 * Filter that adds aggregate items for elided subtrees. By default, garbage
 * collection for aggregate items is performed.
 * 
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public class TreeAggregateFilter extends Filter {

    private Point2D m_anchor = null;
    
    /**
     * Constructor.
     */
    public TreeAggregateFilter() {
       super(ItemRegistry.DEFAULT_AGGR_CLASS, true); 
    } //
    
	/**
	 * @see edu.berkeley.guir.prefuse.action.Action#run(edu.berkeley.guir.prefuse.ItemRegistry, double)
	 */
	public void run(ItemRegistry registry, double frac) {
        m_anchor = getLayoutAnchor(registry);
        
		double sx, sy, ex, ey, stheta, etheta;
		
		Iterator nodeIter = registry.getNodeItems();
		while ( nodeIter.hasNext() ) {
			NodeItem nitem  = (NodeItem)nodeIter.next();
			Node node       = (Node)nitem.getEntity();
            int diff = 0;
            if ( nitem.getChildCount() == 0 && 
                 (diff=(node.getEdgeCount()-nitem.getEdgeCount())) > 0 ) {				
				AggregateItem aggr = registry.getAggregateItem(node, true);
				Point2D       eloc = nitem.getEndLocation();
				Point2D       sloc = nitem.getStartLocation();

				aggr.setLocation   (sloc);
				aggr.updateLocation(eloc);
				aggr.setLocation   (eloc);
				
				setOrientation(aggr);
				
				aggr.setAggregateSize(diff);
			}
		}
        
		// optional garbage collection
        super.run(registry, frac);
	} //
    
    public Point2D getLayoutAnchor(ItemRegistry registry) {
        Point2D a = new Point2D.Double(0,0);
        if ( registry != null ) {
            Display d = registry.getDisplay(0);
            a.setLocation(d.getWidth()/2.0,d.getHeight()/2.0);
            d.getInverseTransform().transform(a,a);
        }
        return a;
    } //
    
    protected void setOrientation(AggregateItem item) {
        Point2D eloc = item.getEndLocation();
        Point2D sloc = item.getStartLocation();
        
        
        double ax, ay, sx, sy, ex, ey, etheta, stheta;
        ax = m_anchor.getX(); ay = m_anchor.getY();
        sx = sloc.getX()-ax; sy = sloc.getY()-ay;
        ex = eloc.getX()-ax; ey = eloc.getY()-ay;
        
        etheta = Math.atan2(ey, ex);
        stheta = ( sx == 0 && sy == 0 ? etheta : Math.atan2(sy, sx) );
        
        item.setStartOrientation(stheta);
        item.setOrientation(etheta);
        item.setEndOrientation(etheta);
    } //

} // end of class TreeAggregateFilter
