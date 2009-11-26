package org.codemap.hitmds;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class Visualization implements PaintListener {

	private double[][] points;

	public Visualization() {
		this(Fixture.genesEndo(2000));
	}
	
	public Visualization(double[][] data) {
		points = new Hitmds2()
			.useDistance(new Distance.CORR(8))
			//.useDistance(new Distance.CORR_DERIV_ABS())
			//.useDistance(new Distance.DIST())
			//.useDistance(new Distance.DIST_MINKOWSKI(8))
			.run(data, new ProgressMonitor.Console());
	}
	
	public static void main(String[] args) {
		new Visualization().open();
	}
	
	public void open() {
		Display display = Display.getDefault();
		Shell shell = new Shell(display, SWT.SHELL_TRIM & ~SWT.RESIZE);
		Canvas canvas = new Canvas(shell, SWT.NONE);
		canvas.addPaintListener(this);
		canvas.setSize(512, 512);
		shell.setText("Codemap on SWTeroids");
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
		e.gc.setAntialias(SWT.ON);
		e.gc.setAlpha(128);
		e.gc.drawOval(156, 156, 200, 200);
		for (double[] p: points) {
			int x = (int) (p[0] * 100 + 256);
			int y = (int) (p[1] * 100 + 256);
			e.gc.drawLine(x - 2, y - 2, x + 2, y + 2);
			e.gc.drawLine(x - 2, y + 2, x + 2, y - 2);
		}
	}
	
}
