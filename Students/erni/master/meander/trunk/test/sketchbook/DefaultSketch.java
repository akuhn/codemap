package sketchbook;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import ch.deif.meander.ContourLineAlgorithm;
import ch.deif.meander.DEMAlgorithm;
import ch.deif.meander.HillshadeAlgorithm;
import ch.deif.meander.Map;
import ch.deif.meander.MapBuilder;
import ch.deif.meander.NormalizeElevationAlgorithm;
import ch.deif.meander.viz.HillshadeVisualization;

public class DefaultSketch {

	public static void main(String... args) {
		long time = System.nanoTime();

		MapBuilder builder = Map.builder().size(256);
		for (int a = 5; a < 90; a += 10) {
			double rad = Math.PI / 180 * a;
			builder.location(0.8 * sin(rad), 0.8 * cos(rad), a + 25);
		}
		Map map = builder.done();
		new DEMAlgorithm(map).run();
		new NormalizeElevationAlgorithm(map).run();
		new HillshadeAlgorithm(map).run();
		new ContourLineAlgorithm(map).run();

		System.out.println((System.nanoTime() - time) / 1000000);
		//new DebugDEMVisualization(map).openApplet();
		new HillshadeVisualization(map).openApplet();
	}

}
