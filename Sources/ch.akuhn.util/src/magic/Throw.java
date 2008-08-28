package magic;

/** Throws checked exceptions without the hassle of having to declare them.
 * 
 * @see http://blog.uncommons.org/2008/08/26/more-stupid-java-tricks/
 * 
 */
public class Throw {

    @SuppressWarnings("deprecation")
    public static RuntimeException checked(Throwable cause) {
        if (cause instanceof RuntimeException) throw (RuntimeException) cause;
        if (cause instanceof Error) throw (Error) cause;
        Thread.currentThread().stop(cause);
        throw unchecked(cause);
    }
    
    public static RuntimeException unchecked(Throwable cause) {
        if (cause instanceof RuntimeException) throw (RuntimeException) cause;
        if (cause instanceof Error) throw (Error) cause;
        throw new RuntimeException(cause);
    }
    
}
