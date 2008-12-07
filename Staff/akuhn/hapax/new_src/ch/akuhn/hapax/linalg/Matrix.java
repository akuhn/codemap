package ch.akuhn.hapax.linalg;

public abstract class Matrix {

    public abstract void put(int row, int column, float value);
    
    public abstract void add(int row, int column, float value);
    
    public abstract float get(int row, int column);

    public abstract int rowSize();
    
    public abstract int columnSize();
    
    public abstract Iterable<Vector> rows();
    
    public abstract Iterable<Vector> columns();
    
    public abstract double density();
    
    public abstract int used();
    
}
