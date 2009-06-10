package ch.akuhn.deepclone;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.IdentityHashMap;
import java.util.Map;

import sun.reflect.ReflectionFactory;

public class DeepClone {
    
    private Map<Object,Object> done = new IdentityHashMap<Object,Object>();
    
    public Object clone(Object object) throws Exception {
	if (isImmutable(object.getClass())) return object;
	Object clone = done.get(object);
	if (clone != null) return clone;
	if (object.getClass().isArray()) return cloneArray(object);
	Constructor<?> c = ReflectionFactory.getReflectionFactory()
			.newConstructorForSerialization(object.getClass(), Object.class.getConstructor());
	c.setAccessible(true);
	clone = c.newInstance();
	for (Class<?> each = clone.getClass(); each != null; each = each.getSuperclass()) {
	    for (Field f: each.getDeclaredFields()) {
		if (Modifier.isStatic(f.getModifiers())) continue;
		f.setAccessible(true);
		if (Modifier.isTransient(f.getModifiers()) || isImmutable(f.getType())) {
		    f.set(clone, f.get(object));
		} else {
		    f.set(clone, clone(f.get(object)));
		}
	    }
	}
	done.put(object,clone);
	return clone;
    }

    private Object cloneArray(Object object) throws Exception {
	if (isImmutable(object.getClass().getComponentType())) {
	    return Arrays.copyOf((Object[]) object, Array.getLength(object));
	} else {
	    Object clone = Array.newInstance(object.getClass().getComponentType(), Array.getLength(object));
	    for (int n = 0; n < Array.getLength(object); n++) {
		Array.set(clone, n, clone(Array.get(object, n)));
	    }
	    return clone;
	}
    }

    private boolean isImmutable(Class<?> jclass) {
	return jclass.isPrimitive() || 
		jclass == Boolean.class ||
		jclass == Byte.class ||
		jclass == Short.class ||
		jclass == Integer.class ||
		jclass == Long.class ||
		jclass == Double.class ||
		jclass == Float.class ||
		jclass == String.class ||
		jclass == Void.class;
    }

    @SuppressWarnings("unchecked")
    public <T> T deepClone(T original) {
	try {
	    return (T) clone(original);
        } catch (Exception ex) {
	    throw new RuntimeException(ex);
        }
    }
    
}
