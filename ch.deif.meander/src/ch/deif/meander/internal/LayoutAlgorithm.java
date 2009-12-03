package ch.deif.meander.internal;

import static java.lang.Math.max;
import static java.lang.Math.min;
import ch.akuhn.hapax.index.LatentSemanticIndex;
import ch.akuhn.hapax.linalg.Matrix;
import ch.akuhn.isomap.Isomap;
import ch.akuhn.mds.MultidimensionalScaling;
import ch.akuhn.org.ggobi.plugins.ggvis.Points;

public class LayoutAlgorithm {

    public double[] x, y;

    public static LayoutAlgorithm fromCorrelationMatrix(LatentSemanticIndex index) {
        return new LayoutAlgorithm().compute(index, null, null);
    }

    public static LayoutAlgorithm fromCorrelationMatrix(LatentSemanticIndex index, double[] x, double[] y) {
        return new LayoutAlgorithm().compute(index, x, y);
    }

    private LayoutAlgorithm compute(final LatentSemanticIndex index, double[] x0, double[] y0) {
        int len = index.documentCount();
        if (len == 0) {
            x = new double[] {};
            y = new double[] {};
            return this;
        }
        assert x0.length == len;
        assert y0.length == len;
        
        Isomap isomap = new Isomap(len) {
            Matrix corr = index.documentCorrelation();
            {
                // number of neighbors
                k=3;
            }
            
            @Override
            protected double dist(int i, int j) {
                return corr.get(i, j);
            }
        };
        isomap.run();
        Points points = isomap.getPoints();
        x = points.x;
        y = points.y;        
//        double[][] ps = new MultidimensionalScaling()
//            .similarities(index.documentCorrelation().asArray())
//            .initialConfiguration(points.x, points.y)
//            .run();
//        x = ps[0];
//        y = ps[1];
        return this;
    }

    public void normalize() {
        double minX = 0;
        double maxX = 0;
        double minY = 0;
        double maxY = 0;
        for (double each: x) {
            minX = min(minX, each);
            maxX = max(maxX, each);
        }
        for (double each: y) {
            minY = min(minY, each);
            maxY = max(maxY, each);
        }
        double width = maxX - minX;
        double height = maxY - minY;
        for (int n = 0; n < x.length; n++) {
            x[n] = (x[n] - minX) / width * 0.8 + 0.1; // XXX
            y[n] = (y[n] - minY) / height * 0.8 + 0.1; // XXX
        }
    }	
}
