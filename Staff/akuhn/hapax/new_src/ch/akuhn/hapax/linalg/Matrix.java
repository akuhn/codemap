package ch.akuhn.hapax.linalg;

public abstract class Matrix {

    public double add(int row, int column, double value) {
        return put(row, column, get(row, column) + value);
    }
    
    public abstract Iterable<Vector> columns();
    
    public abstract int columnSize();

    public double density() {
        return ((double) used()) / rowSize() / columnSize();
    }
    
    public abstract double get(int row, int column);
    
    public abstract double put(int row, int column, double value);
    
    public abstract Iterable<Vector> rows();
    
    public abstract int rowSize();
    
    public abstract int used();
    
}
