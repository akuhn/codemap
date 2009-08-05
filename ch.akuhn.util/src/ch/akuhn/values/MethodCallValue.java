package ch.akuhn.values;

import java.beans.Expression;

public class MethodCallValue<V> extends ComputedValue<V> {

    private Object target;
    private String methodName;
    private Value<?>[] arguments;
    
    public MethodCallValue(Object target, String methodName, Value<?>... arguments) {
        this.target = target;
        this.methodName = methodName;
        this.arguments = arguments;
        for (Value<?> each: arguments) each.addDependent(this);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    protected V computeValue() {
        try {
            Object[] values = new Object[arguments.length];
            for (int i = 0; i < arguments.length; i++) {
                values[i] = arguments[i].getValue();
            }
            return (V) new Expression(target, methodName, values).getValue();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
    
}
