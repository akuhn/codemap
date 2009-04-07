package ch.deif.meander;

import static java.lang.Double.NaN;
import static org.junit.Assert.assertEquals;

import java.util.Iterator;

import ch.unibe.jexample.Given;
import ch.unibe.jexample.JExample;

import org.junit.Test;
import org.junit.runner.RunWith;

import ch.deif.meander.Map.Pixel;

@RunWith(JExample.class)
public class MapTest {

    @Test
    public Map fiveOnThreeMap() {
        Map map = Map.builder().size(5, 3).location(0.5, 0.5, 100).build();
        assertEquals(5, map.width);
        assertEquals(3, map.height);
        assertEquals(1, map.locationSize());
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

}
