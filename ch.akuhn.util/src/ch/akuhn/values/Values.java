package ch.akuhn.values;

public class Values {

    public static boolean equal(Object a, Object b) {
        return a == null ? b == null : a.equals(b);
    }

    public static Error throwError(Throwable error) {
        // TODO Auto-generated method stub
        return null;
    }
    
}
