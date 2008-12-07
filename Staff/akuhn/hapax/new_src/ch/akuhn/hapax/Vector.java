package ch.akuhn.hapax;

public abstract class Vector {

    public abstract void put(int index, float value);
    
    public abstract void add(int index, float value);
    
    public abstract float get(int row, int column);

    public abstract int size();
    
    public abstract double density();
    
    public abstract int used();    
    
}
