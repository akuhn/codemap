package ch.akuhn.values;

public interface Value<V> {

    public V value();
    
    public Throwable error();
    
    public ImmutableValue<V> asImmutable();
    
    public void setValue(V value);
    
    public void setError(Throwable error);
    
    public boolean isError();
    
    public V getValue();
    
    public void addDependent(ValueChangedListener dependent);
    
    public void removeDependent(ValueChangedListener dependent);
    
}