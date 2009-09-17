package edu.berkeley.guir.prefuse.action;

import edu.berkeley.guir.prefuse.ItemRegistry;

/**
 * Interface which every graph processing action must provide. 
 *
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public interface Action {
    
    /**
     * Runs this action, performing desired graph processing
     * @param registry the ItemRegistry housing the VisualItems to process
     * @param frac a fraction indicating the current progress of this
     *  action if it persists over time. For actions that are only run once,
     *  a value of 0 is used.
     */
    public void run(ItemRegistry registry, double frac);
    
    /**
     * Indicates if this action is currently enabled.
     * @return true if this action is enabled, false otherwise.
     */
    public boolean isEnabled();
    
    /**
     * Determines if this Action is enabled or disabled.
     * @param s true to enable the Action, false to disable it.
     */
    public void setEnabled(boolean s);

} // end of interface Action
