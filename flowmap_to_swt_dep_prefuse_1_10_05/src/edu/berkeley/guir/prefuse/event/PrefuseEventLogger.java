package edu.berkeley.guir.prefuse.event;

import java.awt.Container;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import edu.berkeley.guir.prefuse.Display;
import edu.berkeley.guir.prefuse.VisualItem;
import edu.berkeley.guir.prefuse.ItemRegistry;
import edu.berkeley.guir.prefuse.graph.Entity;

/**
 * A listener implementation that logs events from the prefuse architecture.
 * This can be useful for debugging as well as for logging events during user
 * studies.
 * 
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public class PrefuseEventLogger implements ControlListener, 
	ItemRegistryListener, FocusListener, ComponentListener {

	public static final int LOG_INTERFACE = 1;
	public static final int LOG_FOCUS     = 2;
	public static final int LOG_REGISTRY  = 4;
	
	public static final String ITEM_DRAGGED          = "ITEM-DRAGGED";
	public static final String ITEM_MOVED            = "ITEM-MOVED";
	public static final String ITEM_WHEEL_MOVED      = "ITEM-WHEEL-MOVED";
	public static final String ITEM_CLICKED          = "ITEM-CLICKED";
	public static final String ITEM_PRESSED          = "ITEM-PRESSED";
	public static final String ITEM_RELEASED         = "ITEM-RELEASED";
	public static final String ITEM_ENTERED          = "ITEM-ENTERED";
	public static final String ITEM_EXITED           = "ITEM-EXITED";
	public static final String ITEM_KEY_PRESSED      = "ITEM-KEY-PRESSED";
	public static final String ITEM_KEY_RELEASED     = "ITEM-KEY-RELEASED";
	public static final String ITEM_KEY_TYPED        = "ITEM-KEY-TYPED";
	public static final String MOUSE_ENTERED         = "MOUSE-ENTERED";
	public static final String MOUSE_EXITED          = "MOUSE-EXITED";
	public static final String MOUSE_PRESSED         = "MOUSE-PRESSED";
	public static final String MOUSE_RELEASED        = "MOUSE-RELEASED";
	public static final String MOUSE_CLICKED         = "MOUSE-CLICKED";
	public static final String MOUSE_DRAGGED         = "MOUSE-DRAGGED";
	public static final String MOUSE_MOVED           = "MOUSE-MOVED";
	public static final String MOUSE_WHEEL_MOVED     = "MOUSE-WHEEL-MOVED";
	public static final String KEY_PRESSED           = "KEY-PRESSED";
	public static final String KEY_RELEASED          = "KEY-RELEASED";
	public static final String KEY_TYPED             = "KEY-TYPED";
	public static final String FOCUS_CHANGED         = "FOCUS-CHANGED";
	public static final String REGISTRY_ITEM_ADDED   = "REGISTRY-ITEM-ADDED";
	public static final String REGISTRY_ITEM_REMOVED = "REGISTRY-ITEM-REMOVED";
	public static final String WINDOW_POSITION       = "WINDOW-POSITION";

	private ItemRegistry m_registry;
	private Display      m_display;
	private String       m_label;
	private boolean      m_logging;
	private int          m_state;
	
	private PrintStream  m_out;

	public PrefuseEventLogger(ItemRegistry registry, Display display, int state, String label) {
		m_registry = registry;
		m_display  = display;
		m_state    = state;
		m_label    = label;
		m_logging  = false;
	} //
	
	public PrefuseEventLogger(ItemRegistry registry, int state, String label) {
		this(registry, null, state, label);
	} //
	
	public PrefuseEventLogger(Display display, String label) {
		this(null, display, LOG_INTERFACE, label);
	} //
	
	public synchronized void start(String filename) throws FileNotFoundException {
		if ( m_logging )
			throw new IllegalStateException(
				"Can't start an already running logger!");
				
		m_out = new PrintStream(new BufferedOutputStream(
					new FileOutputStream(filename)));
		m_logging = true;
		if ( m_display != null && (m_state & LOG_INTERFACE) > 0 )
			m_display.addControlListener(this);
		if ( m_registry != null && (m_state & LOG_FOCUS) > 0 )
			m_registry.getDefaultFocusSet().addFocusListener(this);
		if ( m_registry != null && (m_state & LOG_REGISTRY) > 0 )
			m_registry.addItemRegistryListener(this);
		
		Container c = m_display.getParent();
		while ( c != null && !(c instanceof Window) )
			c = c.getParent();
		if ( c != null )
			c.addComponentListener(this);
		
		Point s = m_display.getLocationOnScreen();
		log(WINDOW_POSITION+"\t"+"("+s.x+","+s.y+")");
	} //

	public synchronized void stop() {
		if ( !m_logging ) return;
		
		if ( m_display != null && (m_state & LOG_INTERFACE) > 0 )
			m_display.removeControlListener(this);
		if ( m_registry != null && (m_state & LOG_FOCUS) > 0 )
			m_registry.getDefaultFocusSet().removeFocusListener(this);
		if ( m_registry != null && (m_state & LOG_REGISTRY) > 0 )
			m_registry.removeItemRegistryListener(this);		
		
		Container c = m_display.getParent();
		while ( c != null && !(c instanceof Window) )
			c = c.getParent();
		if ( c != null )
			c.removeComponentListener(this);
		
		m_out.flush();
		m_out.close();
		m_logging = false;
	} //
	
	public synchronized boolean isRunning() {
		return m_logging;
	} //
	
	public void log(String msg) {
		if ( !m_logging )
			throw new IllegalStateException("Logger isn't running!");
		m_out.println(System.currentTimeMillis() + "\t" + msg);
	} //
	
	public void log(long timestamp, String msg) {
		if ( !m_logging )
			throw new IllegalStateException("Logger isn't running!");
		m_out.println(timestamp + "\t" + msg);
	} //
	
	public void logMouseEvent(String msg, MouseEvent e) {
		msg = msg+"\t[("+e.getX()+","+e.getY()+"),button="+e.getButton()
		    +",clickCount="+e.getClickCount()+",modifiers="
			+MouseEvent.getMouseModifiersText(e.getModifiers())+"]";
		log(e.getWhen(), msg);
	} //
	
	public void logMouseWheelEvent(String msg, MouseWheelEvent e) {
		msg = msg+"\t[("+e.getX()+","+e.getY()+"),button="+e.getButton()
		    +",clickCount="+e.getClickCount()+",modifiers="
			+MouseEvent.getMouseModifiersText(e.getModifiers())
			+",wheelRotation="+e.getWheelRotation()+"]";
		log(e.getWhen(), msg);
	} //
	
	public void logKeyEvent(String msg, KeyEvent e) {
		msg = msg+"\t[keyCode="+e.getKeyCode()+","+"keyChar="+e.getKeyChar()
			+",keyText="+KeyEvent.getKeyText(e.getKeyCode())+",modifiers="
			+KeyEvent.getKeyModifiersText(e.getModifiers())+"]";
		log(e.getWhen(), msg);
	} //
    
    public void logFocusEvent(FocusEvent e) {
        Entity[] list = null;
        int i;
        StringBuffer sbuf = new StringBuffer(FOCUS_CHANGED);
        sbuf.append("\t[");
        for ( list=e.getAddedFoci(), i=0; i<list.length; i++ ) {
            if ( i > 0 ) sbuf.append(",");
            sbuf.append(getEntityString(list[i]));
        }
        sbuf.append("]\t[");
        for ( list=e.getRemovedFoci(), i=0; i<list.length; i++ ) {
            if ( i > 0 ) sbuf.append(",");
            sbuf.append(getEntityString(list[i]));
        }
        sbuf.append("]");
        log(e.getWhen(), sbuf.toString());
    } //
	
    protected String getEntityString(Entity entity) {
        return (entity == null ? "NULL" : entity.getAttribute(m_label));
    } //
    
	protected String getItemString(VisualItem item) {
		return (item == null ? "NULL" : getEntityString(item.getEntity()));
	} //

	// ========================================================================
	// == WINDOW MONITORING====================================================

	public void componentHidden(ComponentEvent e) {} //
	public void componentMoved(ComponentEvent e) {
		Point s = m_display.getLocationOnScreen();
		log(WINDOW_POSITION+"\t"+"("+s.x+","+s.y+")");
	} //
	public void componentResized(ComponentEvent e) {} //
	public void componentShown(ComponentEvent e) {} //

	// ========================================================================
	// == CALLBACKS ===========================================================

	/**
	 * @see edu.berkeley.guir.prefuse.event.ControlListener#itemDragged(edu.berkeley.guir.prefuse.VisualItem, java.awt.event.MouseEvent)
	 */
	public void itemDragged(VisualItem item, MouseEvent e) {
		logMouseEvent(ITEM_DRAGGED+"\t"+getItemString(item), e);
	} //

	/**
	 * @see edu.berkeley.guir.prefuse.event.ControlListener#itemMoved(edu.berkeley.guir.prefuse.VisualItem, java.awt.event.MouseEvent)
	 */
	public void itemMoved(VisualItem item, MouseEvent e) {
		logMouseEvent(ITEM_MOVED+"\t"+getItemString(item), e);
	} //

	/**
	 * @see edu.berkeley.guir.prefuse.event.ControlListener#itemWheelMoved(edu.berkeley.guir.prefuse.VisualItem, java.awt.event.MouseWheelEvent)
	 */
	public void itemWheelMoved(VisualItem item, MouseWheelEvent e) {
		logMouseWheelEvent(ITEM_WHEEL_MOVED+"\t"+getItemString(item),e);
	} //

	/**
	 * @see edu.berkeley.guir.prefuse.event.ControlListener#itemClicked(edu.berkeley.guir.prefuse.VisualItem, java.awt.event.MouseEvent)
	 */
	public void itemClicked(VisualItem item, MouseEvent e) {
		logMouseEvent(ITEM_CLICKED+"\t"+getItemString(item), e);
	} //

	/**
	 * @see edu.berkeley.guir.prefuse.event.ControlListener#itemPressed(edu.berkeley.guir.prefuse.VisualItem, java.awt.event.MouseEvent)
	 */
	public void itemPressed(VisualItem item, MouseEvent e) {
		logMouseEvent(ITEM_PRESSED+"\t"+getItemString(item), e);
	} //

	/**
	 * @see edu.berkeley.guir.prefuse.event.ControlListener#itemReleased(edu.berkeley.guir.prefuse.VisualItem, java.awt.event.MouseEvent)
	 */
	public void itemReleased(VisualItem item, MouseEvent e) {
		logMouseEvent(ITEM_RELEASED+"\t"+getItemString(item), e);
	} //

	/**
	 * @see edu.berkeley.guir.prefuse.event.ControlListener#itemEntered(edu.berkeley.guir.prefuse.VisualItem, java.awt.event.MouseEvent)
	 */
	public void itemEntered(VisualItem item, MouseEvent e) {
		logMouseEvent(ITEM_ENTERED+"\t"+getItemString(item), e);
	} //

	/**
	 * @see edu.berkeley.guir.prefuse.event.ControlListener#itemExited(edu.berkeley.guir.prefuse.VisualItem, java.awt.event.MouseEvent)
	 */
	public void itemExited(VisualItem item, MouseEvent e) {
		logMouseEvent(ITEM_EXITED+"\t"+getItemString(item), e);
	}

	/**
	 * @see edu.berkeley.guir.prefuse.event.ControlListener#itemKeyPressed(edu.berkeley.guir.prefuse.VisualItem, java.awt.event.KeyEvent)
	 */
	public void itemKeyPressed(VisualItem item, KeyEvent e) {
		logKeyEvent(ITEM_KEY_PRESSED+"\t"+getItemString(item), e);
	} //

	/**
	 * @see edu.berkeley.guir.prefuse.event.ControlListener#itemKeyReleased(edu.berkeley.guir.prefuse.VisualItem, java.awt.event.KeyEvent)
	 */
	public void itemKeyReleased(VisualItem item, KeyEvent e) {
		logKeyEvent(ITEM_KEY_RELEASED+"\t"+getItemString(item), e);
	} //

	/**
	 * @see edu.berkeley.guir.prefuse.event.ControlListener#itemKeyTyped(edu.berkeley.guir.prefuse.VisualItem, java.awt.event.KeyEvent)
	 */
	public void itemKeyTyped(VisualItem item, KeyEvent e) {
		logKeyEvent(ITEM_KEY_TYPED+"\t"+getItemString(item), e);
	} //

	/**
	 * @see edu.berkeley.guir.prefuse.event.ControlListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	public void mouseEntered(MouseEvent e) {
		logMouseEvent(MOUSE_ENTERED, e);
	} //

	/**
	 * @see edu.berkeley.guir.prefuse.event.ControlListener#mouseExited(java.awt.event.MouseEvent)
	 */
	public void mouseExited(MouseEvent e) {
		logMouseEvent(MOUSE_EXITED, e);
	} //

	/**
	 * @see edu.berkeley.guir.prefuse.event.ControlListener#mousePressed(java.awt.event.MouseEvent)
	 */
	public void mousePressed(MouseEvent e) {
		logMouseEvent(MOUSE_PRESSED, e);
	} //

	/**
	 * @see edu.berkeley.guir.prefuse.event.ControlListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	public void mouseReleased(MouseEvent e) {
		logMouseEvent(MOUSE_RELEASED, e);
	} //

	/**
	 * @see edu.berkeley.guir.prefuse.event.ControlListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	public void mouseClicked(MouseEvent e) {
		logMouseEvent(MOUSE_CLICKED, e);
	} //

	/**
	 * @see edu.berkeley.guir.prefuse.event.ControlListener#mouseDragged(java.awt.event.MouseEvent)
	 */
	public void mouseDragged(MouseEvent e) {
		logMouseEvent(MOUSE_DRAGGED, e);
	} //

	/**
	 * @see edu.berkeley.guir.prefuse.event.ControlListener#mouseMoved(java.awt.event.MouseEvent)
	 */
	public void mouseMoved(MouseEvent e) {
		logMouseEvent(MOUSE_MOVED, e);
	} //

	/**
	 * @see edu.berkeley.guir.prefuse.event.ControlListener#mouseWheelMoved(java.awt.event.MouseWheelEvent)
	 */
	public void mouseWheelMoved(MouseWheelEvent e) {
		logMouseWheelEvent(ITEM_WHEEL_MOVED, e);
	} //

	/**
	 * @see edu.berkeley.guir.prefuse.event.ControlListener#keyPressed(java.awt.event.KeyEvent)
	 */
	public void keyPressed(KeyEvent e) {
		logKeyEvent(KEY_PRESSED, e);
	} //

	/**
	 * @see edu.berkeley.guir.prefuse.event.ControlListener#keyReleased(java.awt.event.KeyEvent)
	 */
	public void keyReleased(KeyEvent e) {
		logKeyEvent(KEY_RELEASED, e);
	} //

	/**
	 * @see edu.berkeley.guir.prefuse.event.ControlListener#keyTyped(java.awt.event.KeyEvent)
	 */
	public void keyTyped(KeyEvent e) {
		logKeyEvent(KEY_TYPED, e);
	} //

	/**
	 * @see edu.berkeley.guir.prefuse.event.ItemRegistryListener#registryItemAdded(edu.berkeley.guir.prefuse.VisualItem)
	 */
	public void registryItemAdded(VisualItem item) {
		log(REGISTRY_ITEM_ADDED+"\t"+item.getItemClass()
			+"\t"+item.getAttribute(m_label));
	} //

	/**
	 * @see edu.berkeley.guir.prefuse.event.ItemRegistryListener#registryItemRemoved(edu.berkeley.guir.prefuse.VisualItem)
	 */
	public void registryItemRemoved(VisualItem item) {
		log(REGISTRY_ITEM_REMOVED+"\t"+item.getItemClass()
			+"\t"+item.getAttribute(m_label));
	} //

    /**
     * @see edu.berkeley.guir.prefuse.event.FocusListener#focusChanged(edu.berkeley.guir.prefuse.event.FocusEvent)
     */
    public void focusChanged(FocusEvent e) {
        logFocusEvent(e);
    } //

} // end of class PrefuseEventLogger
