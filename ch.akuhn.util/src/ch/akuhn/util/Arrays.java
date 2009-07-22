package ch.akuhn.util;

public class Arrays {

	public static <E> E[] append(E[] array, E element) {
		E[] result = java.util.Arrays.copyOf(array, array.length + 1);
		result[array.length] = element;
		return result;
	}
	
}
