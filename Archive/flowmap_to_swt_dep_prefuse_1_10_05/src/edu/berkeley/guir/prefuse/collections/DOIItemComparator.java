package edu.berkeley.guir.prefuse.collections;

import java.util.Comparator;

import edu.berkeley.guir.prefuse.AggregateItem;
import edu.berkeley.guir.prefuse.EdgeItem;
import edu.berkeley.guir.prefuse.VisualItem;
import edu.berkeley.guir.prefuse.NodeItem;

/**
 * Compares items based upon computed degree-of-interest (DOI) values.
 * 
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public class DOIItemComparator implements Comparator {

	/**
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Object o1, Object o2) {
		if ( !(o1 instanceof VisualItem && o2 instanceof VisualItem) ) {
			throw new IllegalArgumentException();
		}
		
		VisualItem item1 = (VisualItem)o1;
		VisualItem item2 = (VisualItem)o2;
		
		if ( item1 instanceof NodeItem ) {
			if ( item2 instanceof NodeItem ) {
				double doi1 = ((NodeItem)item1).getDOI();
				double doi2 = ((NodeItem)item2).getDOI();				
				return ( doi1 > doi2 ? 1 : ( doi1 == doi2 ? 0 : -1 ) );				
			} else {
				return 1;
			}
		} else if ( item2 instanceof NodeItem ) {
			return -1;
		} else if ( item1 instanceof EdgeItem ) {
			if ( item2 instanceof EdgeItem ) {
                EdgeItem e1 = (EdgeItem)item1, e2 = (EdgeItem)item2;
				double doi1a = ((NodeItem)e1.getFirstNode()).getDOI();
				double doi2a = ((NodeItem)e2.getFirstNode()).getDOI();
				double doi1b = ((NodeItem)e1.getSecondNode()).getDOI();
				double doi2b = ((NodeItem)e2.getSecondNode()).getDOI();
				double doi1 = Math.max(doi1a, doi1b);
				double doi2 = Math.max(doi2a, doi2b);				
				return ( doi1 > doi2 ? 1 : ( doi1 == doi2 ? 0 : -1 ) );
			} else {
				return 1;
			}
		} else if ( item2 instanceof EdgeItem ) {
			return -1;
		} else if ( item1 instanceof AggregateItem ) {
			if ( item2 instanceof AggregateItem ) {
				double doi1 = ((AggregateItem)item1).getNodeItem().getDOI();
				double doi2 = ((AggregateItem)item2).getNodeItem().getDOI();				
				return ( doi1 > doi2 ? 1 : ( doi1 == doi2 ? 0 : -1 ) );
			} else {
				return 1;
			}
		} else if ( item2 instanceof AggregateItem ) {
			return -1;
		} else {
			return 0;
		}
	} //

} // end of class DefaultItemComparator
