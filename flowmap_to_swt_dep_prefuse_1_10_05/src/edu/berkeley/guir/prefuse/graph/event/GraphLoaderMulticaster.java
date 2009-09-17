package edu.berkeley.guir.prefuse.graph.event;

import java.util.EventListener;

import edu.berkeley.guir.prefuse.event.EventMulticaster;
import edu.berkeley.guir.prefuse.graph.Entity;
import edu.berkeley.guir.prefuse.graph.external.GraphLoader;

/**
 * Manages listeners for graph data loading and unloading events.
 * 
 * @author newbergr
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public class GraphLoaderMulticaster extends EventMulticaster 
    implements GraphLoaderListener
{

    /**
     * @see edu.berkeley.guir.prefuse.graph.event.GraphLoaderListener#entityLoaded(edu.berkeley.guir.prefuse.graph.external.GraphLoader, edu.berkeley.guir.prefuse.graph.Entity)
     */
	public void entityLoaded(GraphLoader loader, Entity e) {
	    ((GraphLoaderListener)a).entityLoaded(loader, e);
        ((GraphLoaderListener)b).entityLoaded(loader, e);
    } //
    
    /**
     * @see edu.berkeley.guir.prefuse.graph.event.GraphLoaderListener#entityUnloaded(edu.berkeley.guir.prefuse.graph.external.GraphLoader, edu.berkeley.guir.prefuse.graph.Entity)
     */
    public void entityUnloaded(GraphLoader loader, Entity e) {
        ((GraphLoaderListener)a).entityUnloaded(loader, e);
        ((GraphLoaderListener)b).entityUnloaded(loader, e);
    } //

	public static GraphLoaderListener add(GraphLoaderListener a, GraphLoaderListener b) {
		return (GraphLoaderListener) addInternal(a, b);
	} //

	public static GraphLoaderListener remove(GraphLoaderListener a, GraphLoaderListener b) {
		return (GraphLoaderListener) removeInternal(a, b);
	} //

	/** 
	 * Returns the resulting multicast listener from adding listener-a
	 * and listener-b together.  
	 * If listener-a is null, it returns listener-b;  
	 * If listener-b is null, it returns listener-a
	 * If neither are null, then it creates and returns
	 * a new AWTEventMulticaster instance which chains a with b.
	 * @param a event listener-a
	 * @param b event listener-b
	 */
	protected static EventListener addInternal(
		EventListener a,
		EventListener b) {
		if (a == null)
			return b;
		if (b == null)
			return a;
		return new GraphLoaderMulticaster(a, b);
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
	
	protected GraphLoaderMulticaster(EventListener a, EventListener b) {
		super(a,b);
	} //

} // end of class PrefuseEventMulticaster
