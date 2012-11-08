
package ch.akuhn.util;

import java.lang.reflect.Constructor;
import java.util.HashMap;

/**
 * A map that knows how to initialize missing mapping.
 * 
 */
public abstract class CacheMap<K,V> extends HashMap<K,V> {

    @SuppressWarnings("unchecked")
    private static <A,T> T createInstanceOf(Class<T> instanceClass, A argument) {
        try {
            Constructor<T>[] inits = (Constructor<T>[]) instanceClass.getDeclaredConstructors();
            for (Constructor<T> each : inits) {
                Class<?>[] params = each.getParameterTypes();
                if (params.length == 1 && params[0].isAssignableFrom(argument.getClass())) {
                    each.setAccessible(true);
                    return each.newInstance(argument);
                }
            }
            return instanceClass.newInstance();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static <A,T> CacheMap<A,T> instances(final Class<? extends T> instanceClass) {
        return new CacheMap<A,T>() {
            @Override
            public T initialize(A key) {
                return createInstanceOf(instanceClass, key);
            }
        };
    }

    public CacheMap() {
        super();
    }

    public CacheMap(HashMap<? extends K,? extends V> m) {
        super(m);
    }

    public CacheMap(int initialCapacity) {
        super(initialCapacity);
    }

    public CacheMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    @SuppressWarnings("unchecked")
    @Override
    public V get(Object key) {
        V value = super.get(key);
        if (value == null) {
            K k = (K) key;
            super.put(k, value = initialize(k));
        }
        return value;
    }

    public abstract V initialize(K key);

}
