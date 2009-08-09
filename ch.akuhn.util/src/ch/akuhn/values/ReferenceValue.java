package ch.akuhn.values;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EventObject;

public class ReferenceValue<V> implements ValueChangedListener, Value<V> {

    protected V value;
    private Collection<ValueChangedListener> listeners = new ArrayList<ValueChangedListener>();

    public V getValue() {
        return value;
    }
    
    public V awaitValue() {
        return getValue();
    }

    public void setValue(V value) {
        V prev = this.value;
        this.value = value;
        if (!eq(prev, value)) this.changed();
    }

    public ReferenceValue() {
        this.value = null;
    }
    
    public ReferenceValue(V value) {
        this.value = value;
    }
    
    protected boolean eq(Object prev, Object newValue) {
        return prev == null ? newValue == null : prev.equals(newValue);
    }
    
    protected void changed() {
        EventObject event = new EventObject(this);
        for (ValueChangedListener each: listeners) {
            each.valueChanged(event);
        }
    }
    
    
    public void addDependent(ValueChangedListener listener) {
        listeners.add(listener); // TODO concurrency
    }

    public void removeDependent(ValueChangedListener listener) {
        listeners.remove(listener); // TODO concurrency
    }

    public void valueChanged(EventObject event) {
        valueChanged(event);
    }

    @Override
    public ValueOrError<V> getValueOrError() {
        return new ValueOrError(value, null);
    }
    
}
