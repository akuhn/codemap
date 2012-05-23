//  Copyright (c) 1998-2008 Adrian Kuhn <akuhn(a)students.unibe.ch>
//  
//  This file is part of ch.akuhn.util.
//  
//  ch.akuhn.util is free software: you can redistribute it and/or modify it
//  under the terms of the GNU Lesser General Public License as published by the
//  Free Software Foundation, either version 3 of the License, or (at your
//  option) any later version.
//  
//  ch.akuhn.util is distributed in the hope that it will be useful, but
//  WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
//  or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
//  License for more details.
//  
//  You should have received a copy of the GNU Lesser General Public License
//  along with ch.akuhn.util. If not, see <http://www.gnu.org/licenses/>.
//  

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
