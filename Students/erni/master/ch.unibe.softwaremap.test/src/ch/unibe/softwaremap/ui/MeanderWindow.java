package ch.unibe.softwaremap.ui;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import sketchbook.LabelSketch;
import ch.deif.meander.Colors;
import ch.deif.meander.Location;
import ch.deif.meander.Map;
import ch.deif.meander.internal.NearestNeighborAlgorithm;
import ch.deif.meander.viz.Layers;
import ch.deif.meander.viz.SelectionOverlay;
import ch.unibe.softwaremap.util.EclipseProcessingBridge;

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
		bridge = new EclipseProcessingBridge(shell, EclipseProcessingBridge.createApplet());
		Layers layers = LabelSketch.createLabeledSketch();
		new NearestNeighborAlgorithm(layers.getMap()).run();
		layers.add(SelectionOverlay.class);
		
		Location loc = findLocation(layers.getMap(), "the");
		loc.setColor(new Colors(255, 0, 0));
		
		loc = findLocation(layers.getMap(), "for");
		loc.setColor(new Colors(255, 0, 0));	
		
		loc = findLocation(layers.getMap(), "fox");
		loc.setColor(new Colors(255, 0, 0));			
		
		bridge.setMapVizualization(layers);
		
		
		int dim = layers.getMap().getParameters().width;
		shell.setSize(dim, dim);
	}
	
	private static Location findLocation(Map map, String name) {
		for (Location each: map.locations()) {
			if (each.name().equals(name))
				return each;
		}
		return null;
	}

	private static void loop(Display display, Shell shell) {
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}
		display.dispose();
	}

}
