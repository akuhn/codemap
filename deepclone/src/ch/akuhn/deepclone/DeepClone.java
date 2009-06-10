package ch.akuhn.deepclone;

import static ch.akuhn.deepclone.DeepCloning.IMMUTABLE;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

public class DeepClone {
    
    private Map<Class<?>,DeepCloning> types = new HashMap<Class<?>,DeepCloning>();
    private Map<Object,Object> done = new IdentityHashMap<Object,Object>();
    
    public DeepCloning getStrategy(Class<?> type) {
	if (type.isPrimitive()) return IMMUTABLE;
	DeepCloning strategy = types.get(type);
	if (strategy != null) return strategy;
	types.put(type, strategy = makeStrategy(type));
	return strategy;
    }
    
    private DeepCloning makeStrategy(Class<?> type) {
	if (Number.class.isAssignableFrom(type)) return IMMUTABLE;
	if (Void.class == type) return IMMUTABLE;
	if (String.class == type) return IMMUTABLE;
	if (type.isArray()) return new ArrayCloning(this, type);
	return new UnsafeCloning(this, type);
    }

    public Object clone(Object object) throws Exception {
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
    
}
