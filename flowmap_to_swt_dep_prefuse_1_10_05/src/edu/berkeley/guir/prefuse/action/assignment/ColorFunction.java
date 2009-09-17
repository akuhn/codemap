package edu.berkeley.guir.prefuse.action.assignment;

import java.awt.Color;
import java.awt.Paint;
import java.util.Iterator;

import edu.berkeley.guir.prefuse.VisualItem;
import edu.berkeley.guir.prefuse.ItemRegistry;
import edu.berkeley.guir.prefuse.action.AbstractAction;

/**
 * Simple ColorFunction which returns "black" for the draw color and
 * "gray" for the fill color when a color is requested. Subclasses 
 * should override the getColor() and getFillColor() methods to provide
 * custom color selection functions.
 * 
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public class ColorFunction extends AbstractAction {
    
	public void run(ItemRegistry registry, double frac) {
		Iterator itemIter = registry.getItems();
		while ( itemIter.hasNext() ) {
			VisualItem item = (VisualItem)itemIter.next();
            Paint c = getColor(item), o = item.getColor();
			if ( o == null ) item.setColor(getInitialColor(item));			
			item.updateColor(c);			
			item.setColor(c);
			
			c = getFillColor(item); o = item.getFillColor();
			if ( o == null ) item.setFillColor(getInitialFillColor(item));
			item.updateFillColor(c);			
			item.setFillColor(c);
		}
	} //

	protected Paint getInitialColor(VisualItem item) {
		return getColor(item);
	} //
	
	protected Paint getInitialFillColor(VisualItem item) {
		return getFillColor(item);
	} //

	public Paint getColor(VisualItem item) {
        if ( item.isFocus() ) {
            return Color.RED;
        } else if ( item.isHighlighted() ) {
            return Color.BLUE;
        } else {
            return Color.BLACK;   
        }
	} //

	public Paint getFillColor(VisualItem item) {
        return Color.WHITE;
	} //

} // end of class ColorFunction
