package ch.akuhn.hapax.linalg;

import static ch.akuhn.util.Interval.range;

public class SymetricMatrix extends Matrix {

    protected double[][] values;

    public SymetricMatrix(int size) {
        values = new double[size][];
        for (int n: range(values.length))
            values[n] = new double[n + 1];
    }

    @Override
    public Iterable<Vector> columns() {
        throw new UnsupportedOperationException();
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
    public Iterable<Vector> rows() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int rowCount() {
        return values.length;
    }

    @Override
    public int used() {
        throw new UnsupportedOperationException();
    }

	public double[][] asArray() {
		double[][] result = new double[values.length][values.length];
		for (int x = 0; x < result.length; x++) {
			for (int y = 0; y < result.length; y++) {
				result[x][y] = 1.0 - get(x,y);
			}
		}
		return result;
	}
}
