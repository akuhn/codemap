package edu.berkeley.guir.prefuse;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Factory class for {@link VisualItem VisualItem} instances. This allows object
 * initialization to be consolidated in a single location and allocated objects
 * to be re-used by maintaining a pool of item references.
 * 
 * This class works closely with the {@link ItemRegistry ItemRegistry}, but is
 * implemented separately to provide encapsulation and simplify design.
 * 
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public class ItemFactory {
	
	private static final Class LIST_TYPE = LinkedList.class;
	private static final Class MAP_TYPE  = HashMap.class;
	
	private class FactoryEntry {
		FactoryEntry(String itemClass, Class classType, int maxSize) {
			try {
				maxItems = maxSize;
				name     = itemClass;
				type     = classType;
				itemList = (List)LIST_TYPE.newInstance();
			} catch ( Exception e) {
				e.printStackTrace();
			}
		} //
		int     maxItems;
		Class   type;
		String  name;
		List    itemList;
	} // end of inner class ItemEntry
	
	private Map m_entryMap;
	
	/**
	 * Constructor. Creates a new ItemFactory instance.
	 */
	public ItemFactory() {
		try {
			m_entryMap = (Map)MAP_TYPE.newInstance();
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	} //
	
    /**
     * Add a new item class for which to maintain an item pool.
     * @param itemClass the label for the item class
     * @param classType the Java Class of the items
     * @param maxItems the maximum size of the item pool
     */
	public void addItemClass(String itemClass, Class classType, int maxItems) {
		FactoryEntry fentry = new FactoryEntry(itemClass, classType, maxItems);
		m_entryMap.put(itemClass, fentry);
	} //
	
	// ========================================================================
	// == FACTORY METHODS =====================================================
	
    /**
     * Get an item from an item pool. Create a new item if the pool is empty.
     */
	public VisualItem getItem(String itemClass) {
		FactoryEntry fentry = (FactoryEntry)m_entryMap.get(itemClass);
		if ( fentry != null ) {
			VisualItem item = null;
			if ( fentry.itemList.isEmpty() ) {
				try {
					item = (VisualItem)fentry.type.newInstance();
				} catch ( Exception e ) {
					e.printStackTrace();
				}
			} else {
				item = (VisualItem)fentry.itemList.remove(0);
			}
			return item;
		} else {
			throw new IllegalArgumentException("The input string must be a"
						+ " recognized item class!");
		}
	} //
	
	/**
	 * Reclaim an item into an item pool. Used to avoid object initialization
	 * costs. If maximum pool sizes are reached, this item will not be
	 * reclaimed. In this case it should have NO remaining references, allowing
	 * it to be garbage collected.
	 * @param item the VisualItem to reclaim
	 */
	public void reclaim(VisualItem item) {
		String itemClass    = item.getItemClass();
		FactoryEntry fentry = (FactoryEntry)m_entryMap.get(itemClass);
		
		// clear any references within the item
		item.clear();
		
		// Determine which "bin" the item belongs in, then add it
		// if the maximum has not yet been reached.
		if ( fentry.itemList.size() <= fentry.maxItems ) {
			fentry.itemList.add(item);
		}
	} //

} // end of class ItemFactory
