package ch.deif.meander.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import ch.deif.meander.MapInstance;


public final class CodemapVisualization extends CompositeLayer implements PaintListener {

	private MapInstance map;
	private Canvas canvas;
	
	public CodemapVisualization(MapInstance map) {
		this.map = map;
	}

	public void link(Canvas newCanvas) {
		if (canvas == newCanvas) return;
		this.unlink();
		canvas = newCanvas;
		canvas.addPaintListener(this);
		canvas.addMouseListener(this);
		canvas.addMouseMoveListener(this);
		canvas.addMouseTrackListener(this);
		canvas.addMouseWheelListener(this);
		canvas.addDragDetectListener(this);
		canvas.addMenuDetectListener(this);
	}

	public void unlink() {
		if (canvas != null) {
			canvas.removePaintListener(this);
			canvas.removeMouseListener(this);
			canvas.removeMouseMoveListener(this);
			canvas.removeMouseTrackListener(this);
			canvas.removeMouseWheelListener(this);
			canvas.removeDragDetectListener(this);
			canvas.removeMenuDetectListener(this);
		}
	}

	@Override
	public void paintControl(PaintEvent e) {
		this.paintMap(map, e.gc);
	}
	
	public void open() {
		assert this.canvas == null;
		Display display = Display.getDefault();
		Shell shell = new Shell(display, SWT.SHELL_TRIM & ~SWT.RESIZE);
		Canvas canvas = new Canvas(shell, SWT.NONE);
		canvas.setSize(400,300);
		if (map != null) canvas.setSize(map.width, map.height);
		this.link(canvas);
		shell.setText("Codemap: " + map);
		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	@Override
	public CodemapVisualization add(SWTLayer layer) {
		return (CodemapVisualization) super.add(layer);
	}

	@Override
	public CodemapVisualization remove(SWTLayer layer) {
		return (CodemapVisualization) super.remove(layer);
	}		
	
	
	
}
