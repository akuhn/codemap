package ch.unibe.softwaremap.util;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import ch.deif.meander.ui.MeanderApplet;
import ch.deif.meander.viz.MapVisualization;

/**
 * Bridges between Eclipse (which uses SWT) and Processing (which uses AWT).
 */
public class EclipseProcessingBridge extends Composite {

	private final Frame mapFrame;
	private final MeanderApplet applet;

	public EclipseProcessingBridge(Composite parent, MeanderApplet applet) {
		super(parent, SWT.EMBEDDED);
		setBackground(parent.getBackground());
		
		mapFrame = SWT_AWT.new_Frame(this);
		mapFrame.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
		this.applet = applet;
		mapFrame.add(getApplet());
	}

	public static MeanderApplet createApplet() {
		MeanderApplet meanderApplet = new MeanderApplet();
		meanderApplet.init();
		return meanderApplet;
	}

	public void setMaximumSize(Dimension dimension) {
		mapFrame.setMaximumSize(dimension);
		mapFrame.setLocation(0, 0);
		mapFrame.setSize(dimension.width, dimension.height);
	}

	public void setMapVizualization(final MapVisualization viz) {
		getApplet().setVisualization(viz);
		mapFrame.setSize(viz.getWidth(), viz.getWidth());
		Display.getDefault().asyncExec(new Runnable() {
			
			@Override
			public void run() {
				EclipseProcessingBridge.this.setSize(viz.getWidth(), viz.getWidth());
				
			}
		});
		mapFrame.repaint();
	}

	public void updateSelection(List<String> handleIdentifiers) {
		getApplet().updateSelection(handleIdentifiers);
	}

	public void addSelection(List<String> handleIdentifiers) {
		getApplet().addSelection(handleIdentifiers);
	}	

	public MeanderApplet getApplet() {
		return applet;
	}

}