package ch.deif.meander;

import org.junit.Test;

public class SandboxMain {

    public Map sample() {
        return Map.builder()
            .size(640, 640)
            .location(0.375, 0.375, 40, "left")
            .location(0.625, 0.250, 100,"large")
            .location(0.750, 0.500, 30,"right")
            .build();
    }
    
    public void sketch() {
        Map map = sample();
        MapVisualization viz = map.createVisualization();
        new PViewer(viz);
    }

    @Test 
    public void debugDEM() {
        Map map = sample();
        new ElevationModelAlgorithm(map).run();
        MapVisualization viz = new DebugDEMVisualization(map);
        new PViewer(viz);
    }
    
    public static void main(String[] args) {
        new SandboxMain().debugDEM();
    }
    
}
