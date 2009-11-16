package ch.deif.meander.swt;

import java.util.Collection;
import java.util.HashSet;

import org.eclipse.swt.events.DragDetectEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Transform;
import org.eclipse.swt.widgets.Canvas;

import ch.deif.meander.MapInstance;
import ch.deif.meander.internal.NearestNeighborAlgorithm;
import ch.deif.meander.map.MapValues;
import ch.deif.meander.ui.CodemapEvent;
import ch.deif.meander.ui.CodemapListener;


public final class CodemapVisualization extends CompositeLayer implements PaintListener {

    public CodemapVisualization(MapValues values) {
        this.mapValues = values;
    }

    @Override
    public void paintControl(PaintEvent e) {
        if (!(e.widget instanceof Canvas)) throw new Error();
        try {
            GC gc = e.gc;
            Device device = gc.getDevice();
            Color blue = new Color(device, 0, 0, 255);
            gc.setBackground(blue);
            gc.fillRectangle(gc.getClipping());
            blue.dispose();
            Point bounds = ((Canvas) e.widget).getSize();
            offsetX = (bounds.x - mapValues.getSize()) / 2;
            offsetY = (bounds.y - mapValues.getSize()) / 2;
            Transform t = new Transform(device);
            t.translate(offsetX, offsetY);
            gc.setTransform(t);
            this.paintMap(mapValues, gc);
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

    @Override
    public CodemapVisualization remove(SWTLayer layer) {
        return (CodemapVisualization) super.remove(layer);
    }

    private Collection<CodemapListener> listeners = new HashSet<CodemapListener>();
    private int offsetX;
    private int offsetY;
    private MapValues mapValues;

    protected static void fireEvent(MouseEvent e, String kind, Object value) {
        final CodemapVisualization self = (CodemapVisualization) e.data;
        final CodemapEvent event = new CodemapEvent(kind, self, value);
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (CodemapListener each : self.listeners) {
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


    private void addOffsetAndThisToEvent(MouseEvent e) {
        e.x -= offsetX;
        e.y -= offsetY;
        e.data = this;
    }
    
    @Override
    public void mouseMove(MouseEvent e) {
        this.addOffsetAndThisToEvent(e);
        this.updateTooltip(e);
        super.mouseMove(e);
    }

    private void updateTooltip(MouseEvent e) {
        MapInstance map = mapValues.mapInstance.getValue();
        if (map == null) return;
        boolean noName = map.isEmpty() || !map.containsPoint(e.x, e.y);
        String name = noName ? null : map.get(NearestNeighborAlgorithm.class).get(e.x).get(e.y).getName();
        ((Canvas) e.widget).setToolTipText(name);
    }


    @Override
    public void dragDetected(DragDetectEvent e) {
        this.addOffsetAndThisToEvent(e);
        super.dragDetected(e);
    }

    @Override
    public void mouseDoubleClick(MouseEvent e) {
        this.addOffsetAndThisToEvent(e);
        super.mouseDoubleClick(e);
    }


    @Override
    public void mouseDown(MouseEvent e) {
        this.addOffsetAndThisToEvent(e);
        super.mouseDown(e);
    }


    @Override
    public void mouseEnter(MouseEvent e) {
        this.addOffsetAndThisToEvent(e);
        super.mouseEnter(e);
    }


    @Override
    public void mouseExit(MouseEvent e) {
        this.addOffsetAndThisToEvent(e);
        super.mouseExit(e);
    }


    @Override
    public void mouseHover(MouseEvent e) {
        this.addOffsetAndThisToEvent(e);
        super.mouseHover(e);
    }


    @Override
    public void mouseScrolled(MouseEvent e) {
        this.addOffsetAndThisToEvent(e);
        super.mouseScrolled(e);
    }


    @Override
    public void mouseUp(MouseEvent e) {
        this.addOffsetAndThisToEvent(e);
        super.mouseUp(e);
    }

    public int getSize() {
        return mapValues.getSize();
    }

    protected static MapValues mapValues(MouseEvent e) {
        return ((CodemapVisualization) e.data).mapValues;
    }

}
