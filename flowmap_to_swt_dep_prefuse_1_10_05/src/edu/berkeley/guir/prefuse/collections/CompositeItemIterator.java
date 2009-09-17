package edu.berkeley.guir.prefuse.collections;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import edu.berkeley.guir.prefuse.ItemRegistry.ItemEntry;

/**
 * Iterates over graph items, in order.
 * 
 * @author jheer
 * @author alann
 */
public class CompositeItemIterator implements Iterator {
	
	private Iterator m_iter[];
	private Object m_item[];
	private Comparator m_comp;
	private boolean m_reverse;
	private int m_emptyCount;

	/**
	 * Constructor. Creates a new iterator over a merged list of objects.
	 * Ordering is determined by the provided Comparator instance and the
	 * reverse flag.
	 * @param itemLists the list of lists to iterate over
	 * @param c the Comparator used to order the objects
	 * @param reverse indicates whether or not to iterate in <i>reverse</i> order.
	 */
	public CompositeItemIterator(List itemLists, Comparator c, 
            boolean visibleOnly, boolean reverse)
	{
		int size = itemLists.size();
		m_emptyCount = 0;
		m_iter = new Iterator[size];
		m_item = new Object[size];
		for ( int i = 0; i < size; i++ ) {
			List itemList = ((ItemEntry)itemLists.get(i)).getItemList();
            // get the right kind of iterator
            if ( visibleOnly )
                m_iter[i] = new VisibleItemIterator(itemList, reverse);
            else if ( reverse )
                m_iter[i] = new ReverseListIterator(itemList);
            else 
                m_iter[i] = itemList.iterator();
			// initialize item pool
            if ( m_iter[i].hasNext() ) {
				m_item[i] = m_iter[i].next();
			} else {
				m_emptyCount++;
			}
		}
		m_reverse = reverse;
		m_comp    = c;
	} //
  
	/**
	 * @see java.util.Iterator#hasNext()
	 */
	public boolean hasNext() {
		return ( m_emptyCount < m_item.length );    	
	} //

	/**
	 * @see java.util.Iterator#next()
	 */
	public Object next() {
		if ( !hasNext() ) { throw new NoSuchElementException();	}
		
		// select next item to return
		int cur = -1;
		for ( int i = 0; i < m_item.length; i++ ) {
			if ( m_item[i] != null ) {
				if ( cur == -1 ) {
					cur = i;
				} else {
					int c = m_comp.compare(m_item[cur], m_item[i]);
					if ( m_reverse ) { c *= -1;	}
					cur = ( c < 0 ? cur : i );
				}
			}
		}
		Object nextItem = null;
		try {
			nextItem = m_item[cur];
		} catch ( Exception e ) {
			System.out.println("");
		}
		
		// dequeue object to take it's place
		m_item[cur] = (m_iter[cur].hasNext() ? m_iter[cur].next() : null);
		if ( m_item[cur] == null )
			m_emptyCount++;
		
		// return 
    	return nextItem;
	} //

	/**
	 * We don't support removals.
   	 */
 	public void remove() {
    	throw new UnsupportedOperationException();
	} //
  
} // end of class CompositeItemIterator
