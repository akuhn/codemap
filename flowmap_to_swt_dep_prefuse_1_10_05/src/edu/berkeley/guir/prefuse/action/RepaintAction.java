package edu.berkeley.guir.prefuse.action;

import edu.berkeley.guir.prefuse.ItemRegistry;

/**
 * Issues repaint requests to all displays tied to the given item registry.
 *
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public class RepaintAction extends AbstractAction {

    /**
     * Calls repaint on all displays associated with the given ItemRegistry.
     * @see edu.berkeley.guir.prefuse.action.Action#run(edu.berkeley.guir.prefuse.ItemRegistry, double)
     */
    public void run(ItemRegistry registry, double frac) {
        registry.repaint();
    } //

} // end of class RepaintAction
