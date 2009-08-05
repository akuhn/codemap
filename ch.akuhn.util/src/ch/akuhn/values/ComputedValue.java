package ch.akuhn.values;

import java.util.EventObject;

public abstract class ComputedValue<V> extends Value<V> {

    private static final Object MISSING = new Object();
    
    private boolean isLazy = true;
    
    @SuppressWarnings("unchecked")
    public ComputedValue() {
        this.value = (V) MISSING;
    }
    
    @Override
    public V getValue() {
        if (value == MISSING) value = computeValue();
        return this.value;
    }

    @Override
    public void setValue(V value) {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    public void resetValue() {
        value = isLazy ? (V) MISSING : computeValue();
        this.changed();
    }

    protected abstract V computeValue();

    @Override
    public void valueChanged(EventObject event) {
        this.resetValue();
    }

}
