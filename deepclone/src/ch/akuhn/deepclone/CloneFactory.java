package ch.akuhn.deepclone;

import static ch.akuhn.deepclone.DeepCloning.IMMUTABLE;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

public class CloneFactory {
    
    private Map<Class<?>,DeepCloning> strategies = new HashMap<Class<?>,DeepCloning>();
    private Map<Object,Object> done = new IdentityHashMap<Object,Object>();
    
    public DeepCloning getStrategy(Class<?> type) {
	if (type.isPrimitive()) return IMMUTABLE;
	DeepCloning strategy = strategies.get(type);
	if (strategy != null) return strategy;
	strategies.put(type, strategy = makeStrategy(type));
	return strategy;
    }
    
    private DeepCloning makeStrategy(Class<?> type) {
	if (isPrimitiveImmutable(type)) return IMMUTABLE;
	if (type.isArray()) return new ArrayCloning(this, type);
	return new UnsafeCloning(this, type);
    }

    protected Object clone(Object object) throws Exception {
	if (object == null) return null;
	Object clone = done.get(object);
	DeepCloning strategy = getStrategy(object.getClass());
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
    
    public void reset() {
	done.clear();
    }
    
    public void dontClone(Class<?> type) {
	strategies.put(type, IMMUTABLE);
    }
    
    public void useUnsafeClone(Class<?> type) {
	strategies.put(type, new UnsafeCloning(this, type));
    }
    
    public void useCloneable(Class<?> type) {
	strategies.put(type, new DefaultCloning(this, type));
    }
    
    public static boolean isPrimitiveImmutable(Class<?> type) {
	return type.isEnum() ||
		type.isAnnotation() ||
		type == String.class ||
		type == Void.class ||
		type == Boolean.class ||
		Number.class.isAssignableFrom(type);
    }
    
}
