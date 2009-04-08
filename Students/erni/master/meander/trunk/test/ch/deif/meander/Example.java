package ch.deif.meander;

public class Example {

    public static void main(String... args) {
        Map map = new MapBuilder()
                .size(640, 640)
                .location(0.5, 0.5, 80)
                .location(0.2, 0.2, 100)
                .build();
        map.createVisualization();
        
        Layers layers = new Layers(map);
        
        Location l1 = map.locations.get(0);
        Location l2 = map.locations.get(1);
        
        ArrowOverlay viz = new ArrowOverlay(map);
        viz.arrow(l1, l2, 4.0f);
        
        layers.add(viz);
        layers.useHillshading();
        layers.openApplet();
        
    }
    
}
