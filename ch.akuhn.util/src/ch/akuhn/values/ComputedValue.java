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
        if (value == MISSING) value = computeValue();
        return this.value;
    }

    @Override
    public void setValue(V value) {
        throw new UnsupportedOperationException();
    }

    public void resetValue() {
        value = isLazy ? missingValue() : computeValue();
        this.changed();
    }

    protected abstract V computeValue();

    @SuppressWarnings("unchecked")
    protected <A> A arg(int index) {
        return (A) arguments[index].getValue();
    }
    
    @Override
    public void valueChanged(EventObject event) {
        this.resetValue();
    }

}
