package sandbox;

import java.awt.Dimension;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import sketchbook.LabelSketch;
import ch.deif.meander.ui.EclipseProcessingBridge;

public class MeanderWindow {

	private static EclipseProcessingBridge bridge;

	public static void main(String[] args) {
		Display display = new Display();
		final Shell shell = new Shell(display);

		FillLayout layout = new FillLayout();
		shell.setLayout(layout);
		
		new Label(shell, SWT.DEFAULT);
		
		display.addFilter(SWT.MouseDown, new Listener(){
			@Override
			public void handleEvent(Event event) {
				System.out.println(event);
			}
		});
	
		shell.addControlListener(new ControlListener(){
		
			@Override
			public void controlResized(ControlEvent e) {
//				processResize(shell);
			}
		
			@Override
			public void controlMoved(ControlEvent e) {
			}
			
		});

		createMeander(shell);
		loop(display, shell);
	}
	
	private static void processResize(Shell shell) {
		Point size = shell.getSize();
		int dimension = Math.min(size.x, size.y);
		bridge.setMapVizualization(LabelSketch.createLabeledSketch(dimension));
		bridge.setMaximumSize(new Dimension(dimension, dimension));		
	}

	private static void createMeander(Shell shell) {
		bridge = new EclipseProcessingBridge(shell);
		bridge.setMapVizualization(LabelSketch.createLabeledSketch());
		bridge.setMaximumSize(new Dimension(800, 800));
	}

	private static void loop(Display display, Shell shell) {
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}
		display.dispose();
	}

}
