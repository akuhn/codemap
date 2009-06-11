package ch.akuhn.deepclone;

import static ch.akuhn.deepclone.DeepCloneStrategy.IMMUTABLE;

import java.util.HashMap;
import java.util.Map;

public class DeepCloneStrategyCache {

    private ImmutableClasses immutables; 
    private Map<Class<?>,DeepCloneStrategy> cache; 
    
    public DeepCloneStrategyCache() {
	this.immutables = new ImmutableClasses();
	this.cache = new HashMap<Class<?>,DeepCloneStrategy>();
    }
    
    public DeepCloneStrategy lookup(Object object) {
	return lookup(object.getClass());
    }
    
    public DeepCloneStrategy lookup(Class<?> type) {
        if (type.isPrimitive()) return IMMUTABLE;
	DeepCloneStrategy result = cache.get(type);
	if (result != null) return result;
	cache.put(type, result = makeStrategy(type));
	return result;
    }
    
    private DeepCloneStrategy makeStrategy(Class<?> type) {
	if (immutables.contains(type)) return IMMUTABLE;
	if (type.isArray()) return new ArrayCloning(type);
	return new UnsafeCloning(type);
    }

}
