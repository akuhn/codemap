package ch.akuhn.org.ggobi.plugins.ggvis;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class PointsDatavis implements PaintListener {

    private int zoom = 10;
    private Points points;

    public PointsDatavis(Points points) {
        this.points = points;
    }

    public PointsDatavis open() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final int ms = 1000 / 50;
                final Display display = new Display();
                final Shell shell = new Shell(display, SWT.SHELL_TRIM & ~SWT.RESIZE);
                final Canvas canvas = new Canvas(shell, SWT.DOUBLE_BUFFERED);
                canvas.addPaintListener(PointsDatavis.this);
                canvas.setSize(512, 512);
                shell.setText("MDS");
                shell.pack();
                shell.open();
                display.timerExec(ms , new Runnable() {
                    @Override
                    public void run() {
                        if (shell.isDisposed()) return;
                        canvas.redraw();
                        display.timerExec(ms, this);
                    }
                });
                while (!shell.isDisposed()) {
                    if (!display.readAndDispatch())
                        display.sleep();
                }
                display.dispose();
                System.exit(-1);
            }
        }).start();
        return this;
    }

    @Override
    public void paintControl(PaintEvent e) {
        if (points == null) return;
        Device device = e.gc.getDevice();
        e.gc.setForeground(device.getSystemColor(SWT.COLOR_BLACK));
        e.gc.setAntialias(SWT.ON);
        e.gc.setAlpha(128);
        e.gc.drawOval(256-zoom, 256-zoom, zoom*2, zoom*2);
        for (int i = 0; i < points.x.length; i++) {
            int x = (int) (points.x[i] * zoom + 256);
            int y = (int) (points.y[i] * zoom + 256);
            e.gc.drawLine(x - 2, y - 2, x + 2, y + 2);
            e.gc.drawLine(x - 2, y + 2, x + 2, y - 2);
        }
    }

}
