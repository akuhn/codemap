package ch.akuhn.deepclone;

import static ch.akuhn.deepclone.DeepCloneStrategy.IMMUTABLE;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

import sun.reflect.ReflectionFactory;

public class DeepClone {
    
    private Map<Class<?>,DeepCloneStrategy> types = new HashMap<Class<?>,DeepCloneStrategy>();
    private Map<Object,Object> done = new IdentityHashMap<Object,Object>();
    
    public DeepCloneStrategy getStrategy(Class<?> type) {
	if (type.isPrimitive()) return IMMUTABLE;
	DeepCloneStrategy strategy = types.get(type);
	if (strategy != null) return strategy;
	types.put(type, strategy = makeStrategy(type));
	return strategy;
    }
    
    private DeepCloneStrategy makeStrategy(Class<?> type) {
	if (Number.class.isAssignableFrom(type)) return IMMUTABLE;
	if (Void.class == type) return IMMUTABLE;
	if (String.class == type) return IMMUTABLE;
	if (type.isArray()) return new ArrayCloning(this, type);
	return new UnsafeCloning(this, type);
    }

    public Object clone(Object object) throws Exception {
	Object clone = done.get(object);
	DeepCloneStrategy strategy = getStrategy(object.getClass());
	clone = strategy.perform(object);
	done.put(object, clone);
	return clone;
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
