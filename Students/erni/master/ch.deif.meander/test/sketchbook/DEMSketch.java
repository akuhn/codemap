package sketchbook;

import ch.deif.meander.Map;
import ch.deif.meander.MapTest;
import ch.deif.meander.internal.DEMAlgorithm;
import ch.deif.meander.visual.DebugDEMVisualization;

public class DEMSketch {

	public static void main(String... args) {
		Map map = new MapTest().makeSampleMap();
		new DEMAlgorithm(map).run();
		new DebugDEMVisualization(map).openApplet();
		System.out.println("done");
	}

}
