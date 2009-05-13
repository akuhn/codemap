package casestudies.hapax;

import sketchbook.DefaultSketch;
import ch.deif.meander.viz.LabelsOverlay;
import ch.deif.meander.viz.Layers;

public class LabelTest {

	public static void main(String... args) {
		Layers layers = new Layers(DefaultSketch.makeMap(400));
		layers.add(LabelsOverlay.class);
		layers.drawToPNG("labels");
	}
	
}
