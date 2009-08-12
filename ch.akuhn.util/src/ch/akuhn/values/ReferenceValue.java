package ch.akuhn.values;

import java.util.EventObject;


public class ReferenceValue<V> extends AbstractValue<V> implements ValueChangedListener {

    protected V value;

    public void setValue(V value) {
        V prev = this.value;
        this.value = value;
        if (!Values.equal(prev, value)) this.changed();
    }

    public ReferenceValue() {
        this.value = null;
    }
    
    public ReferenceValue(V value) {
        this.value = value;
    }
    
    @Override
    public Throwable error() {
        return null;
    }

    @Override
    public void setError(Throwable error) {
        throw new UnsupportedOperationException();
    }

    @Override
    public V value() {
        return value;
    }

    @Override
    public void valueChanged(EventObject event) {
        // ignore
    }
    
}
