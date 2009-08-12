package ch.akuhn.values;

import java.util.Collection;
import java.util.EventObject;
import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class AbstractValue<V> implements Value<V> {

    private Collection<ValueChangedListener> listeners = 
            new ConcurrentLinkedQueue<ValueChangedListener>();

    protected final void changed() {
        EventObject event = new EventObject(this);
        for (ValueChangedListener each: listeners) {
            each.valueChanged(event);
        }
    }
    
    
    public final void addDependent(ValueChangedListener listener) {
        listeners.add(listener); 
    }

    public final void removeDependent(ValueChangedListener listener) {
        listeners.remove(listener); 
    }

    @Override
    public V getValue() {
        if (isError()) throw Values.throwError(error());
        return value();
    }
    
    @Override
    public boolean isError() {
        return error() != null;
    }
    
    @Override
    public ImmutableValue<V> asImmutable() {
        return new ImmutableValue<V>(value(), error());
    }
    
    @Override
    public void setError(Throwable error) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void setValue(V value) {
        throw new UnsupportedOperationException();
    };
    
}
