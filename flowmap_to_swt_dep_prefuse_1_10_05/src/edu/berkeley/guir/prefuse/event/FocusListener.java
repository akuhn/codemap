package edu.berkeley.guir.prefuse.event;

import java.util.EventListener;

/**
 * Inteface for classes to monitor changes to the focus status
 * of graph elements.
 * 
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public interface FocusListener extends EventListener {
    
    public void focusChanged(FocusEvent e);

} // end of interface FocusListener
