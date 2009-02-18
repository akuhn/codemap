package ch.akuhn.util;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;


public class Debug {

    public static final int deepSizeOf(Object o) {
        Set<Object> done = new HashSet<Object>();
        done.add(o);
        return deepSizeOf(o, done);
    }
        
    private static final int deepSizeOf(Object o, Set<Object> done) {
        int size = sizeOf(o);
        if (o.getClass().isArray()) {
            if (o.getClass().getComponentType().isPrimitive()) return size;
            for (int n = 0; n < Array.getLength(o); n++) {
                Object value = Array.get(o, n);
                if (value == null) continue;
                if (isInternedString(value)) continue;
                if (!done.add(value)) continue;
                size += deepSizeOf(value, done);
            }
            return size;
        }
        for (Class<?> aClass = o.getClass(); aClass != null; aClass = aClass.getSuperclass()) {
            for (Field f: aClass.getDeclaredFields()) {
                if (f.getType().isPrimitive()) continue;
                if (Modifier.isStatic(f.getModifiers())) continue;
                f.setAccessible(true);
                Object value = getField(o, f);
                if (value == null) continue;
                if (isInternedString(value)) continue;
                if (!done.add(value)) continue;
                size += deepSizeOf(value, done);
            }
        }
        return size;
    }

    private static Object getField(Object o, Field f) {
        try {
            return f.get(o);
        } catch (IllegalArgumentException ex) {
            throw Throw.exception(ex);
        } catch (IllegalAccessException ex) {
            throw Throw.exception(ex);
        }
    }
    
    private static boolean isInternedString(Object value) {
        if (!(value instanceof String)) return false;
        String string = (String) value;
        return string == string.intern();
    }

    /**
     * 
     * @see http://kohlerm.blogspot.com/2008/12/how-much-memory-is-used-by-my-java.html
     */
    public static final int sizeOf(Object o) {
        // Arrays of boolean, byte, char, short, int: 2 * 4 (Object header) + 4 (length-field) + sizeof(primitiveType) * length -> align result up to a multiple of 8
        // Arrays of objects: 2 * 4 (Object header) + 4 (length-field) + 4 * length -> align result up to a multiple of 8
        // Arrays of longs and doubles: 2 * 4 (Object header) + 4 (length-field) + 4 (dead space due to alignment restrictions) + 8 * length
        // java.lang.Object: 2 * 4 (Object header) 
        // other objects: sizeofSuperClass + 8 * nrOfLongAndDoubleFields + 4 * nrOfIntFloatAndObjectFields + 2 * nrOfShortAndCharFields + 1 * nrOfByteAndBooleanFields -> align result up to a multiple of 8
        if (o.getClass().isArray()) {
            int componentSize = sizeOfField(o.getClass().getComponentType());
            return align((2 * 4) + 4 + (componentSize * Array.getLength(o)));
        }
        else {
            return sizeOfInstance(o.getClass());
        }
    }

    private static int sizeOfInstance(Class<?> type) {
        if (type == Object.class) return 8;
        int size = sizeOfInstance(type.getSuperclass());
        for (Field f: type.getDeclaredFields()) {
            if (Modifier.isStatic(f.getModifiers())) continue;
            size += sizeOfField(f.getType());
        }
        return align(size);
    }

    private static int align(int size) {
        return (size % 8) == 0 ? size : size - (size % 8) + 8;
    }

    private static int sizeOfField(Class<?> type) {
        if (type == Boolean.TYPE) return 1;
        if (type == Byte.TYPE) return 1;
        if (type == Character.TYPE) return 2;
        if (type == Short.TYPE) return 2;
        if (type == Integer.TYPE) return 4;
        if (type == Long.TYPE) return 8;
        if (type == Float.TYPE) return 4;
        if (type == Double.TYPE) return 5;
        return 4;
    }
    
}
