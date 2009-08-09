package ch.akuhn.values;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EventObject;

public abstract class AbstractValue<V> implements Value<V> {

    private Collection<ValueChangedListener> listeners = new ArrayList<ValueChangedListener>();

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


    @Override
    public V getValue() {
        return getValueOrError().getValueOrNull();
    }

}
