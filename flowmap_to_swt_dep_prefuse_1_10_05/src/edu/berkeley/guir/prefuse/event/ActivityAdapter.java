package edu.berkeley.guir.prefuse.event;

import edu.berkeley.guir.prefuse.activity.Activity;

/**
 * Adapter class for ActivityListeners. Provides empty implementations of
 * ActivityListener routines.
 * 
 * Feb 9, 2004 - jheer - Created class
 *
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public class ActivityAdapter implements ActivityListener {

    /**
     * @see edu.berkeley.guir.prefuse.event.ActivityListener#activityScheduled(edu.berkeley.guir.prefuse.activity.Activity)
     */
    public void activityScheduled(Activity a) {
    } //

    /**
     * @see edu.berkeley.guir.prefuse.event.ActivityListener#activityStarted(edu.berkeley.guir.prefuse.activity.Activity)
     */
    public void activityStarted(Activity a) {
    } //

    /**
     * @see edu.berkeley.guir.prefuse.event.ActivityListener#activityStepped(edu.berkeley.guir.prefuse.activity.Activity)
     */
    public void activityStepped(Activity a) {
    } //

    /**
     * @see edu.berkeley.guir.prefuse.event.ActivityListener#activityFinished(edu.berkeley.guir.prefuse.activity.Activity)
     */
    public void activityFinished(Activity a) {
    } //

    /**
     * @see edu.berkeley.guir.prefuse.event.ActivityListener#activityCancelled(edu.berkeley.guir.prefuse.activity.Activity)
     */
    public void activityCancelled(Activity a) {
    } //

} // end of class ActivityAdapter
