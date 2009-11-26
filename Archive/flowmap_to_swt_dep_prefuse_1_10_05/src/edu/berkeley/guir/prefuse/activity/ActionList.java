package edu.berkeley.guir.prefuse.activity;

import java.util.ArrayList;
import java.util.Iterator;

import edu.berkeley.guir.prefuse.ItemRegistry;
import edu.berkeley.guir.prefuse.action.*;

/**
 * <p>The ActionList represents a chain of Actions that perform graph
 * processing. These lists can then be submitted to the ActivityManager
 * to be executed. In addition, ActionList also implements the Action
 * interface, so ActionLists can be placed within other ActionLists,
 * allowing recursive composition of different sets of Actions.</p>
 * 
 * <p>As a subclass of Activity, ActionLists can be of two kinds. 
 * <i>Run-once</i> action lists have
 * a duration value of zero, and simply run once when scheduled. Action lists
 * with a duration greater than zero can be executed multiple times, waiting
 * a specified step time between each execution until the activity has run for
 * its full duration.</p>
 *
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 * @see Activity
 * @see edu.berkeley.guir.prefuse.action.Action
 */
public class ActionList extends Activity implements Action {

    private ItemRegistry   m_registry;
    private ArrayList      m_actions = new ArrayList();
    
    /**
     * Creates a new run-once ActionList that operates on the 
     * given ItemRegistry.
     * @param registry the ItemRegistry that Actions should process
     */
    public ActionList(ItemRegistry registry) {
        this(registry, 0);
    }
    
    /**
     * Creates a new ActionList of specified duration and default
     * step time of 20 milliseconds.
     * @param registry the ItemRegistry that Actions should process
     * @param duration the duration of this Activity, in milliseconds
     */
    public ActionList(ItemRegistry registry, long duration) {
        this(registry, duration, Activity.DEFAULT_STEP_TIME);
    } //
    
    /**
     * Creates a new ActionList of specified duration and step time.
     * @param registry the ItemRegistry that Actions should process
     * @param duration the duration of this Activity, in milliseconds
     * @param stepTime the time to wait in milliseconds between executions
     *  of the action list
     */
    public ActionList(ItemRegistry registry, long duration, long stepTime) {
        this(registry, duration, stepTime, System.currentTimeMillis());
    } //
    
    /**
     * Creates a new ActionList of specified duration, step time, and
     * staring time.
     * @param registry the ItemRegistry that Actions should process
     * @param duration the duration of this Activity, in milliseconds
     * @param stepTime the time to wait in milliseconds between executions
     *  of the action list
     * @param startTime the scheduled start time for this Activity, if it is
     *  later scheduled.
     */
    public ActionList(ItemRegistry registry, long duration, 
            long stepTime, long startTime)
    {
        super(duration, stepTime, startTime);
        m_registry = registry;
    } //
    
    /**
     * Returns the number of Actions in the action list.
     * @return the size of this action list
     */
    public synchronized int size() {
        return m_actions.size();
    } //
    
    /**
     * Adds an Action to the end of the action list
     * @param a the Action instance to add
     */
    public synchronized void add(Action a) {
        m_actions.add(a);
    } //
    
    /**
     * Adds an Action to the action list at the given index
     * @param i the index at which to add the Action
     * @param a the Action instance to add
     */
    public synchronized void add(int i, Action a) {
        m_actions.add(i, a);
    } //
    
    /**
     * Returns the Action at the specified index in the action list
     * @param i the index
     * @return the requested Action
     */
    public synchronized Action get(int i) {
        return (Action)m_actions.get(i);
    } //
    
    /**
     * Removes a given Action from the action list
     * @param a the Action to remove
     * @return true if the Action was found and removed, false otherwise
     */
    public synchronized boolean remove(Action a) {
        return m_actions.remove(a);
    } //
    
    /**
     * Removes the Action at the specified index in the action list
     * @param i the index
     * @return the removed Action
     */
    public synchronized Action remove(int i) {
        return (Action)m_actions.remove(i);
    } //
    
    /**
     * Runs this ActionList (as an Activity)
     * @see edu.berkeley.guir.prefuse.activity.Activity#run(long)
     */
    protected synchronized void run(long elapsedTime) {
        run(m_registry, getPace(elapsedTime));
    } //
    
    /**
     * Runs this ActionList.
     * @see edu.berkeley.guir.prefuse.action.Action#run(edu.berkeley.guir.prefuse.ItemRegistry, double)
     */
    public void run(ItemRegistry registry, double frac) {
        synchronized ( m_registry ) {
            Iterator iter = m_actions.iterator();
            while ( iter.hasNext() ) {
                Action a = (Action)iter.next();
                if ( a.isEnabled() ) a.run(m_registry, frac);
            }
        }
    } //

} // end of class Action
