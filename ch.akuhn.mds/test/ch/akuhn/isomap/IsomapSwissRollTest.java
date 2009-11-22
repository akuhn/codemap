package ch.akuhn.isomap;

import org.junit.Test;

import ch.akuhn.isomap.beta.SwissRoll;
import ch.akuhn.util.Out;

public class IsomapSwissRollTest {

    @Test
    public void shouldEmbedSwissroll() {
        Isomap isomap = new Isomap(200) {
            { k = 6; }
            SwissRoll swiss = new SwissRoll(n);
            @Override
            protected double dist(int i, int j) {
                return swiss.dist(i,j);
            }
        };
        isomap.constructNeighborhoodGraph();
        boolean[][] edges = isomap.getEdges();
        isomap.computeShortestPath();
        isomap.constructDeeDimensionalEmbedding();
        new Out().put(isomap.getPoints().x);
        new Out().put(isomap.getPoints().y);
        isomap.getPoints().applyCentering();
        isomap.getPoints().openVisualization(edges);
    }
    
    public static void main(String[] args) {
        new IsomapSwissRollTest().shouldEmbedSwissroll();
    }
    
}
