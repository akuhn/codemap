package edu.berkeley.guir.prefuse.collections;

import java.util.Comparator;

import edu.berkeley.guir.prefuse.AggregateItem;
import edu.berkeley.guir.prefuse.EdgeItem;
import edu.berkeley.guir.prefuse.VisualItem;
import edu.berkeley.guir.prefuse.NodeItem;

/**
 * Comparator that sorts items based on type and focus status.
 * 
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public class DefaultItemComparator implements Comparator {

	/**
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Object o1, Object o2) {
		if ( !(o1 instanceof VisualItem && o2 instanceof VisualItem) ) {
			throw new IllegalArgumentException();
		}
		
		VisualItem item1 = (VisualItem)o1;
		VisualItem item2 = (VisualItem)o2;
        
		boolean f1 = item1.isFocus();
		boolean f2 = item2.isFocus();
		
        if ( f1 && !f2 )
            return 1;
        else if ( !f1 && f2 )
            return -1;
        
        boolean h1 = item1.isHighlighted();
        boolean h2 = item2.isHighlighted();
        
        boolean n1 = item1 instanceof NodeItem;
        boolean n2 = item2 instanceof NodeItem;
        
        if ( n1 && !n2 )
            return 1;
        else if ( !n1 && n2 )
            return -1;
        else if ( n1 && n2 ) {
            if ( h1 && !h2 )
                return 1;
            else if ( !h1 && h2 )
                return -1;
            else {
                boolean a1 = item1 instanceof AggregateItem;
                boolean a2 = item2 instanceof AggregateItem;
                return (a1 && !a2 ? -1 : (!a1 && a2 ? 1 : 0));
            }
        }
        
        boolean e1 = item1 instanceof EdgeItem;
        boolean e2 = item2 instanceof EdgeItem;
        
        if ( e1 && !e2 )
            return 1;
        else if ( !e1 && e2 )
            return -1;
        else if ( h1 && !h2 )
                return 1;
        else if ( !h1 && h2 )
            return -1;
        else
            return 0;
	} //

} // end of class DefaultItemComparator
