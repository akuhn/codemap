package edu.berkeley.guir.prefuse.action.assignment;

import java.util.Iterator;

import edu.berkeley.guir.prefuse.VisualItem;
import edu.berkeley.guir.prefuse.ItemRegistry;
import edu.berkeley.guir.prefuse.action.AbstractAction;

/**
 * Simple SizeFunction that blindly returns a size of "1" for all
 * items. Subclasses should override the getSize() method to provide
 * custom size assignment for VisualItems.
 * 
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public class SizeFunction extends AbstractAction {

	public void run(ItemRegistry registry, double frac) {
		Iterator itemIter = registry.getItems();
		while ( itemIter.hasNext() ) {
			VisualItem item = (VisualItem)itemIter.next();
			double size = getSize(item);
			item.updateSize(size);
			item.setSize(size);
		}
	} //
	
	public double getSize(VisualItem item) {
		return 1;
	} //

} // end of class SizeFunction
