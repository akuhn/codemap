package sketchbook;

import ch.deif.meander.Map;
import ch.deif.meander.MapBuilder;
import ch.deif.meander.viz.ArrowOverlay;
import ch.deif.meander.viz.Layers;

public class ArrowOverlaySketch {

	public static void main(String... args) {
		//makeMap(800);
		//makeMap(200);
		makeMap(600);
	}

	private static void makeMap(int size) {
		long time = System.nanoTime();

		MapBuilder builder = Map.builder().pixelSize(size);
		for (int n = 0; n < 50; n++) {
			builder.location(Math.random(), Math.random(), Math.random() * 100);
		}
		Map map = builder.done();

		System.out.println((System.nanoTime() - time) / 1000000);
		ArrowOverlay overlay = new ArrowOverlay(map);
		for (int n = 0; n < 20; n++) {
			overlay.arrow(
					map.locationAt((int) (map.locationCount() * Math.random())),
					map.locationAt((int) (map.locationCount() * Math.random())),
					Math.random() * 10);
		}
		new Layers(map).useHillshading().add(overlay).openApplet();
	}

}
