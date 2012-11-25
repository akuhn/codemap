package edu.berkeley.guir.prefuse.event;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.EventListener;

import edu.berkeley.guir.prefuse.VisualItem;

/**
 * Manages a list of listeners for prefuse control events.
 * 
 * @author newbergr
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public class ControlEventMulticaster extends EventMulticaster
    implements ControlListener
{
	public static ControlListener add(ControlListener a, ControlListener b) {
		return (ControlListener) addInternal(a, b);
	} //

	public static ControlListener remove(
		ControlListener l,
		ControlListener oldl) {
		return (ControlListener) removeInternal(l, oldl);
	} //

	public void itemDragged(VisualItem item, MouseEvent e) {
		((ControlListener) a).itemDragged(item, e);
		((ControlListener) b).itemDragged(item, e);
	} //

	public void itemMoved(VisualItem item, MouseEvent e) {
		((ControlListener) a).itemMoved(item, e);
		((ControlListener) b).itemMoved(item, e);
	} //

	public void itemWheelMoved(VisualItem item, MouseWheelEvent e) {
		((ControlListener) a).itemWheelMoved(item, e);
		((ControlListener) b).itemWheelMoved(item, e);
	} //

	public void itemClicked(VisualItem item, MouseEvent e) {
		((ControlListener) a).itemClicked(item, e);
		((ControlListener) b).itemClicked(item, e);
	} //

	public void itemPressed(VisualItem item, MouseEvent e) {
		((ControlListener) a).itemPressed(item, e);
		((ControlListener) b).itemPressed(item, e);
	} //

	public void itemReleased(VisualItem item, MouseEvent e) {
		((ControlListener) a).itemReleased(item, e);
		((ControlListener) b).itemReleased(item, e);
	} //

	public void itemEntered(VisualItem item, MouseEvent e) {
		((ControlListener) a).itemEntered(item, e);
		((ControlListener) b).itemEntered(item, e);
	} //

	public void itemExited(VisualItem item, MouseEvent e) {
		((ControlListener) a).itemExited(item, e);
		((ControlListener) b).itemExited(item, e);
	} //

	public void itemKeyPressed(VisualItem item, KeyEvent e) {
		((ControlListener) a).itemKeyPressed(item, e);
		((ControlListener) b).itemKeyPressed(item, e);
	} //

	public void itemKeyReleased(VisualItem item, KeyEvent e) {
		((ControlListener) a).itemKeyReleased(item, e);
		((ControlListener) b).itemKeyReleased(item, e);
	} //

	public void itemKeyTyped(VisualItem item, KeyEvent e) {
		((ControlListener) a).itemKeyTyped(item, e);
		((ControlListener) b).itemKeyTyped(item, e);
	} //

	public void mouseEntered(MouseEvent e) {
		((ControlListener) a).mouseEntered(e);
		((ControlListener) b).mouseEntered(e);
	} //
	
	public void mouseExited(MouseEvent e) {
		((ControlListener) a).mouseExited(e);
		((ControlListener) b).mouseExited(e);
	} //
	
	public void mousePressed(MouseEvent e) {
		((ControlListener) a).mousePressed(e);
		((ControlListener) b).mousePressed(e);
	} //
	
	public void mouseReleased(MouseEvent e) {
		((ControlListener) a).mouseReleased(e);
		((ControlListener) b).mouseReleased(e);
	} //
	
	public void mouseClicked(MouseEvent e) {
		((ControlListener) a).mouseClicked(e);
		((ControlListener) b).mouseClicked(e);
	} //
	
	public void mouseDragged(MouseEvent e) {
		((ControlListener) a).mouseDragged(e);
		((ControlListener) b).mouseDragged(e);
	} //
	
	public void mouseMoved(MouseEvent e) {
		((ControlListener) a).mouseMoved(e);
		((ControlListener) b).mouseMoved(e);
	} //
	
	public void mouseWheelMoved(MouseWheelEvent e) {
		((ControlListener) a).mouseWheelMoved(e);
		((ControlListener) b).mouseWheelMoved(e);
	} //
	
	public void keyPressed(KeyEvent e) {
		((ControlListener) a).keyPressed(e);
		((ControlListener) b).keyPressed(e);
	} //
	
	public void keyReleased(KeyEvent e) {
		((ControlListener) a).keyReleased(e);
		((ControlListener) b).keyReleased(e);
	} //
	
	public void keyTyped(KeyEvent e) {
		((ControlListener) a).keyTyped(e);
		((ControlListener) b).keyTyped(e);
	} //

    protected static EventListener addInternal(
            EventListener a, EventListener b)
    {
        if (a == null)
            return b;
        if (b == null)
            return a;
        return new ControlEventMulticaster(a, b);
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
    
	protected ControlEventMulticaster(EventListener a, EventListener b) {
		super(a,b);
	} //
    
} // end of class ControlEventMulticaster
