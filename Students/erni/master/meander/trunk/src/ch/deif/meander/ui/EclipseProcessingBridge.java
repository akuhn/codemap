package ch.deif.meander.ui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.widgets.Composite;

import ch.deif.meander.viz.MapVisualization;

/**
 * Bridges between Eclipse (which uses SWT) and Processing (which uses AWT).
 */
public class EclipseProcessingBridge extends Composite {

	private Frame mapFrame;
	private MeanderApplet applet;

	public EclipseProcessingBridge(Composite parent) {
		super(parent, SWT.EMBEDDED);
		mapFrame = SWT_AWT.new_Frame(this);
		mapFrame.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
		applet = new MeanderApplet();
		getApplet().init();
		mapFrame.add(getApplet());
	}

	public void setMaximumSize(Dimension dimension) {
		mapFrame.setMaximumSize(dimension);
		mapFrame.setLocation(0, 0);
		mapFrame.setSize(dimension.width, dimension.height);
	}

	public void setMapVizualization(MapVisualization<?> viz) {
		getApplet().setVisualization(viz);
		mapFrame.repaint();
	}

	public void updateSelection(List<String> handleIdentifiers) {
		getApplet().updateSelection(handleIdentifiers);
	}

	public MeanderApplet getApplet() {
		return applet;
	}

}