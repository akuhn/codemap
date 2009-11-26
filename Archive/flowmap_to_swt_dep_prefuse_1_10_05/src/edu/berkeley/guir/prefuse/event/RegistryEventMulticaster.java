package edu.berkeley.guir.prefuse.event;

import java.util.EventListener;

import edu.berkeley.guir.prefuse.VisualItem;

/**
 * Manages a list of listeners for prefuse item registry events.
 * 
 * @author newbergr
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public class RegistryEventMulticaster extends EventMulticaster
    implements ItemRegistryListener
{
	public static ItemRegistryListener add(
		ItemRegistryListener a,
		ItemRegistryListener b) {
		return (ItemRegistryListener) addInternal(a, b);
	} //

	public static ItemRegistryListener remove(
		ItemRegistryListener l,
		ItemRegistryListener oldl) {
		return (ItemRegistryListener) removeInternal(l, oldl);
	} //

	public void registryItemAdded(VisualItem item) {
		((ItemRegistryListener) a).registryItemAdded(item);
		((ItemRegistryListener) b).registryItemAdded(item);
	} //

	public void registryItemRemoved(VisualItem item) {
		((ItemRegistryListener) a).registryItemRemoved(item);
		((ItemRegistryListener) b).registryItemRemoved(item);
	} //

    protected static EventListener addInternal(
            EventListener a, EventListener b)
    {
        if (a == null)
            return b;
        if (b == null)
            return a;
        return new RegistryEventMulticaster(a, b);
    } //
    
	protected EventListener remove(EventListener oldl) {
		if (oldl == a)
			return b;
		if (oldl == b)
			return a;
		EventListener a2 = removeInternal(a, oldl);
		EventListener b2 = removeInternal(b, oldl);
		if (a2 == a && b2 == b) {
			return this; // it's not here
		}
		return addInternal(a2, b2);
	} //
    
	protected RegistryEventMulticaster(EventListener a, EventListener b) {
		super(a,b);
	} //
    
} // end of class RegistryEventMulticaster
