package ch.akuhn.org.ggobi.plugins.ggvis;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import ch.akuhn.mds.MultidimensionalScaling.MultidimensionalScalingListener;

public class Viz implements PaintListener, MultidimensionalScalingListener {

    public Mds mds;

    public Viz open() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final int ms = 1000 / 50;
                final Display display = new Display();
                final Shell shell = new Shell(display, SWT.SHELL_TRIM & ~SWT.RESIZE);
                final Canvas canvas = new Canvas(shell, SWT.DOUBLE_BUFFERED);
                canvas.addPaintListener(Viz.this);
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
        if (mds == null) return;
        Device device = e.gc.getDevice();
        e.gc.setAntialias(SWT.ON);
        e.gc.setAlpha(128);
        int zoom = 100;
        e.gc.drawOval(256-zoom, 256-zoom, zoom*2, zoom*2);
        final double[][] pps = mds.points();
        if (pps == null) return;
        for (int i = 0; i < pps[0].length; i++) {
            int x = (int) (pps[0][i] * zoom + 256);
            int y = (int) (pps[1][i] * zoom + 256);
            e.gc.drawLine(x - 2, y - 2, x + 2, y + 2);
            e.gc.drawLine(x - 2, y + 2, x + 2, y - 2);
        }
        e.gc.setForeground(device.getSystemColor(SWT.COLOR_BLUE));
        drawHistogram(e, mds.config_dist.getHistogram());
        e.gc.setForeground(device.getSystemColor(SWT.COLOR_GREEN));
        drawHistogram(e, mds.Dtarget.getHistogram());
    }

    private void drawHistogram(PaintEvent e, int[] histo) {
        for (int n = 0; n < histo.length; n++) {
            int xa = (512 * Math.max(0, n - 1)) / histo.length;
            int xb = (512 * n) / histo.length;
            int ya = 512 - histo[Math.max(0, n - 1)]/10;
            int yb = 512 - histo[n]/10 ;    
            e.gc.drawLine(xa, ya, xb, ya);
            e.gc.drawLine(xb, ya, xb, yb);
        }
    }

    @Override
    public void update(Mds mds) {
        this.mds = mds;
    }

}
