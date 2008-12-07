package ch.akuhn.hapax.linalg;

public abstract class Matrix {

    public abstract double put(int row, int column, double value);
    
    public abstract double add(int row, int column, double value);
    
    public abstract double get(int row, int column);

    public abstract int rowSize();
    
    public abstract int columnSize();
    
    public abstract Iterable<Vector> rows();
    
    public abstract Iterable<Vector> columns();
    
    public double density() {
        return ((double) used()) / rowSize() / columnSize();
    }
    
    public abstract int used();
    
}
