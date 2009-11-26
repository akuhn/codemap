package edu.berkeley.guir.prefuse.action.assignment;

import java.awt.Font;
import java.util.Iterator;

import edu.berkeley.guir.prefuse.VisualItem;
import edu.berkeley.guir.prefuse.ItemRegistry;
import edu.berkeley.guir.prefuse.action.AbstractAction;

/**
 * Simple <code>FontFunction</code> that blindly returns a null 
 * <code>Font</code> for all items. Subclasses should override the 
 * <code>getFont()</code> method to provide custom Font assignment
 * for VisualItems.
 * 
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public class FontFunction extends AbstractAction {

	public void run(ItemRegistry registry, double frac) {
		Iterator itemIter = registry.getItems();
		while ( itemIter.hasNext() ) {
			VisualItem item = (VisualItem)itemIter.next();
			Font font = getFont(item);
			item.setFont(font);
		}
	} //
	
	public Font getFont(VisualItem item) {
		return null;
	} //

} // end of class FontFunction
