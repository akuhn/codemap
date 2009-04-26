package ch.deif.meander;

import org.junit.Test;

import ch.deif.meander.ui.PViewer;
import ch.deif.meander.viz.DebugDEMVisualization;
import ch.deif.meander.viz.HillshadeVisualization;
import ch.deif.meander.viz.MapVisualization;

public class SandboxMain {

    public Map sample() {
        return Map.builder().size(640).location(0.375, 0.375, 40)
                .location(0.5, 0.375, 40).location(0.625, 0.250, 100).location(
                        0.750, 0.500, 30).done();
    }

    public void sketch() {
        Map map = sample();
        MapVisualization viz = map.createVisualization();
        new PViewer(viz);
    }

    @Test
    public void debugDEM() {
        Map map = sample();
        new DEMAlgorithm(map).run();
        MapVisualization viz = new DebugDEMVisualization(map);
        new PViewer(viz);
    }

    @Test
    public void hillshade() {
        Map map = sample();
        new DEMAlgorithm(map).run();
        new HillshadeAlgorithm(map).run();
        new ContourLineAlgorithm(map).run();
        MapVisualization viz = new HillshadeVisualization(map);
        //viz.drawToPDF("example"); TODO fix!
        //viz.drawToPNG("example"); TODO fix!
        viz.openApplet();
    }

    public static void main(String[] args) {
        new SandboxMain().hillshade();
    }

}
