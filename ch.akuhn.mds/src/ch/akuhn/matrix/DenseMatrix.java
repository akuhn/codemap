package ch.akuhn.matrix;

import java.util.Arrays;

public class DenseMatrix {

    public double[][] value;
    
    public DenseMatrix(int size) {
        this.value = new double[size][size];
    }

    public DenseMatrix(double[][] value) {
        this.value = value;
    }
    
    public DenseMatrix copy() {
        double[][] copy = new double[value.length][];
        for (int n = 0; n < copy.length; n++) copy[n] = value[n].clone();
        return new DenseMatrix(copy);
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
    
    public void multiplyWith(DenseMatrix other) {
        assert value.length == other.value.length;
        for (int i = 0; i < value.length; i++) {
            for (int j = 0; j < value.length; j++) {
                value[i][j] *= other.value[i][j];
            }
        }
    }

    public SymetricMatrix asSymetricMatrix() {
        return new SymetricMatrix(value);
    }
    
    
}
