package ch.akuhn.matrix;

import static ch.akuhn.foreach.For.range;

import java.util.Arrays;

public class SymetricMatrix extends Matrix {

	double[][] values;
	
    public SymetricMatrix(int size) {
        values = new double[size][];
        for (int n: range(values.length))
            values[n] = new double[n + 1];
    }
    
    private SymetricMatrix(double[][] jagged) {
    	for (int n = 0; n < jagged.length; n++) assert jagged[n].length == n;
    	this.values = jagged;
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

	@Override
	public void apply(Function f) {
		for (double[] row: values) {
			for (int n = 0; n < row.length; n++) {
				row[n] = f.apply(row[n]);
			}
		}
	}

	public static Matrix fromSquare(double[][] matrix) {
		// TODO Auto-generated method stub
		return null;
	}

	public void applyMultiplication(double d) {
		// TODO Auto-generated method stub
	}

	public static Matrix fromJagged(double[][] values) {
		return new SymetricMatrix(values);
	}

	@Override
	public double[][] unwrap() {
		return values;
	}
	
	@Override
	public double[] rowwiseMean() {
		double[] mean = new double[rowCount()];
		for (int i = 0; i < values.length; i++) {
			for (int j = 0; j < i; j++) {
				mean[i] += values[i][j];
				mean[j] += values[i][j];
			}
		}
		for (int n = 0; n < mean.length; n++) mean[n] /= mean.length;
		return mean;
	}
	
    public Vector mult(Vector v) {
        assert v.size() == values.length;
        double[] mult = new double[v.size()];
        for (int i = 0; i < values.length; i++) {
            for (int j = 0; j < i; j++) {
                mult[i] += values[i][j] * v.get(j);
                mult[j] += values[i][j] * v.get(i);
            }
        }
        return Vector.wrap(mult);
    }
	
	
}
