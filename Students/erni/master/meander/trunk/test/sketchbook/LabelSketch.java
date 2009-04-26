package sketchbook;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import ch.deif.meander.Map;
import ch.deif.meander.MapBuilder;
import ch.deif.meander.viz.Label;
import ch.deif.meander.viz.LabelsOverlay;

public class LabelSketch {

    public final static String[] names = ("the quick brown fox jumps over the lazy dog.\n" +
            "No man is an island. Boustrophedon is greek for as the ox turns in plowing").split("\\s+");
    
    public static void main(String... args) {
        MapBuilder builder = Map.builder().size(800);
        for (int n = 0, a = 5; a < 90; a += 10) {
            double rad = Math.PI / 180 * a;
            builder.location(0.8*sin(rad), 0.8*cos(rad), a + 25, names[n++]);
        }
        for (int n = 0; n < 60; n++) {
            builder.location(
                    Math.random(), 
                    Math.random(), 
                    (int) (Math.random() * Math.random() * 140) + 10, 
                    names[(int) (Math.random() * names.length)]);
        }
        Map map = builder.done();
        Label.DRAFT = true;
        new LabelsOverlay(map).openApplet();
        
    }
    
}
