package ch.akuhn.values;

import java.util.EventObject;

public abstract class ActionValue<V> extends ReferenceValue<V> {

    private Value<?>[] arguments;
    
    public ActionValue(Value<?>... arguments) {
        this.arguments = arguments;
        for (Value<?> each: arguments) each.addDependent(this); 
        this.value = performAction(new Arguments(arguments));
    }
    
    protected abstract V performAction(Arguments args);

    @Override
    public V getValue() {
        return this.value;
    }

    @Override
    public void setValue(V value) {
        throw new UnsupportedOperationException();
    }

    public void resetValue() {
        value = performAction(new Arguments(arguments));
        this.changed();
    }

    @Override
    public void valueChanged(EventObject event) {
        this.resetValue();
    }

}
