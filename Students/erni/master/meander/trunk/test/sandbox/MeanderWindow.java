package sandbox;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import sketchbook.LabelSketch;
import ch.deif.meander.ui.EclipseProcessingBridge;
import ch.deif.meander.viz.Layers;
import ch.deif.meander.viz.SelectionOverlay;

public class MeanderWindow {

	private static EclipseProcessingBridge bridge;

	public static void main(String[] args) {
		Display display = new Display();
		final Shell shell = new Shell(display);

		FillLayout layout = new FillLayout();
		shell.setLayout(layout);
	
		createMeander(shell);
		loop(display, shell);
	}
	
	private static void createMeander(Shell shell) {
		bridge = new EclipseProcessingBridge(shell);
		Layers layers = LabelSketch.createLabeledSketch();
		layers.add(SelectionOverlay.class);
		bridge.setMapVizualization(layers);
		int dim = layers.map.getParameters().width;
		shell.setSize(dim, dim);
	}

	private static void loop(Display display, Shell shell) {
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}
		display.dispose();
	}

}
