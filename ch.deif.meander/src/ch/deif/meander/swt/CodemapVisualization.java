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

	public void link(final Canvas newCanvas) {
		if (canvas == newCanvas) return;
		// remember the old canvas, call to unlink needs this ...
		final Canvas oldCanvas = canvas;
		Display.getDefault().asyncExec(new Runnable(){
			@Override
			public void run() {
				linkInternal(newCanvas);
				unlinkInternal(oldCanvas);
			}
		});
		canvas = newCanvas;		
	}
	
	private void linkInternal(Canvas newCanvas) {
		newCanvas.addPaintListener(CodemapVisualization.this);
		newCanvas.addMouseListener(CodemapVisualization.this);
		newCanvas.addMouseMoveListener(CodemapVisualization.this);
		newCanvas.addMouseTrackListener(CodemapVisualization.this);
		newCanvas.addMouseWheelListener(CodemapVisualization.this);
		newCanvas.addDragDetectListener(CodemapVisualization.this);
		newCanvas.addMenuDetectListener(CodemapVisualization.this);		
	}

	@Override
	public void paintControl(PaintEvent e) {
		this.paintMap(map, e.gc);
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
		}).start(); // FIXME start, not run, right?
	}
	
	public void removeListener(CodemapListener listener) {
		listeners.remove(listener);
	}

	public void addListener(CodemapListener listener) {
		listeners.add(listener);
	}

	@Override
	public void redraw() {
		if (canvas == null) return;
		canvas.redraw();
	}
	
	/**
	 * Open an new Shell and display the CodemapVisalization.
	 * For testing purposes only.
	 */
	public void openAndBlock() {
		assert this.canvas == null;
		Display display = Display.getDefault();
		Shell shell = new Shell(display, SWT.SHELL_TRIM | SWT.RESIZE);
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


	public void unlink() {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				unlinkInternal(canvas);
			}
		});
		canvas = null;
	}	
	
	private void unlinkInternal(Canvas linkedCanvas) {
		if (linkedCanvas == null) return;
		linkedCanvas.removePaintListener(CodemapVisualization.this);
		linkedCanvas.removeMouseListener(CodemapVisualization.this);
		linkedCanvas.removeMouseMoveListener(CodemapVisualization.this);
		linkedCanvas.removeMouseTrackListener(CodemapVisualization.this);
		linkedCanvas.removeMouseWheelListener(CodemapVisualization.this);
		linkedCanvas.removeDragDetectListener(CodemapVisualization.this);
		linkedCanvas.removeMenuDetectListener(CodemapVisualization.this);
	}
}
