package ch.akuhn.values;

public interface Value<V> {

    public V getValue();
    
    public Throwable getError();
    
    public ImmutableValue<V> asImmutable();
    
    public void setValue(V value);
    
    public void setError(Throwable error);
    
    public boolean isError();
    
    public V getValueOrFail();
    
    public void addDependent(ValueChangedListener dependent);
    
    public void removeDependent(ValueChangedListener dependent);
    
}