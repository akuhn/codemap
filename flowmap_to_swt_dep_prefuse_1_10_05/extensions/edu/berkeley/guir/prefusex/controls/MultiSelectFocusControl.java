/*
 * Created on Aug 11, 2004
 */
package edu.berkeley.guir.prefusex.controls;

import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

import edu.berkeley.guir.prefuse.FocusManager;
import edu.berkeley.guir.prefuse.ItemRegistry;
import edu.berkeley.guir.prefuse.NodeItem;
import edu.berkeley.guir.prefuse.VisualItem;
import edu.berkeley.guir.prefuse.event.ControlAdapter;
import edu.berkeley.guir.prefuse.focus.DefaultFocusSet;
import edu.berkeley.guir.prefuse.focus.FocusSet;
import edu.berkeley.guir.prefuse.graph.Entity;

/**
 * Manages a focus set of selected items, adding or removing items from
 * the selection set in response to shift-clicks.
 * 
 * @author Jack Li jack(AT)cs_D0Tberkeley_D0Tedu
 */
public class MultiSelectFocusControl extends ControlAdapter {

    private static final Object DEFAULT_FOCUS_KEY = FocusManager.SELECTION_KEY;
	private final ItemRegistry registry; // needed for clearing focusSet on mouseClicked
    private final Object focusKey;
	
	/**
	 * Constructor. Uses the FocusManager.SELECTION_KEY as the key for the
	 * selection focus set.
	 * @param registry ItemRegistry monitoring the focus set
	 */
    public MultiSelectFocusControl(final ItemRegistry registry) {
        this(registry, DEFAULT_FOCUS_KEY);
    } //
    
    /**
	 * Constructor. Uses the given focusKey as the key for the
	 * selection focus set.
	 * @param registry ItemRegistry monitoring the focus set
	 * @param focusKey the key object to use to access the selection FocusSet
	 */
    public MultiSelectFocusControl(final ItemRegistry registry, Object focusKey) {
    	this.registry = registry;
    	this.focusKey = focusKey;
    	registry.getFocusManager().putFocusSet(focusKey, new DefaultFocusSet());
    } //
    
    /**
     * Shift click adds the item to the focus set if not added;
     * else it removes the item
     */
    public void itemClicked(VisualItem item, MouseEvent e) {
        if ( item instanceof NodeItem && 
             SwingUtilities.isLeftMouseButton(e))
        {
        	final FocusManager focusManager = registry.getFocusManager();
            final FocusSet focusSet = focusManager.getFocusSet(focusKey);
            final Entity node = item.getEntity();
            
            if (e.isShiftDown()) { // mode: adding to/removing from focus set
				if (focusSet.contains(node)) {
					focusSet.remove(node);
				} else {
					focusSet.add(node);
				}
			} else { // mode: doing something cool/resetting focus
				if (!focusManager.isFocus(focusKey, node)) {
					focusSet.set(node);
				}
			}
            registry.touch(item.getItemClass());
        }
    } //

    /**
     * Clear the focus
     */
	public void mouseClicked(MouseEvent e) {
		registry.getFocusManager().getFocusSet(focusKey).clear();
	} //
	
} // end of class MultiSelectFocusControl
