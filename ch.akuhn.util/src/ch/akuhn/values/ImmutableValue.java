package ch.akuhn.values;

public class ImmutableValue<V> implements Value<V> {

    private final Throwable error;
    private final V value;

    public ImmutableValue(V value, Throwable error) {
        this.error = error;
        this.value = value;
    }

    @Override
    public void addDependent(ValueChangedListener dependent) {
        // ignore
    }

    @Override
    public ImmutableValue<V> asImmutable() {
        return this;
    }

    @Override
    public Throwable getError() {
        return error;
    }

    @Override
    public V getValueOrFail() {
        if (isError()) throw Values.throwError(error);
        return value;
    }

    @Override
    public boolean isError() {
        return error != null;
    }

    @Override
    public void removeDependent(ValueChangedListener dependent) {
        // ignore
    }

    @Override
    public void setError(Throwable error) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setValue(Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public V getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        return error != null ? error.getClass().hashCode() : value != null ? value.hashCode() : 0;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object obj) {
        if (!(obj instanceof ImmutableValue)) return false;
        ImmutableValue other = (ImmutableValue) obj;
        if (error != null || other.error != null) return Values.equal(error, other.error);
        return Values.equal(value, other.value);
    }
    
}
