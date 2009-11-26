package edu.berkeley.guir.prefuse.action.animate;

import java.awt.geom.Point2D;
import java.util.Iterator;

import edu.berkeley.guir.prefuse.VisualItem;
import edu.berkeley.guir.prefuse.ItemRegistry;
import edu.berkeley.guir.prefuse.action.AbstractAction;

/**
 * This class linearly interpolates a node position between two positions. This
 * is useful for performing animated transitions.
 * 
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public class LocationAnimator extends AbstractAction {

	public void run(ItemRegistry registry, double frac) {
		double sx, sy, ex, ey, x, y;
		double st, et, t;
		
		Iterator itemIter = registry.getItems();
		while ( itemIter.hasNext() ) {
			VisualItem item = (VisualItem)itemIter.next();
			Point2D startLoc = item.getStartLocation();
			Point2D endLoc   = item.getEndLocation();
						
			sx = startLoc.getX();
			sy = startLoc.getY();
			ex = endLoc.getX();
			ey = endLoc.getY();
			
			x = sx + frac * (ex - sx);
			y = sy + frac * (ey - sy);
			
			item.setLocation(x,y);
			
//			if ( item instanceof AggregateItem ) {
//				AggregateItem aggr = (AggregateItem)item;
//				st = aggr.getStartOrientation();
//				et = aggr.getEndOrientation();
//				t  = st + frac * (et - st);
//			}
		}		
	} //

} // end of class LocationAnimator
