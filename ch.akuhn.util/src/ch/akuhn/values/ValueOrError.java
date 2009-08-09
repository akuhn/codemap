package ch.akuhn.values;

import java.util.concurrent.ExecutionException;


public final class ValueOrError<V> {

    private final V value;
    private final Throwable error;
    
    public ValueOrError(V value, Throwable error) {
        this.value = value;
        this.error = error;
    }

    public V getValueOrNull() {
        return value;
    }
    
    public V getValue() {
        if (error != null) throw throwError();
        return value;
    }
    
    public boolean hasValue() {
        return error == null;
    }
    
    public boolean hasError() {
        return error != null;
    }

    private Error throwError() {
        if (error instanceof Error) throw ((Error) error);
        if (error instanceof RuntimeException) throw ((RuntimeException) error);
        if (error instanceof ExecutionException) throw new RuntimeException(error.getCause());
        throw new RuntimeException(error);
    }
    
}
