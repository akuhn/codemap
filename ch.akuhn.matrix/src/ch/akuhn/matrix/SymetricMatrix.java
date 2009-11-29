package ch.akuhn.matrix;

import java.util.Arrays;

import ch.akuhn.linalg.Matrix;


/** Symmetric matrix without diagonal.
 * <P>
 * This class is not thread-safe. 
 *
 * @author Adrian Kuhn
 *
 */
public class SymetricMatrix extends Matrix {

    private static final int BIN = 100;
    public final double[][] values;

    public SymetricMatrix(int size) {
        values = new double[size][];
        for (int n = 0; n < values.length; n++) values[n] = new double[n];
    }

    private SymetricMatrix(double[][] values) {
        this.values = values;
    }

    public static SymetricMatrix fromJagged(double[][] jagged) {
        return new SymetricMatrix(jagged).clone();
    }
    
    public void apply(Function f) {
        for (double[] row: values) {
            for (int n = 0; n < row.length; n++) {
                row[n] = f.apply(row[n]);
            }
        }
    }
    
    public void fill(double constant) {
        for (double[] row: values) Arrays.fill(row, constant);
    }

    public int[] getHistogram() {
        double max = Double.MIN_VALUE;
        for (double[] row: values) {
            for (double each: row) {
                max = Math.max(max, each);
            }
        }
        max = 10; // FIXME
        int[] bins = new int[BIN];
        for (double[] row: values) {
            for (double each: row) {
                int index = (int) Math.floor(each / max * (BIN - 1));
                bins[Math.min(BIN - 1, index)]++;
            }
        }
        return bins;
    }

    public double max() {
        if (values.length == 0) return Double.NaN;
        double max = values[1][0];
        for (double[] row: values) {
            for (double each: row) {
                max = Math.max(max, each);
            }
        }
        return max;
    }
    
    public double min() {
        if (values.length == 0) return Double.NaN;
        double min = values[1][0];
        for (double[] row: values) {
            for (double each: row) {
                min = Math.min(min, each);
            }
        }
        return min;
    }

    public DenseMatrix asDenseMatrix() {
        double[][] dense = new double[values.length][values.length];
        for (int i = 0; i < dense.length; i++) {
            for (int j = 0; j < dense.length; j++) {
                dense[i][j] = i == j ? 0.0 : values[Math.max(i,j)][Math.min(i,j)];
            }
        }
        return new DenseMatrix(dense);
    }
    
    public int columnCount() {
        return rowCount();
    }

    public double get(int row, int column) {
        if (row == column) return 0.0;
        return row > column ? values[row][column] : values[column][row];
    }

    public double put(int row, int column, double value) {
        if (row == column) { assert value == 0.0; return value; }
        return row > column ? (values[row][column] = value) : (values[column][row] = value);
    }

    public int rowCount() {
        return values.length;
    }

    public double mean() {
        double sum = 0;
        int tally = 0;
        for (double[] row: values) {
            for (double each: row) {
                sum += each;
                tally++;
            }
        }
        int N = values.length;
        int n = N * (N - 1) / 2;
        assert tally == n;
        return sum / n;
    }
    
    public double[] columwiseMean() {
        int N = values.length;
        double data[] = new double[N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                data[i] += get(i,j);
            }
        }
        for (int n = 0; n < N; n++) data[n] /= N;
        return data;
    }

    public void applyMultiplication(double value) {
        for (double[] row: values) {
            for (int n = 0; n < row.length; n++) {
                row[n] *= value;
            }
        }
    }

    public double add(int row, int column, double value) {
        if (row == column) { /*assert value == 0.0;*/ return value; }
        return (row > column) ? (values[row][column] += value) : (values[column][row] += value);
    }

    public double[] mult(double[] vector) {
        assert vector.length == values.length;
        double[] mult = new double[vector.length];
        for (int i = 0; i < values.length; i++) {
            for (int j = 0; j < i; j++) {
                mult[i] += values[i][j] * vector[j];
                mult[j] += values[i][j] * vector[i];
            }
        }
        return mult;
    }
    
    @Override
    public SymetricMatrix clone() {
        double[][] clone = new double[values.length][];
        for (int i = 0; i < clone.length; i++) {
            assert values[i].length == i;
            clone[i] = values[i].clone();
        }
        return new SymetricMatrix(clone);
    }

    public static SymetricMatrix fromSquare(double[][] values) {
        // drops upper triangle!
        double[][] clone = new double[values.length][];
        for (int i = 0; i < clone.length; i++) {
            assert values[i].length == values.length;
            clone[i] = values[i].clone();
        }
        return new SymetricMatrix(clone);
    }

	@Override
	public int used() {
		// TODO Auto-generated method stub
		return 0;
	}
    
}
