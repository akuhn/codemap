package ch.akuhn.util;

public class Assert {

    // TODO moar methods to check range of numebrs
    
    public static final <T> T notNull(T t) {
        if (t == null) throw new AssertionError();
        return t;
    }

    public static final void notNull(Object object, Object... more) {
        if (object == null) throw new AssertionError();
        for (Object each: more) if (each == null) throw new AssertionError();
    }
    
    public static final String notEmpty(String string) {
        assert string != null && string.length() > 0;
        return string;
    }
    
}
