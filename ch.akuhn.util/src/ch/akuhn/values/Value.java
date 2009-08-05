package ch.akuhn.values;

import java.util.Collection;
import java.util.EventObject;

public class Value<V> implements ValueChangedListener {

    protected V value;
    private Collection<ValueChangedListener> listeners;

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
        this.changed(); // notify listeners outside locked section
    }

    public Value() {
        this.value = null;
    }
    
    public Value(V value) {
        this.value = value;
    }
    
    protected void changed() {
        EventObject event = new EventObject(this);
        for (ValueChangedListener each: listeners) each.valueChanged(event);
    }
    
    public void addDependent(ValueChangedListener listener) {
        listeners.add(listener); // TODO concurrency
    }

    public void removeDependent(ValueChangedListener listener) {
        listeners.remove(listener); // TODO concurrency
    }

    public void valueChanged(EventObject event) {
        // does nothing
    }
    
}
