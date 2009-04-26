package ch.deif.meander.ui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.widgets.Composite;

import ch.akuhn.util.Get;
import ch.deif.meander.Map;
import ch.deif.meander.MapBuilder;
import ch.deif.meander.Serializer;
import ch.deif.meander.Serializer.MSEDocument;
import ch.deif.meander.Serializer.MSEProject;
import ch.deif.meander.Serializer.MSERelease;
import ch.deif.meander.resources.MSE;
import ch.deif.meander.viz.MapVisualization;

/** Bridges between Eclipse (which uses SWT) and Processing (which uses AWT).
 *
 */
public class EclipseProcessingBridge extends Composite {

	private Frame mapFrame;
    private MeanderApplet applet;
	private static final int MAP_DIM = 256;

	public EclipseProcessingBridge(Composite parent) {
		super(parent, SWT.EMBEDDED);
		mapFrame = SWT_AWT.new_Frame(this);
		mapFrame.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));

		MapVisualization<?> viz = createVizualization();
		this.setMapVizualization(viz);
	}

	public void setMaximumSize(Dimension dimension) {
		mapFrame.setMaximumSize(dimension);
		mapFrame.setLocation(0, 0);
		mapFrame.setSize(dimension.width, dimension.height);
	}

	protected static MapVisualization<?> createVizualization() {
		int nth = 1;
		Serializer ser = new Serializer();
		//TODO: do not use absolute paths here ...
		
		ser.model().importMSE(MSE.junit());
		MSEProject project = ser.model().all(MSEProject.class).iterator()
				.next();
		MSERelease release = Get.element(nth, project.releases);
		MapBuilder builder = Map.builder().size(MAP_DIM);
		for (MSEDocument each : release.documents) {
			builder.location(each, release.name);
		}
		Map map = builder.done();
		return map.getDefauVisualization();
	}

	public void setMapVizualization(MapVisualization<?> viz) {
            int width = viz.map.getParameters().width;
            int height = viz.map.getParameters().height;
            if (applet != null) applet.destroy();
            applet = new MeanderApplet(viz);
            applet.init();
            applet.setSize(width, height);
            mapFrame.removeAll();
            mapFrame.add(applet);
	}

	public void updateSelection(List<String> handleIdentifiers) {
		applet.updateSelection(handleIdentifiers);
		
	}
	
}