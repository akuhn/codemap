package ch.deif.meander.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DragDetectEvent;
import org.eclipse.swt.events.DragDetectListener;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.MouseWheelListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Transform;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class SWTExample2 implements 
		PaintListener, 
		MouseListener,
		MouseMoveListener,
		MouseTrackListener,
		MouseWheelListener,
		MenuDetectListener,
		DragDetectListener {

	public static void main(String[] args) {
		SWTExample2 eg = new SWTExample2();
		Display display = Display.getDefault();
		Shell shell = new Shell(display, SWT.SHELL_TRIM & ~SWT.RESIZE);
		Canvas canvas = new Canvas(shell, SWT.NONE);
		canvas.addPaintListener(eg);
		canvas.addDragDetectListener(eg);
		canvas.addMouseListener(eg);
		canvas.addMouseMoveListener(eg);
		canvas.addMouseTrackListener(eg);
		canvas.addMouseWheelListener(eg);
		canvas.addMenuDetectListener(eg);
		canvas.setSize(512, 512);
		shell.setText("Codemap example (2)");
		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	@Override
	public void paintControl(PaintEvent e) {
		Display display = Display.getCurrent();
		Transform t = new Transform(display);
		t.identity();
		t.translate(100, 100);
		e.gc.setTransform(t);
		e.gc.drawRectangle(0, 0, 50, 50);
		e.gc.drawRectangle(10, 10, 50, 50);
		t.dispose();
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
	
}
