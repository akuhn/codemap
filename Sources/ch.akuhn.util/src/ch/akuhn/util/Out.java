package ch.akuhn.util;

public class Out {

    public static void puts(int[] more) {
        System.out.print("#(");
        Separator s = new Separator();
        for (int each : more) {
            System.out.print(s);
            System.out.print(each);
        }
        System.out.println(")");
    }

    public static <E> void puts(Iterable<E> iterable) {
        for (Object o : iterable) {
            System.out.println(o);
        }
    }
    
    public static <E> void p(Iterable<E> iterable) {
        System.out.print("[");
        Separator s = new Separator();
        for (Object o : iterable) {
            System.out.print(s);
            System.out.print(o);
        }
        System.out.println("]");
    }

    public static void puts(Object object) {
        System.out.println(object);
    }

    public static void puts(Object object, Object... objects) {
        System.out.println(object);
        for (Object o : objects) {
            System.out.println(o);
        }
    }

    public static void puts(Object[] objects) {
        System.out.print("[");
        Separator s = new Separator();
        for (Object o : objects) {
            System.out.print(s);
            System.out.print(o);
        }
        System.out.println("]");
    }

    public static void puts(String format, Object... objects) {
        System.out.println(String.format(format, objects));
    }

}
