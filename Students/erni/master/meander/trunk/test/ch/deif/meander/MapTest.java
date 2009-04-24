package ch.deif.meander;

import static java.lang.Double.NaN;
import static org.junit.Assert.*;

import java.util.Iterator;

import ch.unibe.jexample.Given;
import ch.unibe.jexample.JExample;

import org.junit.Test;
import org.junit.runner.RunWith;

import ch.akuhn.util.All;
import ch.akuhn.util.Size;
import ch.deif.meander.Map.Pixel;

@RunWith(JExample.class)
public class MapTest {

    @Test
    public Map fiveOnThreeMap() {
        Map map = Map.builder().size(5, 3).location(0.5, 0.5, 100).done();
        assertEquals(5, map.getWidth());
        assertEquals(3, map.getHeight());
        assertEquals(1, map.locationCount());
        return map;
    }

    @Test
    @Given("#fiveOnThreeMap")
    public void testPixelCoordinates(Map map) {
        Iterator<Pixel> it = map.pixels().iterator();
        Pixel p;
        p = it.next();
        assertEquals(0.0, p.xNormed(), NaN);
        assertEquals(0.0, p.yNormed(), NaN);
        p = it.next();
        assertEquals(0.25, p.xNormed(), NaN);
        assertEquals(0.0, p.yNormed(), NaN);
        p = it.next();
        assertEquals(0.5, p.xNormed(), NaN);
        assertEquals(0.0, p.yNormed(), NaN);
        p = it.next();
        assertEquals(0.75, p.xNormed(), NaN);
        assertEquals(0.0, p.yNormed(), NaN);
        p = it.next();
        assertEquals(1.0, p.xNormed(), NaN);
        assertEquals(0.0, p.yNormed(), NaN);
        p = it.next();
        assertEquals(0.0, p.xNormed(), NaN);
        assertEquals(0.5, p.yNormed(), NaN);
        p = it.next();
        assertEquals(0.25, p.xNormed(), NaN);
        assertEquals(0.5, p.yNormed(), NaN);
    }

    @Test
    @Given("#fiveOnThreeMap")
    public Map mapWithDEM(Map map) {
        assertEquals(false, map.hasDEM());
        new DEMAlgorithm(map).run();
        assertEquals(true, map.hasDEM());
        return map;
    }

    @Test
    @Given("#mapWithDEM")
    public void testPixelElevation(Map map) {
        Pixel p;
        p = map.get(2, 1);
        assertEquals(0.5, p.xNormed(), NaN);
        assertEquals(0.5, p.yNormed(), NaN);
        assertEquals(100, p.elevation(), 1e-2);
    }
    
    @Test
    public Map makeSampleMap() {
        MapBuilder builder = Map.builder()
            .size(200, 200);
        for (int a = 5; a < 90; a += 10) {
            double rad = Math.PI / 180 * a;
            builder.location(Math.sin(rad), Math.cos(rad), a);
        }
        return builder.done();
    }
    
    @Test 
    @Given("#makeSampleMap")
    public Map testLocations(Map map) {
        assertEquals(9, map.locationCount());
        assertEquals(9, Size.of(map.locations()));
        assertEquals(true, All.notNull(map.locations()));
        return map;
    }
    
    @Test
    @Given("#makeSampleMap")
    public Map testPixelDimension(Map map) {
        assertEquals(200, map.getWidth());
        assertEquals(200, map.getHeight());
        return map;
    }
    
}
