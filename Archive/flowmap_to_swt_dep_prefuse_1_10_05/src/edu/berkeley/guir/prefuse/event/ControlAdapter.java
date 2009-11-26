package edu.berkeley.guir.prefuse.event;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import edu.berkeley.guir.prefuse.VisualItem;

/**
 * Adapter class for prefuse interface events. Subclasses can override the
 * desired methods to perform user interface event handling.
 * 
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public class ControlAdapter implements ControlListener {

	/**
	 * @see edu.berkeley.guir.prefuse.event.ControlListener#itemDragged(edu.berkeley.guir.prefuse.VisualItem, java.awt.event.MouseEvent)
	 */
	public void itemDragged(VisualItem gi, MouseEvent e) {
	} //

	/**
	 * @see edu.berkeley.guir.prefuse.event.ControlListener#itemMoved(edu.berkeley.guir.prefuse.VisualItem, java.awt.event.MouseEvent)
	 */
	public void itemMoved(VisualItem gi, MouseEvent e) {
	} //

	/**
	 * @see edu.berkeley.guir.prefuse.event.ControlListener#itemWheelMoved(edu.berkeley.guir.prefuse.VisualItem, java.awt.event.MouseWheelEvent)
	 */
	public void itemWheelMoved(VisualItem gi, MouseWheelEvent e) {
	} //

	/**
	 * @see edu.berkeley.guir.prefuse.event.ControlListener#itemClicked(edu.berkeley.guir.prefuse.VisualItem, java.awt.event.MouseEvent)
	 */
	public void itemClicked(VisualItem gi, MouseEvent e) {
	} //

	/**
	 * @see edu.berkeley.guir.prefuse.event.ControlListener#itemPressed(edu.berkeley.guir.prefuse.VisualItem, java.awt.event.MouseEvent)
	 */
	public void itemPressed(VisualItem gi, MouseEvent e) {
	} //

	/**
	 * @see edu.berkeley.guir.prefuse.event.ControlListener#itemReleased(edu.berkeley.guir.prefuse.VisualItem, java.awt.event.MouseEvent)
	 */
	public void itemReleased(VisualItem gi, MouseEvent e) {
	} //

	/**
	 * @see edu.berkeley.guir.prefuse.event.ControlListener#itemEntered(edu.berkeley.guir.prefuse.VisualItem, java.awt.event.MouseEvent)
	 */
	public void itemEntered(VisualItem gi, MouseEvent e) {
	} //

	/**
	 * @see edu.berkeley.guir.prefuse.event.ControlListener#itemExited(edu.berkeley.guir.prefuse.VisualItem, java.awt.event.MouseEvent)
	 */
	public void itemExited(VisualItem gi, MouseEvent e) {
	} //

	/**
	 * @see edu.berkeley.guir.prefuse.event.ControlListener#itemKeyPressed(edu.berkeley.guir.prefuse.VisualItem, java.awt.event.KeyEvent)
	 */
	public void itemKeyPressed(VisualItem item, KeyEvent e) {
	} //

	/**
	 * @see edu.berkeley.guir.prefuse.event.ControlListener#itemKeyReleased(edu.berkeley.guir.prefuse.VisualItem, java.awt.event.KeyEvent)
	 */
	public void itemKeyReleased(VisualItem item, KeyEvent e) {
	} //

	/**
	 * @see edu.berkeley.guir.prefuse.event.ControlListener#itemKeyTyped(edu.berkeley.guir.prefuse.VisualItem, java.awt.event.KeyEvent)
	 */
	public void itemKeyTyped(VisualItem item, KeyEvent e) {
	} //

	/**
	 * @see edu.berkeley.guir.prefuse.event.ControlListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	public void mouseEntered(MouseEvent e) {	
	} //

	/**
	 * @see edu.berkeley.guir.prefuse.event.ControlListener#mouseExited(java.awt.event.MouseEvent)
	 */
	public void mouseExited(MouseEvent e) {		
	} //

	/**
	 * @see edu.berkeley.guir.prefuse.event.ControlListener#mousePressed(java.awt.event.MouseEvent)
	 */
	public void mousePressed(MouseEvent e) {	
	} //

	/**
	 * @see edu.berkeley.guir.prefuse.event.ControlListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	public void mouseReleased(MouseEvent e) {
	} //

	/**
	 * @see edu.berkeley.guir.prefuse.event.ControlListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	public void mouseClicked(MouseEvent e) {
	} //

	/**
	 * @see edu.berkeley.guir.prefuse.event.ControlListener#mouseDragged(java.awt.event.MouseEvent)
	 */
	public void mouseDragged(MouseEvent e) {
	} //

	/**
	 * @see edu.berkeley.guir.prefuse.event.ControlListener#mouseMoved(java.awt.event.MouseEvent)
	 */
	public void mouseMoved(MouseEvent e) {
	} //

	/**
	 * @see edu.berkeley.guir.prefuse.event.ControlListener#mouseWheelMoved(java.awt.event.MouseWheelEvent)
	 */
	public void mouseWheelMoved(MouseWheelEvent e) {
	} //

	/**
	 * @see edu.berkeley.guir.prefuse.event.ControlListener#keyPressed(java.awt.event.KeyEvent)
	 */
	public void keyPressed(KeyEvent e) {
	} //

	/**
	 * @see edu.berkeley.guir.prefuse.event.ControlListener#keyReleased(java.awt.event.KeyEvent)
	 */
	public void keyReleased(KeyEvent e) {
	} //

	/**
	 * @see edu.berkeley.guir.prefuse.event.ControlListener#keyTyped(java.awt.event.KeyEvent)
	 */
	public void keyTyped(KeyEvent e) {
	} //

} // end of class ControlAdapter
