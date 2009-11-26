package ch.akuhn.linalg;

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

}
