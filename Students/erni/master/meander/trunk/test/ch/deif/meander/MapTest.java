package ch.deif.meander;

import static java.lang.Double.NaN;
import static org.junit.Assert.assertEquals;

import java.util.Iterator;

import jexample.Depends;
import jexample.JExample;

import org.junit.Test;
import org.junit.runner.RunWith;

import ch.deif.meander.Map.Pixel;

@RunWith(JExample.class)
public class MapTest {

    @Test
    public Map fiveOnThreeMap() {
         Map map = Map.builder()
             .size(5,3)
             .location(0.5, 0.5, 100, "Foo")
             .build();
         assertEquals(5, map.width);
         assertEquals(3, map.height);
         assertEquals(1, map.locationSize());
         return map;
    }
    
    @Test
    @Depends("#fiveOnThreeMap")
    public void testPixelCoordinates(Map map) {
        Iterator<Pixel> it = map.pixels().iterator();
        Pixel p;
        p = it.next();
        assertEquals(0.0, p.x(), NaN);
        assertEquals(0.0, p.y(), NaN);
        p = it.next();
        assertEquals(0.25, p.x(), NaN);
        assertEquals(0.0, p.y(), NaN);
        p = it.next();
        assertEquals(0.5, p.x(), NaN);
        assertEquals(0.0, p.y(), NaN);
        p = it.next();
        assertEquals(0.75, p.x(), NaN);
        assertEquals(0.0, p.y(), NaN);
        p = it.next();
        assertEquals(1.0, p.x(), NaN);
        assertEquals(0.0, p.y(), NaN);
        p = it.next();
        assertEquals(0.0, p.x(), NaN);
        assertEquals(0.5, p.y(), NaN);
        p = it.next();
        assertEquals(0.25, p.x(), NaN);
        assertEquals(0.5, p.y(), NaN);
    }
    
    @Test
    @Depends("#fiveOnThreeMap")
    public Map mapWithDEM(Map map) {
        assertEquals(false, map.hasDEM());
        new ElevationModelAlgorithm(map).run();
        assertEquals(true, map.hasDEM());
        return map;
    }
    
    @Test
    @Depends("#mapWithDEM")
    public void testPixelElevation(Map map) {
        Pixel p;
        p = map.get(2, 1);
        assertEquals(0.5, p.x(), NaN);
        assertEquals(0.5, p.y(), NaN);
        assertEquals(100, p.elevation(), 1e-2);
    }
    
    
}