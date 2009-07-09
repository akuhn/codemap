package ch.deif.meander.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import ch.akuhn.hapax.Hapax;
import ch.deif.meander.MapInstance;
import ch.deif.meander.builder.Meander;

public class SWTExample implements PaintListener {

	private MapInstance map;
	private Background layer;

	public SWTExample(MapInstance map) {
		this.map = map;
		this.layer = new Background();
		layer.children.add(new WaterBackground());
		layer.children.add(new ShoreLayer());
		layer.children.add(new HillshadeLayer());
	}

	public static void main(String[] args) {
		MapInstance map = Meander.builder()
			.addCorpus(Hapax.legomenon().makeCorpus("../ch.akuhn.hapax", ".java"))
			.makeMap()
			.withSize(512);
		Display display = Display.getDefault();
		Shell shell = new Shell(display, SWT.SHELL_TRIM & ~SWT.RESIZE);
		Canvas canvas = new Canvas(shell, SWT.NO_BACKGROUND | (SWT.DOUBLE_BUFFERED * 0));
		canvas.addPaintListener(new SWTExample(map));
		canvas.setSize(512, 512);
		shell.setText("Codemap on SWTeroids");
		shell.pack();
		shell.open();
		startAnimationLoop(display, canvas);
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	private static void startAnimationLoop(final Display display, final Canvas canvas) {
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				if (canvas.isDisposed()) return;
				canvas.redraw();
				display.timerExec(1000/25, this);
			}
		};
		display.timerExec(1000/25, runnable);
	}

	@Override
	public void paintControl(PaintEvent e) {
		layer.paintMap(map, e.gc);
		new LabelOverlay().paintMap(map, e.gc);
	}
	
}
