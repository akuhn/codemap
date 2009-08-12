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
    public Throwable error() {
        return error;
    }

    @Override
    public V getValue() {
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
    public V value() {
        return isError() ? null : value;
    }

}
