package ch.akuhn.matrix;
import java.util.Arrays;

public class DoubleMatrix {

    public final double[][] data;
    
    public DoubleMatrix(double[][] data) {
        this.data = data;
    }
    
    @Override
    public DoubleMatrix clone() {
        double[][] clone = new double[data.length][];
        for (int i = 0; i < clone.length; i++) clone[i] = data[i].clone();
        return new DoubleMatrix(clone);
    }
    
    public void fill(double value) {
        for (int i = 0; i < data.length; i++) Arrays.fill(data, value);
    }
    
    public DoubleMatrix kayNearestNeighbours(int k, double threshold) {
        double[][] path = new double[data.length][data.length];
        for (int i = 0; i < path.length; i++) Arrays.fill(path[i], Double.POSITIVE_INFINITY);
        for (int i = 0; i < data.length; i++) {
            double[] minima = Arrays.copyOf(data[i], k + 1);
            Arrays.sort(minima);
            for (int j = k + 1; j < data.length; j++) {
                if (data[i][j] >= minima[k]) continue;
                int n = -Arrays.binarySearch(minima, data[i][j])-1;
                System.arraycopy(minima, n, minima, n+1, k-n);
                minima[n] = data[i][j];
            }
            double min = Math.max(minima[k], threshold);
            for (int j = k + 1; j < data.length; j++) {
                path[i][j] = (data[i][j] <= min) ? data[i][j] : Double.POSITIVE_INFINITY;
            }
        }
        return new DoubleMatrix(path);
    }

    public DoubleMatrix undirected() {
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data.length; j++) {
                data[i][j] = Math.min(data[i][j], data[j][i]);
            }
        }
        return this;
    }

    public DoubleMatrix applyAllPairsShortestPath() {
        FloydWarshallAlgorithm.apply(data); // inplace
        return this;
    }
    
}
