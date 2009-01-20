package ch.deif.meander;

@SuppressWarnings("serial")
public class SketchVisualization extends MapVisualization {

    public SketchVisualization(Map map) {
        super(map);
    }

    @Override
    public void draw() {
        float width = params.width;
        float height = params.height;
        g.background(204);
        g.stroke(0);
        g.noFill();
        for (Location each: map.locations()) {
            float x = (each.x + 1) * width / 2;
            float y = (1 - each.y) * height / 2;
            float r = each.height / 100 * width / 2.61f;
            g.ellipse(x, y, r, r);
        }
    }

}
