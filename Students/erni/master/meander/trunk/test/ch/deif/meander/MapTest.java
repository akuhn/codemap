package ch.deif.meander;

import static java.lang.Double.NaN;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static org.junit.Assert.assertEquals;

import java.util.Iterator;

import org.junit.Test;
import org.junit.runner.RunWith;

import ch.akuhn.util.All;
import ch.akuhn.util.Size;
import ch.deif.meander.Map.Pixel;
import ch.unibe.jexample.Given;
import ch.unibe.jexample.JExample;

@RunWith(JExample.class)
public class MapTest {

	@Test
	public Map fiveOnThreeMap() {
		Map map = Map.builder().pixelSize(5).location(0.5, 0.5, 100).done();
		assertEquals(5, map.getWidth());
		assertEquals(1, map.locationCount());
		return map;
	}

	@Test
	@Given("#fiveOnThreeMap")
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
		p = map.get(2, 2);
		assertEquals(0.5, p.x(), NaN);
		assertEquals(0.5, p.y(), NaN);
		// FIXME elevation is 99 instead of 100 as we subtract the threshold
		assertEquals(99, p.elevation(), 1e-2);
	}

	@Test
	public Map makeSampleMap() {
		MapBuilder builder = Map.builder().pixelSize(200);
		for (int a = 5; a < 90; a += 10) {
			double rad = Math.PI / 180 * a;
			builder.location(0.8 * sin(rad), 0.8 * cos(rad), a + 25);
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
		return map;
	}

}
