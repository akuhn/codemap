/**
 * 
 */
package ch.deif.meander.ui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.widgets.Composite;

import ch.akuhn.util.Get;
import ch.deif.meander.Map;
import ch.deif.meander.MapBuilder;
import ch.deif.meander.MapVisualization;
import ch.deif.meander.Serializer;
import ch.deif.meander.Serializer.MSEDocument;
import ch.deif.meander.Serializer.MSEProject;
import ch.deif.meander.Serializer.MSERelease;
import ch.deif.meander.ui.Applet.MapViz;

public class SoftwareMap extends Composite {

	public final MapViz applet;
	public final Map map;
	private Frame mapFrame;
	private static final int MAP_DIM = 700;

	public SoftwareMap(Composite parent) {
		super(parent, SWT.EMBEDDED);
		mapFrame = SWT_AWT.new_Frame(this);
		mapFrame.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));

		MapVisualization viz = createVizualization();
		this.map = viz.map;
		int width = viz.map.getParameters().width;
		int height = viz.map.getParameters().height;

		applet = new Applet.MapViz(viz);
		applet.init();
		applet.setSize(width, height);
		mapFrame.add(applet);
	}

	public void setMaximumSize(Dimension dimension) {
		mapFrame.setMaximumSize(dimension);
		mapFrame.setLocation(0, 0);
		mapFrame.setSize(dimension.width, dimension.height);
	}

	protected MapVisualization createVizualization() {
		int nth = 1;
		Serializer ser = new Serializer();
		ser.model().importMSEFile("mse/junit_with_terms.mse");
		MSEProject project = ser.model().all(MSEProject.class).iterator()
				.next();
		MSERelease release = Get.element(nth, project.releases);
		MapBuilder builder = Map.builder().size(MAP_DIM, MAP_DIM);
		for (MSEDocument each : release.documents) {
			builder.location(each, release.name);
		}
		Map map = builder.build();
		return map.getDefauVisualization();
	}

}