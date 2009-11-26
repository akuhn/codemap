package edu.berkeley.guir.prefuse.action;

import java.util.ArrayList;

import edu.berkeley.guir.prefuse.ItemRegistry;

/**
 * The ActionSwitch multiplexes between a set of Actions, allowing only one
 * of a group of actions to be executed at a given time.
 *
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public class ActionSwitch extends AbstractAction {

    private ArrayList actions;
    private int switchVal;
    
    /**
     * Creates an empty action switch.
     */
    public ActionSwitch() {
        actions = new ArrayList();
        switchVal = 0;
    } //
    
    /**
     * Creates a new ActionSwitch with the given actions and switch value.
     * @param acts the Actions to include in this switch
     * @param switchVal the switch value indicating which Action to run
     */
    public ActionSwitch(Action[] acts, int switchVal) {
        this();
        for ( int i=0; i<acts.length; i++ )
            actions.add(acts[i]);
        setSwitchValue(switchVal);
    } //
    
    public void run(ItemRegistry registry, double frac) {
        if ( actions.size() >0 ) {
            get(switchVal).run(registry,frac);
        }
    } //
    
    /**
     * Get the Action at the given index.
     * @param i the index of the Action
     * @return the requested Action
     */
    public Action get(int i) {
        return (Action)actions.get(i);
    } //
    
    /**
     * Add an Action to the switch, it is given the highest index.
     * @param a the Action to add
     */
    public void add(Action a) {
        actions.add(a);
    } //
    
    /**
     * Add an Action at the given index. Actions with higher indices
     * are pushed over by one.
     * @param i the index of the Action
     * @param a the Action to add
     */
    public void add(int i, Action a) {
        actions.add(i,a);
    } //
    
    /**
     * Sets the Action at the given index, overriding the Action
     * previously at that value.
     * @param i the index of the Action
     * @param a the Action to set
     */
    public void set(int i, Action a) {
        actions.set(i,a);
    } //
    
    /**
     * Removes the Action at the given index, Actions at higher indices
     * are shifted down by one.
     * @param i the index of the Action
     * @return the removed Action
     */
    public Action remove(int i) {
        return (Action)actions.remove(i);
    } //
    
    /**
     * Indicates how many Actions are in this switch
     * @return the number of Actions in this switch
     */
    public int size() {
        return actions.size();
    } //
    
    /**
     * Returns the current switch value, indicating the index of the Action
     * that will be executed in reponse to run() invocations.
     * @return the switch value
     */
    public int getSwitchValue() {
        return switchVal;
    } //
    
    /**
     * Set the switch value. This is the index of the Action that will be
     * executed in response to run() invocations.
     * @param s the new switch value
     */
    public void setSwitchValue(int s) {
        if ( s < 0 || s >= actions.size() )
            throw new IllegalArgumentException("Switch value out of legal range");
        switchVal = s;
    } //

} // end of class ActionSwitch
