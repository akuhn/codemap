package ch.deif.meander.internal;

import org.codemap.hitmds.Hitmds2;

import ch.akuhn.hapax.index.LatentSemanticIndex;
import ch.akuhn.util.As;
import ch.deif.meander.Location;

public class JMDS {

	public double[] x, y;
	public double r0;
	public double r;

	public static JMDS fromCorrelationMatrix(LatentSemanticIndex index) {
		return new JMDS().compute(index, null);
	}

	public static JMDS fromCorrelationMatrix(LatentSemanticIndex index, Iterable<Location> matchingLocations) {
		return new JMDS().compute(index, matchingLocations);
	}

	private JMDS compute(LatentSemanticIndex index, Iterable<Location> matchingLocations) {
		assert matchingLocations == null || index.documents.size() == As.list(matchingLocations).size();

		int size = index.documents.size();
		x = new double[size];
		y = new double[size];
		double[][] input = new double[size][size];

		int row = 0;
		int col = 0;

		for (double corellation: index.documentCorrelations()) {
			input[row][col++] = corellation;
			if (col >= size) {
				col = 0;
				row++;
			}
		}

		double[][] result = new Hitmds2().run(input);
		assert result.length == 0 || result[0].length == 2;

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
