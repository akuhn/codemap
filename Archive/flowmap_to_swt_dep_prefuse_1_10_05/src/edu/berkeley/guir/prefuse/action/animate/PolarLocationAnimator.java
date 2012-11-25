package edu.berkeley.guir.prefuse.action.animate;

import java.awt.geom.Point2D;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import edu.berkeley.guir.prefuse.AggregateItem;
import edu.berkeley.guir.prefuse.Display;
import edu.berkeley.guir.prefuse.ItemRegistry;
import edu.berkeley.guir.prefuse.NodeItem;
import edu.berkeley.guir.prefuse.VisualItem;
import edu.berkeley.guir.prefuse.action.AbstractAction;
import edu.berkeley.guir.prefuse.event.FocusEvent;
import edu.berkeley.guir.prefuse.event.FocusListener;
import edu.berkeley.guir.prefuse.graph.Entity;
import edu.berkeley.guir.prefuse.graph.Node;
import edu.berkeley.guir.prefuse.graph.TreeNode;

/**
 * Interpolates between starting and ending display locations by linearly
 * interpolating between polar co-ordinates.
 * 
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public class PolarLocationAnimator extends AbstractAction implements FocusListener {

	private static final double TWO_PI = 2*Math.PI;
	
    private Point2D m_anchor = new Point2D.Double();
	private Set m_linear = new HashSet();
    private ItemRegistry m_registry;

    private Point2D getAnchor(ItemRegistry registry) {
        Display d = registry.getDisplay(0);
        m_anchor.setLocation(d.getWidth()/2,d.getHeight()/2);
        d.getAbsoluteCoordinate(m_anchor, m_anchor);
        return m_anchor;
    }
    
	public void run(ItemRegistry registry, double frac) {
	    if ( registry != m_registry ) {
	        if ( m_registry != null )
                m_registry.getDefaultFocusSet().removeFocusListener(this);
            m_registry = registry;
            m_registry.getDefaultFocusSet().addFocusListener(this);
        }
        
        Point2D anchor = getAnchor(registry);
		
		double ax, ay, sx, sy, ex, ey, x, y;
		double dt1, dt2, sr, st, er, et, r, t, stt, ett;

		ax = anchor.getX();
		ay = anchor.getY();
		
		Iterator itemIter = m_registry.getItems();
		while ( itemIter.hasNext() ) {
			VisualItem item = (VisualItem)itemIter.next();
			Point2D startLoc = item.getStartLocation();
			Point2D endLoc   = item.getEndLocation();
            
			sx = startLoc.getX() - ax;
			sy = startLoc.getY() - ay;
			ex = endLoc.getX() - ax;
			ey = endLoc.getY() - ay;

			// linearly interpolate to and from focus
			if ( m_linear.contains(item) ) {
				x = startLoc.getX() + frac * (endLoc.getX()-startLoc.getX());
				y = startLoc.getY() + frac * (endLoc.getY()-startLoc.getY());
				item.setLocation(x,y);
			} else {
				sr = Math.sqrt(sx*sx + sy*sy);
                if ( item instanceof NodeItem && Double.isNaN(sr) ) {
                    // for reasons unknown to me, some versions of Java screw up
                    // a perfectly legal floating point calc, returning NaN(!)
                    // here is a crappy hack that seems to work -- just try again!
                    sr = Math.sqrt(sx*sx + sy*sy);
                }
                
				st = Math.atan2(sy,sx);
                
				er = Math.sqrt(ex*ex + ey*ey);
                if ( item instanceof NodeItem && Double.isNaN(er) ) {
                    // for reasons unknown to me, some versions of Java screw up
                    // a perfectly legal floating point calc, returning NaN(!)
                    // here is a crappy hack that seems to work -- just try again!
                    er = Math.sqrt(ex*ex + ey*ey);
                }
                
				et = Math.atan2(ey,ex);
				stt = translate(st);
				ett = translate(et);
				
				dt1 = et - st;
				dt2 = ett - stt;
				
				if ( Math.abs(dt1) < Math.abs(dt2) ) {
					t = st + frac * dt1;
				} else {
					t = stt + frac * dt2;
				}
				r = sr + frac * (er - sr);
							
				x = Math.round(ax + r*Math.cos(t));
				y = Math.round(ay + r*Math.sin(t));
                
				item.setLocation(x,y);
			}
			
			if ( item instanceof AggregateItem ) {
				AggregateItem aggr = (AggregateItem)item;
				st = aggr.getStartOrientation();
				et = aggr.getEndOrientation();
				stt = translate(st);
				ett = translate(et);
				
				dt1 = et - st;
				dt2 = ett - stt;
				
				if ( Math.abs(dt1) < Math.abs(dt2) ) {
					t = st + frac * dt1;
				} else {
					t = stt + frac * dt2;
				}
				aggr.setOrientation(t);
			}
		}
	} //

	private double translate(double t) {
		return ( t < 0 ? t+TWO_PI : t );
	} //

    public void focusChanged(FocusEvent e) {
        if ( e.getEventType() == FocusEvent.FOCUS_SET ) {
            m_linear.clear();
            Entity[] rem = e.getRemovedFoci();
            if ( rem.length == 0 ) return;
            if ( rem[0] instanceof Node ) {
                TreeNode p = m_registry.getNodeItem((Node)rem[0]);
                for ( ; p != null; p = p.getParent() )        
                    m_linear.add(p);
            }
        }
    } //

} // end of class PolarLocationAnimator
