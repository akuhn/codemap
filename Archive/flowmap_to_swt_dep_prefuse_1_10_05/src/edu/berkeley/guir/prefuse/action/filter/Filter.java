package edu.berkeley.guir.prefuse.action.filter;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import edu.berkeley.guir.prefuse.ItemRegistry;
import edu.berkeley.guir.prefuse.action.AbstractAction;

/**
 * Abstract class providing garbage collection facilities for Action instances
 * that perform filtering (i.e. mapping from abstract graph data to visual
 * representations).
 *
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public abstract class Filter extends AbstractAction {

    private Set m_classes;
    private boolean  m_gc;
    
    /**
     * Creates a new filter associated with the given item class that
     * optionally performs garbage collection.
     * @param itemClass the item class associated with this filter
     * @param gc indicates whether garbage collection should be performed
     */
    public Filter(String itemClass, boolean gc) {
        m_classes = new HashSet(3);
        if ( itemClass != null )
            m_classes.add(itemClass);
        m_gc = gc;
    } //
    
    /**
     * Creates a new filter associated with the given item classes that
     * optionally performs garbage collection.
     * @param itemClasses the item classes associated with this filter
     * @param gc indicates whether garbage collection should be performed
     */
    public Filter(String[] itemClasses, boolean gc) {
        m_classes = new HashSet(3);
        for ( int i=0; i<itemClasses.length; i++ ) {
            if ( itemClasses[i] != null )
                m_classes.add(itemClasses[i]);
        }
        m_gc = gc;
    } //
    
    /**
     * Indicates whether or not this filter performs garbage collection.
     * @return true if garbage collection enabled, false otherwise
     */
    public boolean isGarbageCollectEnabled() {
        return m_gc;
    } //
    
    /**
     * Sets a flag determining if garbage collection is performed
     * @param s the flag indicating if garbage collected is performed
     */
    public void setGarbageCollect(boolean s) {
        m_gc = s;
    } //
    
    /**
     * Gets the item classes associated with this Filter. Item classes are
     * repreesnted as String instances corresponding to entries in an 
     * ItemRegistry.
     * @return the item classes associated with this Filter
     */
    public String[] getItemClasses() {
        return (String[])m_classes.toArray(new String[m_classes.size()]);
    } //
    
    /**
     * Associate an item class with this filter
     * @param itemClass the itemClass to add
     */
    public void addItemClass(String itemClass) {
        m_classes.add(itemClass);
    } //
    
    /**
     * Dissociate an item class with this filter
     * @param itemClass the itemClass to remove
     */
    public void removeItemClass(String itemClass) {
        m_classes.remove(itemClass);
    } //
    
    /**
     * Signals the ItemRegistry to perform garbage collection, if enabled, on
     * the item classes registered with this filter.
     */
    public void run(ItemRegistry registry, double frac) {
        if ( m_gc ) {
            Iterator iter = m_classes.iterator();
            while ( iter.hasNext() ) {
                String iclass = (String)iter.next();
                registry.garbageCollect(iclass);
            }
        }
    } //

} // end of class Filter
