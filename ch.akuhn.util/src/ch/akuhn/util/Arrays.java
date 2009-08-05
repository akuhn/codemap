package ch.akuhn.util;

public abstract class Arrays {

    public static <E> E[] append(E[] array, E element) {
        E[] result = java.util.Arrays.copyOf(array, array.length + 1);
        result[array.length] = element;
        return result;
    }

    public static <E> java.util.List<E> asList(E... a) {
        return java.util.Arrays.asList(a);
    }


}
