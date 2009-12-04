package ch.akuhn.matrix;

import java.util.Arrays;

/** 
 * 
 * @author akuhn
 *
 */
public class DenseMatrix extends Matrix {

    private double[][] values;

    public DenseMatrix(double[][] values) {
        this.values = values;
    }

    public DenseMatrix(int rows, int columns) {
        values = new double[rows][columns];
    }

    @Override
    public double add(int row, int column, double value) {
        return values[row][column] += value;
    }

    @Override
    public int columnCount() {
        return values[0].length;
    }

    @Override
    public double get(int row, int column) {
        return values[row][column];
    }

    @Override
    public double put(int row, int column, double value) {
        return values[row][column] = value;
    }

    @Override
    public int rowCount() {
        return values.length;
    }

    @Override
    public int used() {
        // TODO Auto-generated method stub
        throw null;
    }

	@Override
	public void apply(Function f) {
		f.apply(values);
	}

    public Matrix kayNearestNeighbours(int k, double threshold) {
    	double[][] path = new double[values.length][values.length];
        for (int i = 0; i < path.length; i++) Arrays.fill(path[i], Double.POSITIVE_INFINITY);
        for (int i = 0; i < values.length; i++) {
            double[] minima = Arrays.copyOf(values[i], k + 1);
            Arrays.sort(minima);
            for (int j = k + 1; j < values.length; j++) {
                if (values[i][j] >= minima[k]) continue;
                int n = -Arrays.binarySearch(minima, values[i][j])-1;
                System.arraycopy(minima, n, minima, n+1, k-n);
                minima[n] = values[i][j];
            }
            double min = Math.max(minima[k], threshold);
            for (int j = k + 1; j < values.length; j++) {
                path[i][j] = (values[i][j] <= min) ? values[i][j] : Double.POSITIVE_INFINITY;
            }
        }
        return new DenseMatrix(path);
    }	
	
}
