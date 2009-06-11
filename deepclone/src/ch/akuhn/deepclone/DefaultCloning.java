package ch.akuhn.deepclone;

import java.lang.reflect.Method;

public class DefaultCloning extends DeepCloning {

    private Method cloneMethod;

    public DefaultCloning(CloneFactory cloner, Class<?> type) {
	super(cloner);
	this.cloneMethod = findCloneMethod(type);
    }

    private static Method findCloneMethod(Class<?> type) {
	try {
	    Method m = type.getMethod("clone");
	    m.setAccessible(true);
	    return m;
        } catch (SecurityException ex) {
	    throw new RuntimeException(ex);
        } catch (NoSuchMethodException ex) {
	    throw new RuntimeException(ex);
        }
    }

    @Override
    public Object perform(Object instance) throws Exception {
	return cloneMethod.invoke(instance);
    }
    
    
    
}
