package ch.akuhn.util;

public class Assert {

    public static final <T> T notNull(T t) {
        assert t != null;
        return t;
    }
    
    public static final String notEmpty(String string) {
        assert string != null && string.length() > 0;
        return string;
    }
    
}
