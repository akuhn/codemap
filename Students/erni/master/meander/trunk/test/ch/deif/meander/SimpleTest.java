package ch.deif.meander;

import org.junit.Test;

import processing.core.PImage;

public class SimpleTest {

    @Test
    public void example() {
        Parameters p = new Parameters();
        p.useExample();
        MapDescription d = new MapDescription(p,
                new Location(-0.25f,+0.25f,40,"left"),
                new Location(+0.25f,+0.50f,100,"large"),
                new Location(+0.50f,+0.00f,30,"right"));
        Map map = d.generateMap();
        new PViewer(map);
    }

    public static void main(String[] args) {
        new SimpleTest().example();
    }
    
}
