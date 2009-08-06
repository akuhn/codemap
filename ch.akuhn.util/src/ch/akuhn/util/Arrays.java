package ch.akuhn.util;

import ch.akuhn.values.Value;

public abstract class Arrays {

    public static <E> E[] append(E[] array, E element) {
        E[] result = java.util.Arrays.copyOf(array, array.length + 1);
        result[array.length] = element;
        return result;
    }

    public static <E> java.util.List<E> asList(E... a) {
        return java.util.Arrays.asList(a);
    }

    public static int indexOf(Object[] array, Object element) {
        for (int n = 0; n < array.length; n++) {
            if (array[n] == element) return n;
        }
        return -1;
    }


}
