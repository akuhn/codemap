package ch.akuhn.values;

import java.util.EventObject;

public abstract class ComputedValue<V> extends ReferenceValue<V> {

    private static final Object MISSING = new Object();

    @SuppressWarnings("unchecked")
    private V missingValue() {
        return (V) MISSING;
    }
    
    private boolean isLazy = true;
    private Value<?>[] arguments;
    
    public ComputedValue() {
        this.value = missingValue();
        this.arguments = null;
    }

    public ComputedValue(Value<?>... arguments) {
        this.value = missingValue();
        this.arguments = arguments;
        for (Value<?> each: arguments) each.addDependent(this); 
    }
    
    @Override
    public V getValue() {
        if (value == MISSING) value = computeValue(new Arguments(arguments));
        return this.value;
    }

    @Override
    public void setValue(V value) {
        throw new UnsupportedOperationException();
    }

    public void resetValue() {
        value = isLazy ? missingValue() : computeValue(new Arguments(arguments));
        this.changed();
    }

    protected abstract V computeValue(Arguments arguments);

    @Override
    public void valueChanged(EventObject event) {
        this.resetValue();
    }

}
