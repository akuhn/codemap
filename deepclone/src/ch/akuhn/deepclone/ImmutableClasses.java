package ch.akuhn.deepclone;

import java.util.Collection;
import java.util.HashSet;

public class ImmutableClasses {

    private Collection<String> classNames = new HashSet<String>();
    
    public boolean contains(Class<?> type) {
	return type.isEnum() ||
		type.isAnnotation() ||
		type.isPrimitive() ||
		Class.class.isAssignableFrom(type) ||
		Number.class.isAssignableFrom(type) ||
		Throwable.class.isAssignableFrom(type) ||
		classNames.contains(type.getName());
    }
    
    public void add(String className) {
	classNames.add(className);
    }
    
    public void add(String... classNames) {
	for (String each: classNames) add(each);
    }

    public ImmutableClasses() {
	this.add("java.lang.Boolean",
		"java.lang.Character",
		"java.lang.Object",
		"java.lang.Void");
    }
    
}
