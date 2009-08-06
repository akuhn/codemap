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

import ch.deif.meander.MapInstance;
import ch.deif.meander.internal.NearestNeighborAlgorithm;
import ch.deif.meander.ui.CodemapEvent;
import ch.deif.meander.ui.CodemapListener;


public final class CodemapVisualization extends CompositeLayer implements PaintListener {

	/*default*/ MapInstance map; // FIXME
	private Background background;
	
	public CodemapVisualization(MapInstance map) {
		this();
		this.map = map;
	}
	
	public CodemapVisualization() {
		setRoot(this);
	}	


	@Override
	public void paintControl(PaintEvent e) {
		if (!(e.widget instanceof Canvas)) throw new Error();
		try {
			GC gc = e.gc;
			Device device = gc.getDevice();
			gc.setBackground(device.getSystemColor(SWT.COLOR_BLUE));
			gc.fillRectangle(gc.getClipping());
			Point bounds = ((Canvas) e.widget).getSize();
			offsetX = (bounds.x - map.getWidth()) / 2;
			offsetY = (bounds.y - map.getWidth()) / 2;
			Transform t = new Transform(device);
			t.translate(offsetX, offsetY);
			gc.setTransform(t);
			this.paintMap(map, gc);
			t.dispose();
		}
		catch (Throwable ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public CodemapVisualization add(SWTLayer layer) {
		return (CodemapVisualization) super.add(layer);
	}
	
	public CodemapVisualization addBackground(Background bg) {
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

	public void redrawBackground() {
		background.redrawBackground();
	}
	
	/**
	 * Open an new Shell and display the CodemapVisalization.
	 * For testing purposes only.
	 */
	public void openAndBlock() {
//		Display display = new Display();
//		//Shell shell = new Shell(display, SWT.SHELL_TRIM & ~SWT.RESIZE);
//		Shell shell = new Shell(display, SWT.SHELL_TRIM);
//		Canvas canv = new Canvas(shell, SWT.NONE | SWT.DOUBLE_BUFFERED);
//		canv.setSize(400,300);
//		if (map != null) canv.setSize(map.width + 400, map.height);
//		this.link(canv);
//		
//		
//		Menu menu = new Menu(shell, SWT.POP_UP);
//		MenuItem item = new MenuItem(menu, SWT.PUSH);
//		item.setText("Popup");
//		canv.setMenu(menu);
//		
//		shell.setText("Codemap: " + map);
//		shell.pack();
//		shell.open();
//		//this.startAnimationLoop();
//		while (!shell.isDisposed()) {
//			if (!display.readAndDispatch())
//				display.sleep();
//		}
//		display.dispose();
	}


	private void translate(MouseEvent e) {
		e.x -= offsetX;
		e.y -= offsetY;
	}
	
	@Override
	public void mouseMove(MouseEvent e) {
		this.translate(e);
		String name = !map.containsPoint(e.x, e.y) ? null
				: map.get(NearestNeighborAlgorithm.class).get(e.x).get(e.y).getName();
		((Canvas) e.widget).setToolTipText(name);
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

	public MapInstance getMapInstance() {
		return map;
	}
	
}
