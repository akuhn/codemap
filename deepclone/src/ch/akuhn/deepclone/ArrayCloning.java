package ch.akuhn.deepclone;

import java.lang.reflect.Array;

public class ArrayCloning implements DeepCloneStrategy {

    private Class<?> componentType;

    public ArrayCloning(Class<?> type) {
        this.componentType = type.getComponentType();
    }

    @Override
    public Object makeClone(Object original, CloneFactory delegate) throws Exception {
        int length = Array.getLength(original);
        Object clone = Array.newInstance(componentType, length);
        if (componentType.isPrimitive()) {
            System.arraycopy(original, 0, clone, 0, length);
        } 
        else for (int n = 0; n < length; n++) {
            Array.set(clone, n, delegate.clone(Array.get(original, n)));
        }
        return clone;
    }

}
