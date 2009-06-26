package sketchbook;

import ch.deif.meander.Map;
import ch.deif.meander.MapBuilder;
import ch.deif.meander.viz.Layers;
import ch.deif.meander.viz.YouAreHereOverlay;

public class YouAreHereOverlaySketch {

	public static void main(String... args) {
		makeMap(600);
	}

	private static void makeMap(int size) {
		MapBuilder builder = Map.builder().pixelSize(size);
		for (int n = 0; n < 50; n++) {
			builder.location(Math.random(), Math.random(), Math.random() * 100);
		}
		Map map = builder.done();

		YouAreHereOverlay overlay = new YouAreHereOverlay(map);
		overlay.setHere(map.locationAt((int) (Math.random() * map.locationCount())));
		new Layers(map).useHillshading().add(overlay).openApplet();
	}

}
