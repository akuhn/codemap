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
        MapBuilder builder = Map.builder()
            .size(200, 200);
        for (int a = 5; a < 90; a += 10) {
            double rad = Math.PI / 180 * a;
            builder.location(0.8*sin(rad), 0.8*cos(rad), a + 25);
        }
        Map map = builder.done();
        System.out.println("1");
        new DEMAlgorithm(map).run();
        System.out.println("2");
        new NormalizeElevationAlgorithm(map).run();
        System.out.println("3");
        new HillshadeAlgorithm(map).run();
        System.out.println("4");
        new ContourLineAlgorithm(map).run();
        System.out.println("5");
        new HillshadeVisualization(map).openApplet();
    }
    
    
}
