package ch.akuhn.values;

public interface Value<V> {

    public V getValue();
    
    public ValueOrError<V> getValueOrError();
    
    public void setValue(V value);
    
    public void addDependent(ValueChangedListener dependent);
    
    public void removeDependent(ValueChangedListener dependent);
    
}