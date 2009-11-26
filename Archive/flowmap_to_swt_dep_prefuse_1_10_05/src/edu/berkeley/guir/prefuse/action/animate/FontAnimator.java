package edu.berkeley.guir.prefuse.action.animate;

import java.awt.Font;
import java.util.Iterator;

import edu.berkeley.guir.prefuse.ItemRegistry;
import edu.berkeley.guir.prefuse.VisualItem;
import edu.berkeley.guir.prefuse.action.AbstractAction;
import edu.berkeley.guir.prefuse.util.FontLib;

/**
 * Interpolates between starting and ending Fonts for VisualItems
 * during an animation. Font sizes are interpolated linearly. If the
 * animation fraction is under 0.5, the face and style of the starting
 * font are used, otherwise the face and style of the second font are
 * applied.
 * 
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public class FontAnimator extends AbstractAction {

	/**
	 * @see edu.berkeley.guir.prefuse.action.Action#run(edu.berkeley.guir.prefuse.ItemRegistry, double)
	 */
	public void run(ItemRegistry registry, double frac) {
		Iterator iter = registry.getItems();
		while ( iter.hasNext() ) {
			VisualItem item = (VisualItem)iter.next();
            Font f1 = item.getStartFont(), f2 = item.getEndFont();
            item.setFont(FontLib.getIntermediateFont(f1,f2,frac));
		}
	} //

} // end of class FontAnimator
