package ch.akuhn.values;

public class Values {

    public static boolean equal(Object a, Object b) {
        return a == null ? b == null : a.equals(b);
    }

    public static Error throwError(Throwable error) {
        if (error instanceof Error) throw (Error) error;
        if (error instanceof RuntimeException) throw (RuntimeException) error;
        throw new RuntimeException(error);
    }
    
    public static <V> Value<V> of(V object) {
        return new ReferenceValue<V>(object);
    }
    
}
