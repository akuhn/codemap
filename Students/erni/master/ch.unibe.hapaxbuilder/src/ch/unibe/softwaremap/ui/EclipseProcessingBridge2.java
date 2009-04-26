package ch.unibe.softwaremap.ui;

import java.awt.Dimension;
import java.awt.Frame;

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.widgets.Composite;

import processing.core.PApplet;

/**
 * Bridges between Eclipse (which uses SWT) and Processing (which uses AWT).
 * 
 */
public class EclipseProcessingBridge2 extends Composite {

	private Frame bridge;

	public EclipseProcessingBridge2(Composite parent) {
		super(parent, SWT.EMBEDDED);
		bridge = SWT_AWT.new_Frame(this);
	}

	public void setMaximumSize(Dimension dimension) {
		bridge.setMaximumSize(dimension);
		bridge.setLocation(0, 0);
		bridge.setSize(dimension.width, dimension.height);
	}

	public void setApplet(PApplet applet) {
		applet.init();
		int width = applet.width;
		int height = applet.height;
		applet.setSize(width, height);
		bridge.removeAll();
		bridge.add(applet);
	}

}