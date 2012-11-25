package ch.akuhn.isomap;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ch.akuhn.org.ggobi.plugins.ggvis.Points;

public class IsomapTest {

	private static class NullIsomap extends Isomap {

		public NullIsomap(int size) {
			super(size);
			assert size <= 1; 
		}

		@Override
		protected double dist(int i, int j) {
			throw new AssertionError("should not be called");
		}
		
	}
	
	@Test
	public void testIsomapSizeZero() {
		Isomap isomap = new NullIsomap(0);
		isomap.run();
		Points points = isomap.getPoints();
		assertEquals(0, points.size());
	}
	
	@Test
	public void testIsomapSizeOne() {
		Isomap isomap = new NullIsomap(1);
		isomap.run();
		Points points = isomap.getPoints();
		assertEquals(1, points.size());
		assertEquals(0, points.x[0], Double.MIN_VALUE);
		assertEquals(0, points.y[0], Double.MIN_VALUE);
	}
	
}
