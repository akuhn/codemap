package ch.akuhn.hapax.linalg;

public class DenseMatrix extends Matrix {

    private float[][] values;
    
    public DenseMatrix(float[][] values) {
        this.values = values;
    }
    
    public DenseMatrix(int rows, int columns) {
        values = new float[rows][columns];
    }

    @Override
    public void add(int row, int column, float value) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public int columnSize() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Iterable<Vector> columns() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public double density() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public float get(int row, int column) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void put(int row, int column, float value) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public int rowSize() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Iterable<Vector> rows() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int used() {
        // TODO Auto-generated method stub
        return 0;
    }
    
}
