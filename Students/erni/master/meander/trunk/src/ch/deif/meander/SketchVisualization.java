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
        background(204);
        stroke(0);
        noFill();
        smooth();
        for (Location each: map.locations()) {
            float x = (float) (each.x * width);
            float y = (float) (each.y * height);
            float r = (float) (each.height / 100 * width / 2.61f);
            ellipse(x, y, r, r);
        }
    }

}
