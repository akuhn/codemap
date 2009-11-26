package edu.berkeley.guir.prefuse.graph.event;

import java.util.EventListener;

import edu.berkeley.guir.prefuse.graph.Entity;
import edu.berkeley.guir.prefuse.graph.external.GraphLoader;

/**
 * Listener interface for monitoring the loading and unloading of graph
 *  data from an external backing store.
 *
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public interface GraphLoaderListener extends EventListener {

    /**
     * Indicates an Entity has been loaded from an external store.
     * @param loader the responsible GraphLoader
     * @param e the loaded Entity
     */
    public void entityLoaded(GraphLoader loader, Entity e);
    
    /**
     * Notifies listener that a loadeded Entity has been unloaded and
     *  so is no longer available in memory.
     * @param loader the responsible GraphLoader
     * @param e the unloaded Entity
     */
    public void entityUnloaded(GraphLoader loader, Entity e);
    
} // end of interface GraphLoaderListener
