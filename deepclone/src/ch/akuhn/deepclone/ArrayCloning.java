package ch.akuhn.deepclone;

import java.lang.reflect.Array;
import java.util.Arrays;

public class ArrayCloning extends DeepCloning {

    private Class<?> type;

    public ArrayCloning(DeepClone cloner, Class<?> type) {
	super(cloner);
	this.type = type;
    }

    @Override
    public Object perform(Object instance) throws Exception {
	DeepCloning componentStrategy = cloner.getStrategy(type.getComponentType());
	int length = Array.getLength(instance);
	if (componentStrategy.isImmutable()) {
	    return Arrays.copyOf((Object[]) instance, length);
	} else {
	    Object clone = Array.newInstance(type.getComponentType(), length);
	    for (int n = 0; n < length; n++) {
		Array.set(clone, n, cloner.clone(Array.get(instance, n)));
	    }
	    return clone;
	}
    }


}
