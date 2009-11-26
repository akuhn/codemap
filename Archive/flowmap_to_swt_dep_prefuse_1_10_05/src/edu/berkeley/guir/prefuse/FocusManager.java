package edu.berkeley.guir.prefuse;

import java.util.HashMap;
import java.util.Iterator;

import edu.berkeley.guir.prefuse.focus.DefaultFocusSet;
import edu.berkeley.guir.prefuse.focus.FocusSet;
import edu.berkeley.guir.prefuse.graph.Entity;

/**
 * This class helps manage focus and/or selected items in a visualization.
 * It assumes there is at most a single user-selected focus, corresponding
 * to the user's current locus of attention (e.g. a moused-over, or previously
 * clicked item). However, there can also be any number of other 
 * {@link edu.berkeley.guir.prefuse.focus.FocusSet FocusSets},
 * including search results, or multiple user selections. This class supports 
 * the storage, retrieval, and monitoring of such focus items and sets. Because
 * potential focus items may not yet be visualized, focus sets store
 * {@link edu.berkeley.guir.prefuse.graph.Entity Entity}
 * instances rather than {@link VisualItem VisualItem} instances.
 *
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public class FocusManager {

    /**
     * Object key used for the default focus set.
     */
    public static final String DEFAULT_KEY    = "default";
    
    /**
     * Object key that can be used for a focus set of selected items.
     */
    public static final String SELECTION_KEY  = "selection";
    
    /**
     * Object key that can be used for a focus set of search results.
     */
    public static final String SEARCH_KEY     = "search";
    
    private HashMap m_focusSets;

    /**
     * Creates a new FocusManager instance.
     */
    public FocusManager() {
        m_focusSets = new HashMap();
        setDefaultFocusSet(new DefaultFocusSet());
    } //
    
    /**
     * Retrieves the focus set associated with the given key, if any.
     * @param key the key mapping to the desired focus set
     * @return the corresponding focus set, or null if none
     */
    public FocusSet getFocusSet(Object key) {
        return (FocusSet)m_focusSets.get(key);
    } //
    
    /**
     * Adds a focus set to this manager.
     * @param key the key for the focus set to add
     * @param set the focus set to add
     */
    public void putFocusSet(Object key, FocusSet set) {
        m_focusSets.put(key, set);
    } //
    
    /**
     * Returns the default focus set.
     * @return the default focus set
     */
    public FocusSet getDefaultFocusSet() {
        return (FocusSet)m_focusSets.get(DEFAULT_KEY);
    } //
    
    /**
     * Sets the default focus set.
     * @param set the focus set to make the new default
     */
    public void setDefaultFocusSet(FocusSet set) {
        m_focusSets.put(DEFAULT_KEY, set);
    } //
    
    /**
     * Returns an iterator over the various focus sets in this manager.
     * @return an iterator over the focus sets registered with this manager
     */
    public Iterator getFocusSetIterator() {
        return m_focusSets.values().iterator();
    } //
    
    /**
     * Tests if the given entity is in the focus set corresponding to the
     * given key.
     * @param key the key for the focus set to test
     * @param entity the entity to test for focus set membership
     * @return true if the entity is in the focus set, false otherwise
     */
    public boolean isFocus(Object key, Entity entity) {
        FocusSet set = getFocusSet(key);
        return ( set==null ? false : set.contains(entity) );
    } //
    
    /**
     * Tests if the given entity is in any of this manager's focus sets.
     * @param entity the entity to test for focus set membership
     * @return true if the entity is in a focus set registered with this
     *  membership, false otherwise.
     */
    public boolean isFocus(Entity entity) {
        Iterator iter = m_focusSets.keySet().iterator();
        while ( iter.hasNext() ) {
            FocusSet set = (FocusSet)m_focusSets.get(iter.next());
            if ( set.contains(entity) )
                return true;
        }
        return false;
    } //
    
} // end of class FocusManager
