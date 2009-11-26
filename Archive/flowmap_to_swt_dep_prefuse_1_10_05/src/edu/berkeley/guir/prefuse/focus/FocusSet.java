package edu.berkeley.guir.prefuse.focus;

import java.util.Collection;
import java.util.Iterator;

import edu.berkeley.guir.prefuse.event.FocusListener;
import edu.berkeley.guir.prefuse.graph.Entity;

/**
 * An interface for sets of currently "in focus" entities, such
 * as those corresponding to clicked or selected items, or
 * search results. As these various entities may not yet be visualized
 * when they are added to the set, the FocusSet manages collections
 * of abstract graph Entity instances, rather than their visualized
 * VisualItem analogues.
 *
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public interface FocusSet {

    /**
     * Adds a listener to monitor changes to this FocusSet.
     * @param fl the FocusListener to add
     */
    public void addFocusListener(FocusListener fl);
    
    /**
     * Removes a listener currently monitoring this FocusSet.
     * @param fl the FocusListener to remove
     */
    public void removeFocusListener(FocusListener fl);
    
    /**
     * Adds a new Entity to this FocusSet.
     * @param focus the Entity to add
     */
    public void add(Entity focus);
    
    /**
     * Adds a Collection of Entity instances to this FocusSet. All members of
     * this Collection should be of type Entity.
     * @param foci the Collection of Entity instances to add.
     */
    public void add(Collection foci);
    
    /**
     * Removes an Entity from this FocusSet.
     * @param focus the Entity to remove
     */
    public void remove(Entity focus);
    
    /**
     * Removes a Collection of Entity instances from this FocusSet. All members 
     * of this Collection should already be members of this set.
     * @param foci the Collection of Entity instances to remove.
     */
    public void remove(Collection foci);
    
    /**
     * Sets an Entity as the single focus in this FocusSet. This causes any
     * previous members of the focus set to be removed.
     * @param focus the Entity to set
     */
    public void set(Entity focus);
    
    /**
     * Sets a Collection of Entity instances as the foci in this FocusSet. 
     * All members of this Collection should of type Entity. This method
     * causes any previous members of the focus set to be removed.
     * @param foci the Collection of Entity instances to remove.
     */
    public void set(Collection foci);
    
    /**
     * Clears this FocusSet, removing all current members.
     */
    public void clear();
    
    /**
     * Returns an Iterator over the members of this FocusSet.
     * @return an Iterator over the members of this FocusSet
     */
    public Iterator iterator();
    
    /**
     * Returns the size of this FocusSet.
     * @return the number of elements in this FocusSet
     */
    public int size();
    
    /**
     * Indicates if a given Entity is contained within this FocusSet.
     * @param entity the Entity to check for containment
     * @return true if this Entity is in the FocusSet, false otherwise
     */
    public boolean contains(Entity entity);
    
} // end of interface FocusSet
