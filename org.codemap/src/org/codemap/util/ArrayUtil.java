package org.codemap.util;


public class ArrayUtil {

    public static <T> T[] asArray(T... elems) {
        return elems;
    }

    public static <T> boolean isEmpty(T[] array) {
        return array.length == 0;
    }

}
