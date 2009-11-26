package edu.berkeley.guir.prefuse.activity;

import java.util.ArrayList;

import edu.berkeley.guir.prefuse.event.ActivityAdapter;

/**
 * <p>The ActivityManager is responsible for scheduling and running timed 
 * activities that perform graph processing and animation.</p>
 * 
 * <p>The AcivityManager runs in its own separate thread of execution, and
 * one instance is used to schedule activities from any number of currently
 * active visualizations. The class is implemented as a singleton; the single
 * instance of this class is interacted with through static methods. These
 * methods are called by an Activity's run methods, and so are made only
 * package visible here.</p>
 * 
 * <p>Activity instances can be scheduled by using their 
 * {@link edu.berkeley.guir.prefuse.activity.Activity#run() run()}, 
 * {@link edu.berkeley.guir.prefuse.activity.Activity#runNow() runNow()},
 * {@link edu.berkeley.guir.prefuse.activity.Activity#runAt(long) runAt()}, and 
 * {@link edu.berkeley.guir.prefuse.activity.Activity#runAfter(Activity) 
 * runAfter()} methods. These will automatically call the
 * appropriate methods with the ActivityManager.</p>
 *
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 * @see Activity
 * @see ActionList
 */
public class ActivityManager extends Thread {

    private static ActivityManager s_instance;
    
    private ArrayList m_activities;
    private ArrayList m_tmp;
    private long      m_nextTime;
    
    /**
     * Returns the active ActivityManager instance.
     * @return the ActivityManager
     */
    private static ActivityManager getInstance() {
        if ( s_instance == null || !s_instance.isAlive() ) {
            s_instance = new ActivityManager();
        }
        return s_instance;
    } //
    
    /**
     * Create a new ActivityManger.
     */
    private ActivityManager() {
        m_activities = new ArrayList();
        m_tmp = new ArrayList();
        m_nextTime = Long.MAX_VALUE;
        this.start();
    } //
    
    /**
     * Schedules an Activity with the manager.
     * @param a the Activity to schedule
     */
    static void schedule(Activity a) {
        getInstance()._schedule(a);
    } //
    
    /**
     * Schedules an Activity to start immediately, overwriting the
     * Activity's currently set startTime.
     * @param a the Activity to schedule
     */
    static void scheduleNow(Activity a) {
        getInstance()._scheduleNow(a);
    } //
    
    /**
     * Schedules an Activity at the specified startTime, overwriting the
     * Activity's currently set startTime.
     * @param a the Activity to schedule
     * @param startTime the time at which the activity should run
     */
    static void scheduleAt(Activity a, long startTime) {
        getInstance()._scheduleAt(a,startTime);
    } //
    
    /**
     * Schedules an Activity to start immediately after another Activity.
     * The second Activity will be scheduled to start immediately after the
     * first one finishes, overwriting any previously set startTime. If the
     * first Activity is cancelled, the second one will not run.
     * 
     * This functionality is provided by using an ActivityListener to monitor
     * the first Activity. The listener is removed upon completion or
     * cancellation of the first Activity.
     * 
     * This method does not effect the scheduling of the first Activity.
     * @param before the first Activity to run
     * @param after the Activity to run immediately after the first
     */
    static void scheduleAfter(Activity before, Activity after) {
        getInstance()._scheduleAfter(before, after);
    } //
    
    /**
     * Schedules an Activity to start immediately after another Activity.
     * The second Activity will be scheduled to start immediately after the
     * first one finishes, overwriting any previously set startTime. If the
     * first Activity is cancelled, the second one will not run.
     * 
     * This functionality is provided by using an ActivityListener to monitor
     * the first Activity. The listener will persist across mulitple runs,
     * meaning the second Activity will always be evoked upon a successful
     * finish of the first.
     * 
     * This method does not otherwise effect the scheduling of the first Activity.
     * @param before the first Activity to run
     * @param after the Activity to run immediately after the first
     */
    static void alwaysScheduleAfter(Activity before, Activity after) {
        getInstance()._alwaysScheduleAfter(before, after);
    } //
    
    /**
     * Removes an Activity from this manager, called by an
     * Activity when it finishes or is cancelled. Application 
     * code should not call this method! Instead, use 
     * Activity.cancel() to stop a sheduled or running Activity.
     * @param a
     * @return true if the activity was found and removed, false
     *  if the activity is not scheduled with this manager.
     */
    static void removeActivity(Activity a) {
        getInstance()._removeActivity(a);
    } //
    
    /**
     * Returns the number of scheduled activities
     * @return the number of scheduled activities
     */
    public static int activityCount() {
        return getInstance()._activityCount();
    } //
    
