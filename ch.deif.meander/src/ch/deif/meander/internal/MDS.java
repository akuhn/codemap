package ch.deif.meander.internal;

import ch.akuhn.hapax.index.LatentSemanticIndex;
import ch.akuhn.mds.MultidimensionalScaling;
import ch.akuhn.util.As;
import ch.deif.meander.Location;

public class MDS {

	public double[] x, y;
	public double r0;
	public double r;

	public static MDS fromCorrelationMatrix(LatentSemanticIndex index) {
		return new MDS().compute(index, null);
	}

	public static MDS fromCorrelationMatrix(LatentSemanticIndex index, Iterable<Location> matchingLocations) {
		return new MDS().compute(index, matchingLocations);
	}

	private MDS compute(LatentSemanticIndex index, Iterable<Location> matchingLocations) {
		assert matchingLocations == null || index.documentCount() == As.list(matchingLocations).size();
		int size = index.documentCount();
		double[][] result = new MultidimensionalScaling()
			.similarities(index.documentCorrelation().asArray())
			.maxIterations(100)
			.run();
		assert result.length == 0 || result[0].length == 2;
		x = new double[size];
		y = new double[size];
		int i = 0;
		for (double[] documentPosition: result) {
			x[i] = documentPosition[0];
			y[i] = documentPosition[1];
			i++;
		}
		return this;
	}

	public void normalize() {
		double minX = 0;
		double maxX = 0;
		double minY = 0;
		double maxY = 0;
		for (double each: x) {
			minX = Math.min(minX, each);
			maxX = Math.max(maxX, each);
		}
		for (double each: y) {
			minY = Math.min(minY, each);
			maxY = Math.max(maxY, each);
		}
		double width = maxX - minX;
		double height = maxY - minY;
		for (int n = 0; n < x.length; n++) {
			x[n] = (x[n] - minX) / width * 0.8 + 0.1; // XXX
			y[n] = (y[n] - minY) / height * 0.8 + 0.1; // XXX
		}
	}	
	
}
