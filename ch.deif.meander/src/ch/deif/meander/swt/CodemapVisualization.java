package ch.deif.meander.swt;

import java.util.Collection;
import java.util.HashSet;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DragDetectEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Transform;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

import ch.deif.meander.MapInstance;
import ch.deif.meander.internal.NearestNeighborAlgorithm;
import ch.deif.meander.ui.CodemapEvent;
import ch.deif.meander.ui.CodemapListener;


public final class CodemapVisualization extends CompositeLayer implements PaintListener {

	private boolean animate;
	private Runnable animationLoop = makeAnimationLoop();
	Canvas canvas;
	private int frameRate = 25;
	/*default*/ MapInstance map; // FIXME
	private Background background;
	
	public CodemapVisualization(MapInstance map) {
		this();
		this.map = map;
	}
	
	public CodemapVisualization() {
		setRoot(this);
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
		final Canvas oldCanvas = canvas;
		canvas = newCanvas;	
		Display.getDefault().asyncExec(new Runnable(){
			@Override
			public void run() {
				linkInternal(newCanvas);
				unlinkInternal(oldCanvas);
				canvas.redraw();
			}
		});
	}
	
	private void linkInternal(Canvas newCanvas) {
		newCanvas.addPaintListener(CodemapVisualization.this);
		newCanvas.addMouseListener(CodemapVisualization.this);
		newCanvas.addMouseMoveListener(CodemapVisualization.this);
		newCanvas.addMouseTrackListener(CodemapVisualization.this);
		newCanvas.addMouseWheelListener(CodemapVisualization.this);
		newCanvas.addDragDetectListener(CodemapVisualization.this);
	}

	@Override
	public void paintControl(PaintEvent e) {
		if (canvas == null) return;
		GC gc = e.gc;
		Device device = gc.getDevice();
		gc.setBackground(device.getSystemColor(SWT.COLOR_BLUE));
		gc.fillRectangle(gc.getClipping());
		Point bounds = canvas.getSize();
		offsetX = (bounds.x - map.getWidth()) / 2;
		offsetY = (bounds.y - map.getWidth()) / 2;
		Transform t = new Transform(device);
		t.translate(offsetX, offsetY);
		gc.setTransform(t);
		this.paintMap(map, gc);
		t.dispose();
	}

	@Override
	public CodemapVisualization add(SWTLayer layer) {
		return (CodemapVisualization) super.add(layer);
	}
	
	public CodemapVisualization add(Background bg) {
		background = bg;
		background.setRoot(getRoot());
		return this;
	}	

	@Override
	public CodemapVisualization remove(SWTLayer layer) {
		return (CodemapVisualization) super.remove(layer);
	}
	
	
	
	@Override
	public void paintMap(MapInstance map, GC gc) {
		if (background != null) background.paintMap(map, gc);
		super.paintMap(map, gc);
	}

	public void startAnimationLoop() {
		animate = true;
		animationLoop.run();
	}
	
	public void stopAnimationLoop() {
		animate = false;
	}
	
	private Collection<CodemapListener> listeners = new HashSet<CodemapListener>();
	private int offsetX;
	private int offsetY;

	@Override
	public void fireEvent(final CodemapEvent event) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				for (CodemapListener each : listeners) {
					each.handleEvent(event);
				}
			}
		}).start();
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
	
	public void redrawBackground() {
		if (background == null) return;
		background.redraw();
	}
	
	/**
	 * Open an new Shell and display the CodemapVisalization.
	 * For testing purposes only.
	 */
	public void openAndBlock() {
		assert this.canvas == null;
		Display display = new Display();
		//Shell shell = new Shell(display, SWT.SHELL_TRIM & ~SWT.RESIZE);
		Shell shell = new Shell(display, SWT.SHELL_TRIM);
		Canvas canv = new Canvas(shell, SWT.NONE | SWT.DOUBLE_BUFFERED);
		canv.setSize(400,300);
		if (map != null) canv.setSize(map.width + 400, map.height);
		this.link(canv);
		
		
		Menu menu = new Menu(shell, SWT.POP_UP);
		MenuItem item = new MenuItem(menu, SWT.PUSH);
		item.setText("Popup");
		canv.setMenu(menu);
		
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
	}
	
	private void translate(MouseEvent e) {
		e.x -= offsetX;
		e.y -= offsetY;
	}
	
	@Override
	public void mouseMove(MouseEvent e) {
		if (canvas == null) return;
		this.translate(e);
		String name = !map.containsPoint(e.x, e.y) ? null
				: map.get(NearestNeighborAlgorithm.class).get(e.x).get(e.y).getName();
		canvas.setToolTipText(name);
		super.mouseMove(e);
	}


	@Override
	public void dragDetected(DragDetectEvent e) {
		this.translate(e);
		super.dragDetected(e);
	}

	@Override
	public void mouseDoubleClick(MouseEvent e) {
		this.translate(e);
		super.mouseDoubleClick(e);
	}


	@Override
	public void mouseDown(MouseEvent e) {
		this.translate(e);
		super.mouseDown(e);
	}


	@Override
	public void mouseEnter(MouseEvent e) {
		this.translate(e);
		super.mouseEnter(e);
	}


	@Override
	public void mouseExit(MouseEvent e) {
		this.translate(e);
		super.mouseExit(e);
	}


	@Override
	public void mouseHover(MouseEvent e) {
		this.translate(e);
		super.mouseHover(e);
	}


	@Override
	public void mouseScrolled(MouseEvent e) {
		this.translate(e);
		super.mouseScrolled(e);
	}


	@Override
	public void mouseUp(MouseEvent e) {
		this.translate(e);
		super.mouseUp(e);
	}

	public int getSize() {
		return map.getWidth();
	}
	
}
