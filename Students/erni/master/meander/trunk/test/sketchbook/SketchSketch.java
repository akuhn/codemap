package sketchbook;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import ch.deif.meander.Map;
import ch.deif.meander.MapBuilder;
import ch.deif.meander.viz.SketchVisualization;

public class SketchSketch {

    public static void main(String... args) {
        MapBuilder builder = Map.builder().size(800, 800);
        for (int a = 5; a < 90; a += 10) {
            double rad = Math.PI / 180 * a;
            builder.location(0.8*sin(rad), 0.8*cos(rad), a + 25);
        }
        Map map = builder.done();
        new SketchVisualization(map).openApplet();

    }
}
