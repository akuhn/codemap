package edu.berkeley.guir.prefuse.event;

import java.util.EventListener;

import edu.berkeley.guir.prefuse.activity.Activity;

/**
 * Callback interface by which interested classes can be notified of
 * the progress of a scheduled activity.
 * 
 * Feb 9, 2004 - jheer - Created class
 *
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public interface ActivityListener extends EventListener {

    /**
     * Called when an activity has been scheduled with an ActivityManager
     * @param a the scheduled Activity
     */
    public void activityScheduled(Activity a);
    
    /**
     * Called when an activity is first started.
     * @param a the started Activity
     */
    public void activityStarted(Activity a);
    
    /**
     * Called when an activity is stepped.
     * @param a the stepped Activity
     */
    public void activityStepped(Activity a);
    
    /**
     * Called when an activity finishes.
     * @param a the finished Activity
     */
    public void activityFinished(Activity a);
    
    /**
     * Called when an activity is cancelled.
     * @param a the cancelled Activity
     */
    public void activityCancelled(Activity a);
    
} // end of interface ActivityListener
