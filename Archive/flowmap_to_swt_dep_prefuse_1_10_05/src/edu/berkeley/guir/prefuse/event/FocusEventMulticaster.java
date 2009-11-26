package edu.berkeley.guir.prefuse.event;

import java.util.EventListener;

/**
 * Manages a list of listeners for focus events.
 * 
 * @author newbergr
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public class FocusEventMulticaster extends EventMulticaster
    implements FocusListener
{

	public static FocusListener add(FocusListener a, FocusListener b) {
		return (FocusListener) addInternal(a, b);
	} //

	public static FocusListener remove(FocusListener a, FocusListener b) {
		return (FocusListener) removeInternal(a, b);
	} //

    protected static EventListener addInternal(
            EventListener a, EventListener b)
    {
        if (a == null)
            return b;
        if (b == null)
            return a;
        return new FocusEventMulticaster(a, b);
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
    
	protected FocusEventMulticaster(EventListener a, EventListener b) {
        super(a, b);
	} //

    public void focusChanged(FocusEvent e) {
        ((FocusListener) a).focusChanged(e);
        ((FocusListener) b).focusChanged(e);
    } //
	
} // end of class FocusEventMulticaster
