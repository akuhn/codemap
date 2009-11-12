package org.codemap.graph;

import java.util.Arrays;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class Diagram implements PaintListener {

    public Diagram open() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final int ms = 1000 / 1;
                final Display display = new Display();
                final Shell shell = new Shell(display, SWT.SHELL_TRIM & ~SWT.RESIZE);
                final Canvas canvas = new Canvas(shell, SWT.DOUBLE_BUFFERED);
                canvas.addPaintListener(Diagram.this);
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

    private SwissRoll swissRoll = new SwissRoll(1000);
    
    @Override
    public void paintControl(PaintEvent e) {
        Device device = e.gc.getDevice();
        e.gc.setForeground(device.getSystemColor(SWT.COLOR_BLACK));
        e.gc.setAntialias(SWT.ON);
        e.gc.setAlpha(128);
        int zoom = 50;
        e.gc.drawOval(256-zoom, 256-zoom, zoom*2, zoom*2);
        for (int n = 0; n < swissRoll.x.length; n++) {
            int x = (int) (swissRoll.x[n] * zoom + 256);
            int y = (int) (swissRoll.z[n] * zoom + 256);
            e.gc.drawLine(x - 2, y - 2, x + 2, y + 2);
            e.gc.drawLine(x - 2, y + 2, x + 2, y - 2);
        }
        e.gc.setForeground(device.getSystemColor(SWT.COLOR_RED));
        int[][] path = swissRoll.asDistanceMatrix().kayNearestNeighbours(5);
        for (int i = 0; i < path.length; i++) {
            for (int j = 0; j < path.length; j++) {
                if (path[i][j] > 1) continue;
                int x0 = (int) (swissRoll.x[i] * zoom + 256);
                int y0 = (int) (swissRoll.z[i] * zoom + 256);
                int x = (int) (swissRoll.x[j] * zoom + 256);
                int y = (int) (swissRoll.z[j] * zoom + 256);
                e.gc.drawLine(x0, y0, x, y);
            }
        }
    }

    public static void main(String[] args) {
        new Diagram().open();
    }
    
}
