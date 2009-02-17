package ch.akuhn.hapax.linalg;

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
    public Iterable<Vector> columns() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int columnSize() {
        return values[0].length;
    }

    @Override
    public double density() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public double get(int row, int column) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public double put(int row, int column, double value) {
        return values[row][column] = value;
    }

    @Override
    public Iterable<Vector> rows() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int rowSize() {
        return values.length;
    }

    @Override
    public int used() {
        // TODO Auto-generated method stub
        return 0;
    }

}