    /**
     * Schedules an Activity with the manager.
     * @param a the Activity to schedule
     */
    private synchronized void _schedule(Activity a) {
        if ( a.isScheduled() ) return; // already scheduled, do nothing
        m_activities.add(a);
        a.setScheduled(true);
        long start = a.getStartTime();
        if ( start < m_nextTime ) { 
           m_nextTime = start;
           notify();
        }
    } //
    
    /**
     * Schedules an Activity at the specified startTime, overwriting the
     * Activity's currently set startTime.
     * @param a the Activity to schedule
     * @param startTime the time at which the activity should run
     */
    private synchronized void _scheduleAt(Activity a, long startTime) {
        if ( a.isScheduled() ) return; // already scheduled, do nothing
        a.setStartTime(startTime);
        schedule(a);
    } //
    
    /**
     * Schedules an Activity to start immediately, overwriting the
     * Activity's currently set startTime.
     * @param a the Activity to schedule
     */    
    private synchronized void _scheduleNow(Activity a) {
        if ( a.isScheduled() ) return; // already scheduled, do nothing
        a.setStartTime(System.currentTimeMillis());
        schedule(a);
    } //
    
    /**
     * Schedules an Activity to start immediately after another Activity.
     * The second Activity will be scheduled to start immediately after the
     * first one finishes, overwriting any previously set startTime. If the
     * first Activity is cancelled, the second one will not run.
     * 
     * This functionality is provided by using an ActivityListener to monitor
     * the first Activity. The listener is removed upon completion or
     * cancellation of the first Activity.
     * 
     * This method does not effect the scheduling of the first Activity.
     * @param before the first Activity to run
     * @param after the Activity to run immediately after the first
     */
    private synchronized void _scheduleAfter(Activity before, Activity after) {
        before.addActivityListener(new ScheduleAfterActivity(after,true));
    } //
    
    /**
     * Schedules an Activity to start immediately after another Activity.
     * The second Activity will be scheduled to start immediately after the
     * first one finishes, overwriting any previously set startTime. If the
     * first Activity is cancelled, the second one will not run.
     * 
     * This functionality is provided by using an ActivityListener to monitor
     * the first Activity. The listener will persist across mulitple runs,
     * meaning the second Activity will always be evoked upon a successful
     * finish of the first.
     * 
     * This method does not otherwise effect the scheduling of the first Activity.
     * @param before the first Activity to run
     * @param after the Activity to run immediately after the first
     */
    private synchronized void _alwaysScheduleAfter(Activity before, Activity after) {
        before.addActivityListener(new ScheduleAfterActivity(after,false));
    } //
    
    /**
     * Removes an Activity from this manager, called by an
     * Activity when it finishes or is cancelled. Application 
     * code should not call this method! Instead, use 
     * Activity.cancel() to stop a sheduled or running Activity.
     * @param a
     * @return true if the activity was found and removed, false
     *  if the activity is not scheduled with this manager.
     */
    private synchronized boolean _removeActivity(Activity a) {
        boolean r = m_activities.remove(a);
        if ( r ) {
            a.setScheduled(false);
            if ( m_activities.size() == 0 ) {
                m_nextTime = Long.MAX_VALUE;
            }
        }
        return r;
    } //
    
    /**
     * Returns the number of scheduled activities
     * @return the number of scheduled activities
     */
    private synchronized int _activityCount() {
        return m_activities.size();
    } //
    
    /**
     * Main scheduling thread loop. This is automatically started upon
     * initialization of the ActivityManager.
     */
    public void run() {
        while ( true ) {
            if ( activityCount() > 0 ) {
                long currentTime = System.currentTimeMillis();
                long t = -1;
                
                synchronized (this) {
                    // copy content of activities, as new activities might
                    // be added while we process the current ones
                    m_tmp.addAll(m_activities);
                }
                
                for ( int i=0; i<m_tmp.size(); i++ ) {
                    // run the activity - the activity will check for
                    // itself if it should perform any action or not
                    Activity a = (Activity)m_tmp.get(i);
                    t = Math.max(t, a.runActivity(currentTime));
                }

                // clear the temporary list
                m_tmp.clear();
                
                if ( t == -1 ) continue;
                
                // determine the next time we should run
                synchronized (this) {
                    try {
                        wait(t);
                    } catch (InterruptedException e) { }
                }
            } else {
                // nothing to do, chill out until notified
                try {
                    synchronized (this) { wait(); }
                } catch (InterruptedException e) { }
            }
        }
    } //
    
    public class ScheduleAfterActivity extends ActivityAdapter {
        Activity after;
        boolean remove;
        public ScheduleAfterActivity(Activity after, boolean remove) {
            this.after = after;
            this.remove = remove;
        } //
        public void activityFinished(Activity a) {
            if ( remove ) a.removeActivityListener(this);
            scheduleNow(after);
        } //
        public void activityCancelled(Activity a) {
            if ( remove ) a.removeActivityListener(this);
        } //
    } // end of inner class ScheduleAfterActivity
    
} // end of class ActivityManager
