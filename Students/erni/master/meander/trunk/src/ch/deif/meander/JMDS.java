package ch.deif.meander;

import ch.akuhn.hapax.index.LatentSemanticIndex;
import ch.akuhn.util.As;
import ch.deif.meander.mds.HitMDS;

public class JMDS {

    public double[] x, y;
    public double r0;
    public double r;

    public static JMDS fromCorrelationMatrix(LatentSemanticIndex index) {
        return new JMDS().compute(index, null);
    }

    public static JMDS fromCorrelationMatrix(LatentSemanticIndex index,
            Iterable<Location> matchingLocations) {
        return new JMDS().compute(index, matchingLocations);
    }

    private JMDS compute(LatentSemanticIndex index,
            Iterable<Location> matchingLocations) {
        assert matchingLocations == null
                || index.documents.size() == As.list(matchingLocations).size();

        int size = index.documents.size();
        x = new double[size];
        y = new double[size];
        double[][] input = new double[size][size];

        int row = 0;
        int col = 0;

        for (double corellation : index.documentCorrelations()) {
            input[row][col++] = corellation;
            if (col >= size) {
                col = 0;
                row++;
            }
        }

        double[][] result = new HitMDS().evaluate(input, -2);
        assert result[0].length == 2;

        int i = 0;
        for (double[] documentPosition : result) {
            x[i] = documentPosition[0];
            y[i] = documentPosition[1];
            i++;
        }

        return this;
    }
}
