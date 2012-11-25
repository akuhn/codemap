
package ch.akuhn.util;

import java.lang.reflect.Constructor;

/**
 * ACME = a factory making everything.
 * 
 * @author akuhn
 * 
 */
public class Factory<T> {

    @SuppressWarnings("unchecked")
    private static final <T> Constructor<T> searchConstructor(Constructor<?>[] all, int parameterLength) {
        Constructor result = null;
        for (Constructor init : all) {
            if (init.getParameterTypes().length == parameterLength) {
                if (result != null) throw new AssertionError("Ambigous constructor");
                result = init;
            }
        }
        return (Constructor<T>) result;
    }
    private Constructor<T> takeOne;
    private Constructor<T> takeThree;
    private Constructor<T> takeTwo;

    private Class<T> type;

    public Factory(Class<T> type) {
        this.type = type;
        Constructor<?>[] all = type.getDeclaredConstructors();
        this.takeOne = searchConstructor(all, 1);
        this.takeTwo = searchConstructor(all, 2);
        this.takeThree = searchConstructor(all, 3);
    }

    public T create() {
        try {
            return type.newInstance();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public T create(Object obj) {
        try {
            return takeOne.newInstance(obj);
        } catch (Exception ex) {
            throw Throw.exception(ex);
        }
    }

    public T create(Object obj1, Object obj2) {
        try {
            return takeTwo.newInstance(obj1, obj2);
        } catch (Exception ex) {
            throw Throw.exception(ex);
        }
    }

    public T create(Object obj1, Object obj2, Object obj3) {
        try {
            return takeThree.newInstance(obj1, obj2, obj3);
        } catch (Exception ex) {
            throw Throw.exception(ex);
        }
    }

}
