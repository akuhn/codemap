package ch.akuhn.isomap;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ch.akuhn.org.ggobi.plugins.ggvis.Points;

public class IsomapSwissRollTest {

	private static class I extends Isomap {

		private SwissRoll swiss;

		public I(int n, int k) {
			super(n);
			swiss = new SwissRoll(n);
			this.k = k;
		}
		
		 @Override
         protected double dist(int i, int j) {
             return swiss.dist(i,j);
         }
		
	}
	
	@Test
	public void shouldWorkWith1Element() {
		Isomap isomap = new I(1, 3);
		isomap.run();
		Points points = isomap.getPoints();
		assertEquals(1, points.x.length);
	}
	
	@Test
	public void shouldWorkWithLessElementsThanNeighbors() {
		int k = 6, less = 1;
		Isomap isomap = new I(k-less,k);
		isomap.run();
		Points points = isomap.getPoints();
		assertEquals(k-less, points.x.length);
	}

	@Test
	public void shouldWorkWithMoreThanFewElements() {
		int few = 10, more = 2;
		Isomap isomap = new I(few+more, 3);
		isomap.run();
		Points points = isomap.getPoints();
		assertEquals(few+more, points.x.length);
	}
	
	@Test
    public void shouldEmbedSwissroll() throws InterruptedException {
        //Stopwatch.p();
        //Thread.sleep(500);
        //Stopwatch.p();
        Isomap isomap = new I(500, 6);
        isomap.constructNeighborhoodGraph();
        boolean[][] edges = isomap.getEdges();
        isomap.computeShortestPathWithDijkstra();
        isomap.constructDeeDimensionalEmbedding();
        //new Out().put(isomap.getPoints().x);
        //new Out().put(isomap.getPoints().y);
        isomap.getPoints().applyCentering();
        isomap.getPoints().openVisualization(edges);
    }
    
    public static void main(String[] args) throws InterruptedException {
        new IsomapSwissRollTest().shouldEmbedSwissroll();
    }
    
}
