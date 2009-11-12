package ch.akuhn.matrix;

import java.util.Arrays;


/** Symmetric matrix without diagonal.
 * <P>
 * This class is not thread-safe. 
 *
 * @author Adrian Kuhn
 *
 */
public class SymetricMat {

    private static final int BIN = 100;
    public final double[][] value;

    public SymetricMat(int size) {
        value = new double[size][];
        for (int n = 0; n < value.length; n++) value[n] = new double[n];
    }

    public SymetricMat(double[][] matrix) {
        value = new double[matrix.length][];
        for (int n = 0; n < value.length; n++) value[n] = Arrays.copyOf(matrix[n], n);
    }

    public void apply(Function f) {
        for (double[] row: value) {
            for (int n = 0; n < row.length; n++) {
                row[n] = f.apply(row[n]);
            }
        }
    }
    
    public void fill(double constant) {
        for (double[] row: value) Arrays.fill(row, constant);
    }

    public int[] getHistogram() {
        double max = Double.MIN_VALUE;
        for (double[] row: value) {
            for (double each: row) {
                max = Math.max(max, each);
            }
        }
        max = 2.5; // FIXME
        int[] bins = new int[BIN];
        for (double[] row: value) {
            for (double each: row) {
                int index = (int) Math.floor(each / max * (BIN - 1));
                bins[Math.min(BIN - 1, index)]++;
            }
        }
        return bins;
    }

    public double max() {
        if (value.length == 0) return Double.NaN;
        double max = value[1][0];
        for (double[] row: value) {
            for (double each: row) {
                max = Math.max(max, each);
            }
        }
        return max;
    }
    
    public double min() {
        if (value.length == 0) return Double.NaN;
        double min = value[1][0];
        for (double[] row: value) {
            for (double each: row) {
                min = Math.min(min, each);
            }
        }
        return min;
    }
    

}
