package ch.akuhn.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class As {

    public static <T> T[] array(Class<T> tClass, T t, T... ts) {
        T[] $ = (T[]) Array.newInstance(tClass, ts.length + 1);
        System.arraycopy(ts, 0, $, 1, ts.length);
        $[0] = t;
        return $;
    }

    public static <T> T[] array(T t, T... ts) {
        return (T[]) array(Extensions.leastUpperBound(t.getClass(), ts), t, ts);
    }

    public static <T> T[] array(T[] arr, T t) {
        T[] $ = (T[]) Array.newInstance(arr.getClass().getComponentType(), arr.length + 1);
        System.arraycopy(arr, 0, $, 0, arr.length);
        $[arr.length] = t;
        return $;
    }

    public static <T> T[] array(T[] aaa, T[] bbb) {
        T[] $ = (T[]) Array.newInstance(aaa.getClass().getComponentType(), aaa.length + bbb.length);
        System.arraycopy(aaa, 0, $, 0, aaa.length);
        System.arraycopy(bbb, 0, $, aaa.length, bbb.length);
        return $;
    }

    public static <E> List<E> list(E... elements) {
        return Arrays.asList(elements);
    }

    public static <E> List<E> list(Iterable<E> iterable) {
        ArrayList<E> list = new ArrayList<E>();
        for (E each : iterable)
            list.add(each);
        list.trimToSize();
        return list;
    }

    public static <E> Set<E> set(E... elements) {
        return new HashSet<E>(Arrays.asList(elements));
    }

}
