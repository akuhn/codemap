package ch.akuhn.util;

import java.lang.reflect.Array;
import java.lang.reflect.Method;

public class Out extends PrintOn {

    public Out(Appendable app) {
        super(app);
    }
    
    public Out() {
        super(System.out);
    }
    
    
    
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

    private Out internalPrint(Object object) {
        if (hasToString(object)) {
            this.print(object);
        }
        else if (object.getClass().isArray()) {
            this.append('[');
            Separator sep = new Separator();
            for (int n = 0; n < Array.getLength(object); n++) {
                this.print(sep);
                this.internalPrint(Array.get(object, n));
            }
            this.append(']');
        }
        else if (object instanceof Iterable<?>) {
            this.append('[');
            Separator sep = new Separator();
            for (Object each: (Iterable<?>) object) {
                this.print(sep);
                this.internalPrint(each);
            }
            this.append(']');
        }
        else {
            this.print(object);
        }
        return this;
    }
    
    public Out put(Object object) {
        this.internalPrint(object).cr();
        return this;
    }
    
    public void putEach(Object object) {
        if (object.getClass().isArray()) 
            for (int n = 0; n < Array.getLength(object); n++)
                this.put(Array.get(object, n));
        else if (object instanceof Iterable<?>) 
            for (Object each: (Iterable<?>) object) 
                this.put(each);
        else 
            this.put(object);
    }

    private boolean hasToString(Object object) {
        try {
            Class<?> type = object.getClass();
            Method toString = type.getMethod("toString");
            return toString.getDeclaringClass() != Object.class;
        } catch (SecurityException ex) {
            throw Throw.exception(ex);
        } catch (NoSuchMethodException ex) {
            throw Throw.exception(ex);
        }
    }
    
    public static void main(String... args) {
        Out out = new Out(System.out);
        out.put("aaa");
        out.put(new Object());
        out.put(1);
        out.put(new int[][] { {1,2},{3} });
    }
    
}
