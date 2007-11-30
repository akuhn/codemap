package ch.akuhn.util;

public abstract class Objects {

	public static void out(Object object) {
		System.out.println(object);
	}

	public static void out(Object[] objects) {
		System.out.print("[");
		for (int n = 0; n < objects.length; n++) {
			if (n != 0) System.out.print(", ");
			System.out.print(objects[n]);
		}
		System.out.println("]");
	}

	public boolean equals(Object a, Object b) {
		return a == null ? b == null : a.equals(b);
	}
	
}
