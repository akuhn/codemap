
package ch.akuhn.util;


/**
 * Methods for static import.
 * 
 * @author Adrian Kuhn
 * 
 */
@SuppressWarnings("unchecked")
public abstract class Extensions {

    // private final static Object NONE = new Object();

    public static Class<?> leastUpperBound(Class<?> initial, Object... os) {
        Class $ = initial;
        for (Object o : os) {
            while (!$.isAssignableFrom(o.getClass())) {
                $ = $.getSuperclass();
                if ($ == null) return Object.class;
            }
        }
        return $;
    }

    private Extensions() {
        throw new AssertionError();
    }

}
