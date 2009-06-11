package ch.akuhn.deepclone;

import java.lang.reflect.Array;

/** Deep-clones array instances, both with primitive and object components.
 * 
 * @author Adrian Kuhn
 *
 */
public class ArrayCloning implements DeepCloneStrategy {

    private Class<?> componentType;

    public ArrayCloning(Class<?> type) {
        this.componentType = type.getComponentType();
    }

    @Override
    public Object makeClone(Object original, CloneFactory delegate) throws Exception {
        int length = Array.getLength(original);
        Object clone = delegate.getCachedClone(original);
        if (clone != null) return clone;
        clone = Array.newInstance(componentType, length);
        delegate.putCachedClone(original, clone);
        cloneFields(original, delegate, length, clone);
        return clone;
    }

    private void cloneFields(Object original, CloneFactory delegate, int length, Object clone) {
        if (componentType.isPrimitive()) {
            System.arraycopy(original, 0, clone, 0, length);
        } 
        else for (int n = 0; n < length; n++) {
            Array.set(clone, n, delegate.clone(Array.get(original, n)));
        }
    }

}
