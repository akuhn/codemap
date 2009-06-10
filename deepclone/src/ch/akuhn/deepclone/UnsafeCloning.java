package ch.akuhn.deepclone;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;

import sun.reflect.ReflectionFactory;

public class UnsafeCloning extends DeepCloneStrategy {

//    private Class<?> type;
    private Constructor<?> constructor;
    private Collection<Field> fields;
    
    public UnsafeCloning(DeepClone cloner, Class<?> type) {
	super(cloner);
//	this.type = type;
	this.constructor = makeConstructor(type);
	this.fields = makeFields(type);
    }

    private static Collection<Field> makeFields(Class<?> type) {
	Collection<Field> fields = new ArrayList<Field>();
	for (Class<?> each = type; each != null; each = each.getSuperclass()) {
	    for (Field f: each.getDeclaredFields()) {
		if (Modifier.isStatic(f.getModifiers())) continue;
		f.setAccessible(true);
		fields.add(f);
	    }
	}
	return fields;
    }

    private static Constructor<?> makeConstructor(Class<?> type) {
	try {
	    Constructor<?> constructor = ReflectionFactory.getReflectionFactory()
	    		.newConstructorForSerialization(type, Object.class.getConstructor());
	    constructor.setAccessible(true);
	    return constructor;
        } catch (SecurityException ex) {
	    throw new RuntimeException(ex);
        } catch (NoSuchMethodException ex) {
	    throw new RuntimeException(ex);
        }
    }
    
    @Override
    public Object perform(Object object) throws Exception {
	if (object == null) return null;
	Object clone = constructor.newInstance();
	for (Field f: fields) {
	    Object value = f.get(object);
	    if (needsClone(f)) value = cloner.clone(value);
	    f.set(clone, value);
	}
	return clone;
    }

    private static boolean needsClone(Field f) {
	return !Modifier.isTransient(f.getModifiers());
    }

}
