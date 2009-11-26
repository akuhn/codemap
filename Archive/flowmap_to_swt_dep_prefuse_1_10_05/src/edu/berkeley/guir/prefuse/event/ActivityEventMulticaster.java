package edu.berkeley.guir.prefuse.event;

import java.util.EventListener;

import edu.berkeley.guir.prefuse.activity.Activity;

/**
 * Multicaster for ActivityListener calls.
 * 
 * Feb 16, 2004 - jheer - Created class
 *
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public class ActivityEventMulticaster extends EventMulticaster
    implements ActivityListener
{
    public static ActivityListener add(ActivityListener a, ActivityListener b) {
        return (ActivityListener) addInternal(a, b);
    } //

    public static ActivityListener remove(
            ActivityListener l,
            ActivityListener oldl) {
        return (ActivityListener) removeInternal(l, oldl);
    } //
    
    public void activityScheduled(Activity ac) {
        ((ActivityListener) a).activityScheduled(ac);
        ((ActivityListener) b).activityScheduled(ac);
    } //

    public void activityStarted(Activity ac) {
        ((ActivityListener) a).activityStarted(ac);
        ((ActivityListener) b).activityStarted(ac);
    } //

    public void activityStepped(Activity ac) {
        ((ActivityListener) a).activityStepped(ac);
        ((ActivityListener) b).activityStepped(ac);
    } //

    public void activityFinished(Activity ac) {
        ((ActivityListener) a).activityFinished(ac);
        ((ActivityListener) b).activityFinished(ac);
    } //

    public void activityCancelled(Activity ac) {
        ((ActivityListener) a).activityCancelled(ac);
        ((ActivityListener) b).activityCancelled(ac);
    } //

    protected static EventListener addInternal(
            EventListener a, EventListener b)
    {
        if (a == null)
            return b;
        if (b == null)
            return a;
        return new ActivityEventMulticaster(a, b);
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
    
    protected ActivityEventMulticaster(EventListener a, EventListener b) {
        super(a,b);
    } //

} // end of class ActivityEventMulticaster
