package edu.berkeley.guir.prefusex.controls;

import java.awt.Cursor;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

import edu.berkeley.guir.prefuse.Display;
import edu.berkeley.guir.prefuse.FocusManager;
import edu.berkeley.guir.prefuse.ItemRegistry;
import edu.berkeley.guir.prefuse.NodeItem;
import edu.berkeley.guir.prefuse.VisualItem;
import edu.berkeley.guir.prefuse.activity.Activity;
import edu.berkeley.guir.prefuse.event.ControlAdapter;
import edu.berkeley.guir.prefuse.focus.FocusSet;

/**
 * Sets the current focus (according to the ItemRegistry's default focus
 * set) in response to mouse actions. This does not necessarily cause the
 * display to change. For this functionality, use a 
 * {@link edu.berkeley.guir.prefuse.event.FocusListener FocusListener} 
 * to drive display updates when the focus changes.
 *
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public class FocusControl extends ControlAdapter {

    private Object focusSetKey = FocusManager.DEFAULT_KEY;
    protected int ccount;
    protected Class[] itemTypes = new Class[] { NodeItem.class };
    protected Activity activity = null;
    
    /**
     * Creates a new FocusControl that changes the focus to another item
     * when that item is clicked once.
     */
    public FocusControl() {
        this(1);
    } //
    
    /**
     * Creates a new FocusControl that changes the focus to another item
     * when that item is clicked once.
     * @param act an activity run to upon focus change 
     */
    public FocusControl(Activity act) {
        this(1);
        activity = act;
    } //
    
    /**
     * Creates a new FocusControl that changes the focus when an item is 
     * clicked the specified number of times. A click value of zero indicates
     * that the focus should be changed in response to mouse-over events.
     * @param clicks the number of clicks needed to switch the focus.
     */
    public FocusControl(int clicks) {
        ccount = clicks;
    } //
    
    /**
     * Creates a new FocusControl that changes the focus when an item is 
     * clicked the specified number of times. A click value of zero indicates
     * that the focus should be changed in response to mouse-over events.
     * @param clicks the number of clicks needed to switch the focus.
     * @param act an activity run to upon focus change 
     */
    public FocusControl(int clicks, Activity act) {
        ccount = clicks;
        activity = act;
    } //
    
    /**
     * Creates a new FocusControl that changes the focus when an item is 
     * clicked the specified number of times. A click value of zero indicates
     * that the focus should be changed in response to mouse-over events.
     * @param clicks the number of clicks needed to switch the focus.
     * @param types the VisualItem classes that eligible for focus status
     */
    public FocusControl(int clicks, Class[] types) {
        ccount = clicks;
        setFocusItemTypes(types);
    } //
    
    /**
     * Creates a new FocusControl that changes the focus when an item is 
     * clicked the specified number of times. A click value of zero indicates
     * that the focus should be changed in response to mouse-over events.
     * @param clicks the number of clicks needed to switch the focus.
     * @param focusSetKey the key corresponding to the focus set to use
     */
    public FocusControl(int clicks, Object focusSetKey) {
        ccount = clicks;
        this.focusSetKey = focusSetKey;
    } //
    
    /**
     * Creates a new FocusControl that changes the focus when an item is 
     * clicked the specified number of times. A click value of zero indicates
     * that the focus should be changed in response to mouse-over events.
     * @param clicks the number of clicks needed to switch the focus.
     * @param focusSetKey the key corresponding to the focus set to use
     * @param types the VisualItem classes that eligible for focus status
     */
    public FocusControl(int clicks, Object focusSetKey, Class[] types) {
        ccount = clicks;
        this.focusSetKey = focusSetKey;
        setFocusItemTypes(types);
    } //
    
    public void setFocusItemTypes(Class[] types) {
        for ( int i=0; i<types.length; i++ ) {
            if ( !isVisualItem(types[i]) ) {
                throw new IllegalArgumentException("All types must be of type VisualItem");
            }
        }
        itemTypes = (Class[])types.clone();
    } //
    
    protected boolean isVisualItem(Class c) {
        while ( c != null && !VisualItem.class.equals(c) ) {
            c = c.getSuperclass();
        }
        return (c != null);
    } //
    
    protected boolean isAllowedType(VisualItem item) {
        for ( int i=0; i<itemTypes.length; i++ ) {
            if ( itemTypes[i].isInstance(item) ) {
                return true;
            }
        }
        return false;
    } //
    
    public void itemEntered(VisualItem item, MouseEvent e) {
        if ( isAllowedType(item) ) {
            Display d = (Display)e.getSource();
            d.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            if ( ccount == 0 ) {
                ItemRegistry registry = item.getItemRegistry();
                FocusManager fm = registry.getFocusManager();
                FocusSet fs = fm.getFocusSet(focusSetKey);
                fs.set(item.getEntity());
                registry.touch(item.getItemClass());
                runActivity();
            }
        }
    } //
    
    public void itemExited(VisualItem item, MouseEvent e) {
        if ( isAllowedType(item) ) {
            Display d = (Display)e.getSource();
            d.setCursor(Cursor.getDefaultCursor());
            if ( ccount == 0 ) {
                ItemRegistry registry = item.getItemRegistry();
                FocusManager fm = registry.getFocusManager();
                FocusSet fs = fm.getFocusSet(focusSetKey);
                fs.remove(item.getEntity());
                registry.touch(item.getItemClass());
                runActivity();
            }
        }
    } //
    
    public void itemClicked(VisualItem item, MouseEvent e) {
        if ( isAllowedType(item) && ccount > 0 && 
             SwingUtilities.isLeftMouseButton(e)    && 
             e.getClickCount() == ccount )
        {
            ItemRegistry registry = item.getItemRegistry();
            FocusManager fm = registry.getFocusManager();
            FocusSet fs = fm.getFocusSet(focusSetKey);
            fs.set(item.getEntity());
            registry.touch(item.getItemClass());
            runActivity();
        }
    } //
    
    private void runActivity() {
        if ( activity != null ) {
            activity.runNow();
        }
    } //
    
} // end of class FocusControl
