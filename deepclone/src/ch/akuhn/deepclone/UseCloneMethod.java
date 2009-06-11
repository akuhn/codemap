package ch.akuhn.deepclone;

import java.lang.reflect.Method;

public class UseCloneMethod implements DeepCloneStrategy {

    private static final Method cloneMethod = getCloneMethod();

    private static Method getCloneMethod() {
	try {
	    Method m = Object.class.getDeclaredMethod("clone");
	    m.setAccessible(true);
	    return m;
        } catch (Exception ex) {
	    throw new RuntimeException(ex);
        }
    }

    @Override
    public Object makeClone(Object instance, CloneFactory delegate) throws Exception {
	return cloneMethod.invoke(instance);
    }
    
    
    
}
