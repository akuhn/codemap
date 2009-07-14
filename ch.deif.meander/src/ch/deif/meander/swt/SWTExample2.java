package ch.deif.meander.swt;

import org.eclipse.swt.events.DragDetectEvent;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Transform;
import org.eclipse.swt.widgets.Display;

import ch.deif.meander.MapInstance;

public class SWTExample2 extends SWTLayer {

	public static void main(String[] args) {
		new CodemapVisualization(null).add(new SWTExample2()).openAndBlock();
	}
	
	@Override
	public void dragDetected(DragDetectEvent e) {
		System.out.println("DragDetectListener.dragDetected()");
	}

	@Override
	public void mouseDoubleClick(MouseEvent e) {
		System.out.println("MouseListener.mouseDoubleClick()");
	}

	@Override
	public void mouseDown(MouseEvent e) {
		System.out.println("MouseListener.mouseDown()");
	}

	@Override
	public void mouseUp(MouseEvent e) {
		System.out.println("MouseListener.mouseUp()");
	}

	@Override
	public void mouseMove(MouseEvent e) {
		//System.out.println("MouseMoveListener.mouseMove()");
	}

	@Override
	public void mouseEnter(MouseEvent e) {
		System.out.println("MouseTrackListener.mouseEnter()");
	}

	@Override
	public void mouseExit(MouseEvent e) {
		System.out.println("MouseTrackListener.mouseExit()");
	}

	@Override
	public void mouseHover(MouseEvent e) {
		//System.out.println("MouseTrackListener.mouseHover()");
	}

	@Override
	public void mouseScrolled(MouseEvent e) {
		System.out.println("MouseWheeleListener.mouseScrolled()");
	}

	@Override
	public void menuDetected(MenuDetectEvent e) {
		System.out.println("MenuDetectListener.menuDetected()");
	}

	@Override
	public void paintMap(MapInstance map, GC gc) {
		Display display = Display.getCurrent();
		Transform t = new Transform(display);
		t.identity();
		t.translate(100, 100);
		gc.setTransform(t);
		gc.drawRectangle(0, 0, 50, 50);
		gc.drawRectangle(10, 10, 50, 50);
		t.dispose();
	}	
	
}
