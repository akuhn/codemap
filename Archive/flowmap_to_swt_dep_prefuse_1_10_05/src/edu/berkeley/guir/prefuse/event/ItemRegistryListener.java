package edu.berkeley.guir.prefuse.event;

import java.util.EventListener;

import edu.berkeley.guir.prefuse.VisualItem;

/**
 * A listener interface through which components can be notified
 * of changes in registry bindings. 
 * 
 * Apr 25, 2003 - alann - Created class
 * 
 * @author alann
 */
public interface ItemRegistryListener extends EventListener {
  
  /**
   * Indicates a binding to a new VisualItem has been established.
   * @param item the new VisualItem
   */
  public void registryItemAdded(VisualItem item);
  
  /**
   * Indicates a binding to a VisualItem has been removed.
   * @param item the removed VisualItem
   */
  public void registryItemRemoved(VisualItem item);
  
} // end of class ItemRegistryListener
