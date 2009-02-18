package ch.akuhn.util;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.IdentityHashMap;
import java.util.Map;

public class SizeOf {

    private static final String[] UNIT = { "bytes", "KB", "MB", "GB", "TB" };
    
    private int size = 0;

    @Override
    public String toString() {
        int order = (int) ((Math.log(size) /  Math.log(1024)) + 0.05);
        if (order == 0) return size + " bytes";
        return String.format("%.2f %s (%d bytes)", 1.0 / Math.pow(1024, order) * size, UNIT[order], size);
    }
    
    public SizeOf(int size) {
        this.size = size;
    }
    
    public static final SizeOf deepSizeOf(Object o) {
        Map<Object,Object> done = new IdentityHashMap<Object,Object>();
        return new SizeOf(deepSizeOf(o, done));
    }
        
    private static final int deepSizeOf(Object obj, Map<Object,Object> done) {
        if (obj == null || isInternedString(obj) || done.containsKey(obj)) return 0;
        done.put(obj, null);
        int size = sizeOf(obj);
        Class<?> type = obj.getClass();
        if (type.isArray()) {
            if (!type.getComponentType().isPrimitive()) {
                int len = Array.getLength(obj);
                for (int n = 0; n < len; n++) {
                    Object value = Array.get(obj, n);
                    size += deepSizeOf(value, done);
                }
            }
        }
        else { 
            while (type != null) {
                for (Field f: type.getDeclaredFields()) {
                    if (f.getType().isPrimitive()) continue;
                    if (Modifier.isStatic(f.getModifiers())) continue;
                    f.setAccessible(true);
                    Object value = getField(obj, f);
                    int sizeOf = deepSizeOf(value, done);
                    size += sizeOf;
                }
                type = type.getSuperclass();
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
    
    public static void main(String... args) {
        System.out.println(new SizeOf(1));
        System.out.println(new SizeOf(10));
        System.out.println(new SizeOf(100));
        System.out.println(new SizeOf(500));
        System.out.println(new SizeOf(600));
        System.out.println(new SizeOf(700));
        System.out.println(new SizeOf(800));
        System.out.println(new SizeOf(900));
        System.out.println(new SizeOf(1000));
        System.out.println(new SizeOf(10000));
        System.out.println(new SizeOf(100000));
        System.out.println(new SizeOf(1000000));
    }
    
}
