package ch.akuhn.values;

public class Values {

    public static boolean eq(Object a, Object b) {
        return a == null ? b == null : a.equals(b);
    }
    
}
