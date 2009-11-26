package edu.berkeley.guir.prefuse.event;

import java.util.EventListener;

/**
 * Manages a list of listeners for events.
 * 
 * @author newbergr
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public abstract class EventMulticaster implements EventListener {

	protected final EventListener a, b;

	/** 
	 * Returns the resulting multicast listener after removing the
	 * old listener from listener-l.
	 * If listener-l equals the old listener OR listener-l is null, 
	 * returns null.
	 * Else if listener-l is an instance of AWTEventMulticaster, 
	 * then it removes the old listener from it.
	 * Else, returns listener l.
	 * @param l the listener being removed from
	 * @param oldl the listener being removed
	 */
	protected static EventListener removeInternal(
		EventListener l,
		EventListener oldl) {
		if (l == oldl || l == null) {
			return null;
		} else if (l instanceof EventMulticaster) {
			return ((EventMulticaster) l).remove(oldl);
		} else {
			return l; // it's not here
		}
	} //

	protected EventMulticaster(EventListener a, EventListener b) {
		this.a = a;
		this.b = b;
	} //

	protected abstract EventListener remove(EventListener oldl);

	private static int getListenerCount(EventListener l) {
		if (l instanceof EventMulticaster) {
			EventMulticaster mc = (EventMulticaster) l;
			return getListenerCount(mc.a) + getListenerCount(mc.b);
		}
		// Delete nulls. 
		else {
			return (l == null) ? 0 : 1;
		}
	} //

	private static int populateListenerArray(
		EventListener[] a,
		EventListener l,
		int index) {
		if (l instanceof EventMulticaster) {
			EventMulticaster mc = (EventMulticaster) l;
			int lhs = populateListenerArray(a, mc.a, index);
			return populateListenerArray(a, mc.b, lhs);
		} else if (l != null) {
			a[index] = l;
			return index + 1;
		}
		// Delete nulls. 
		else {
			return index;
		}
	} //

	public static EventListener[] getListeners(
        EventListener l, Class listenerType)
    {
		int n = getListenerCount(l);
		EventListener[] result = (EventListener[])
            java.lang.reflect.Array.newInstance(listenerType, n);
		populateListenerArray(result, l, 0);
		return result;
	} //
	
} // end of class EventMulticaster
