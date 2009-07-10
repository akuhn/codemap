package ch.deif.meander.swt;

import java.util.Collection;
import java.util.HashSet;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import ch.deif.meander.MapInstance;
import ch.deif.meander.ui.CodemapEvent;
import ch.deif.meander.ui.CodemapListener;


public final class CodemapVisualization extends CompositeLayer implements PaintListener {

	private boolean animate;
	private Runnable animationLoop = makeAnimationLoop();
	private Canvas canvas;
	private int frameRate = 25;
	/*default*/ MapInstance map; // FIXME
	
	public CodemapVisualization(MapInstance map) {
		this.map = map;
		this.root = this;
	}


	private Runnable makeAnimationLoop() {
		return new Runnable() {
			@Override
			public void run() {
				if (!animate || canvas == null || canvas.isDisposed()) return;
				canvas.redraw();
				if (animate) canvas.getDisplay().timerExec(1000/frameRate, this);
			}
		};
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
	
	public void openAndBlock() {
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
		//this.startAnimationLoop();
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
	
	public void startAnimationLoop() {
		animate = true;
		animationLoop.run();
	}
	
	public void stopAnimationLoop() {
		animate = false;
	}
	
	private Collection<CodemapListener> listeners = new HashSet<CodemapListener>();

	@Override
	public void fireEvent(final CodemapEvent event) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				for (CodemapListener each : listeners) {
					each.handleEvent(event);
				}
			}
		}).run();
	}
	
	public void removeListener(CodemapListener listener) {
		listeners.remove(listener);
	}

	public void addListener(CodemapListener listener) {
		listeners.add(listener);
	}


	@Override
	public void redraw() {
		canvas.redraw();
	}	
	
}
