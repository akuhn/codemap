package edu.berkeley.guir.prefuse.action.animate;

import java.awt.Color;
import java.awt.Paint;
import java.util.Iterator;

import edu.berkeley.guir.prefuse.ItemRegistry;
import edu.berkeley.guir.prefuse.VisualItem;
import edu.berkeley.guir.prefuse.action.AbstractAction;
import edu.berkeley.guir.prefuse.util.ColorLib;

/**
 * Linearly interpolates between starting and ending colors for VisualItems
 * during an animation. Custom color interpolators can be written by 
 * subclassing this class and overriding the 
 * <code>getInterpolatedColor()</code> method.
 * 
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public class ColorAnimator extends AbstractAction {

	/**
	 * @see edu.berkeley.guir.prefuse.action.Action#run(edu.berkeley.guir.prefuse.ItemRegistry, double)
	 */
	public void run(ItemRegistry registry, double frac) {
		Iterator iter = registry.getItems();
		while ( iter.hasNext() ) {
			VisualItem item = (VisualItem)iter.next();
			
            Paint p1 = item.getStartColor(), p2 = item.getEndColor();
            if ( p1 instanceof Color && p2 instanceof Color ) {
                Color c1 = (Color)p1, c2 = (Color)p2;
                item.setColor(ColorLib.getIntermediateColor(c1,c2,frac));
            } else {
                throw new IllegalStateException("Can't interpolate Paint"
                        + " instances that are not of type Color");
            }
			
			Paint f1 = item.getStartFillColor(), f2 = item.getEndFillColor();
            if ( f1 instanceof Color && f2 instanceof Color ) {
                Color c1 = (Color)f1, c2 = (Color)f2;
                item.setFillColor(ColorLib.getIntermediateColor(c1,c2,frac));
            }
		}
	} //

} // end of class ColorAnimator
