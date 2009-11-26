package ch.akuhn.hapax.linalg;

import static ch.akuhn.foreach.For.range;

import java.util.Arrays;

public class SymetricMatrix extends Matrix {

    public double[][] values;

    public SymetricMatrix(int size) {
        values = new double[size][];
        for (int n: range(values.length))
            values[n] = new double[n + 1];
    }

    @Override
    public int columnCount() {
        return rowCount();
    }

    @Override
    public double get(int row, int column) {
        return row > column ? values[row][column] : values[column][row];
    }

    @Override
    public double put(int row, int column, double value) {
        return row > column ? (values[row][column] = value) : (values[column][row] = value);
    }

    @Override
    public int rowCount() {
        return values.length;
    }

    @Override
    public int used() {
        throw new UnsupportedOperationException();
    }

    public void fill(double constant) {
        for (double[] row: values) Arrays.fill(row, constant);
    }

}
