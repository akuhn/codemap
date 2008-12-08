package ch.akuhn.util.blocks;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import ch.akuhn.util.Throw;

@SuppressWarnings("unchecked")
public class Methods {

    private static class DynamicFunction implements Function {

        private final MethodReference ref;

        public DynamicFunction(String reference) {
            this.ref = new MethodReference(reference);
        }

        public Object apply(Object a) {
            Method m = ref.resolve(a.getClass());
            try {
                return m.invoke(a);
            } catch (IllegalArgumentException ex) {
                throw Throw.exception(ex);
            } catch (IllegalAccessException ex) {
                throw Throw.exception(ex);
            } catch (InvocationTargetException ex) {
                throw Throw.exception(ex.getCause());
            }
        }

    }

    private static class DynamicPredicate implements Predicate {

        private final MethodReference ref;

        public DynamicPredicate(String reference) {
            this.ref = new MethodReference(reference);
        }

        public boolean apply(Object a) {
            Method m = ref.resolve(a.getClass());
            try {
                return (Boolean) m.invoke(a);
            } catch (IllegalArgumentException ex) {
                throw Throw.exception(ex);
            } catch (IllegalAccessException ex) {
                throw Throw.exception(ex);
            } catch (InvocationTargetException ex) {
                throw Throw.exception(ex.getCause());
            }
        }

    }

    private static final class StaticFunction<T,A> implements Function<T,A> {
        private Method m;

        public StaticFunction(Method m) {
            this.m = m;
        }

        public T apply(A a) {
            try {
                return (T) m.invoke(null, a);
            } catch (IllegalArgumentException ex) {
                throw new RuntimeException(ex);
            } catch (IllegalAccessException ex) {
                throw new RuntimeException(ex);
            } catch (InvocationTargetException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private static final class StaticPredicate<T> implements Predicate<T> {
        private Method m;

        public StaticPredicate(Method m) {
            this.m = m;
        }

        public boolean apply(T t) {
            try {
                return (Boolean) m.invoke(null, t);
            } catch (IllegalArgumentException ex) {
                throw new RuntimeException(ex);
            } catch (IllegalAccessException ex) {
                throw new RuntimeException(ex);
            } catch (InvocationTargetException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private static final class VirtualFunction<T,A> implements Function<T,A> {
        private Method m;

        public VirtualFunction(Method m) {
            this.m = m;
        }

        public T apply(A a) {
            try {
                return (T) m.invoke(a);
            } catch (IllegalArgumentException ex) {
                throw new RuntimeException(ex);
            } catch (IllegalAccessException ex) {
                throw new RuntimeException(ex);
            } catch (InvocationTargetException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private static final class VirtualPredicate<T> implements Predicate<T> {
        private Method m;

        public VirtualPredicate(Method m) {
            this.m = m;
        }

        public boolean apply(T t) {
            try {
                return (Boolean) m.invoke(t);
            } catch (IllegalArgumentException ex) {
                throw new RuntimeException(ex);
            } catch (IllegalAccessException ex) {
                throw new RuntimeException(ex);
            } catch (InvocationTargetException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public static final <T,A> Function<T,A> asFunction(String reference) {
        return new DynamicFunction(reference);
    }

    public static <T> Predicate<T> asPredicate(String reference) {
        return new DynamicPredicate(reference);
    }

    public static StackTraceElement caller() {
        // getStackTrace()[0] --> Thread#getStackTrace
        // getStackTrace()[1] --> Methods#caller
        // getStackTrace()[2] --> active
        // getStackTrace()[3] --> caller of active method
        return Thread.currentThread().getStackTrace()[3];
    }

    static final Class findClass(String name) {
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException ex) {
            return null;
        }
    }

    public static Method fromName(String reference) {
        return fromName(reference, caller());
    }

    public static Method fromName(String reference, StackTraceElement context) {
        MethodReference ref = new MethodReference(reference);
        Class jclass = findClass(context.getClassName());
        return ref.resolve(jclass);
    }

    public static final <T,A> Function<T,A> newFunction(final Method m) {
        return Modifier.isStatic(m.getModifiers()) ? new StaticFunction(m) : new VirtualFunction(m);
    }

    public static final <T,A> Function<T,A> newFunction(String reference) {
        Method m = Methods.fromName(reference, Thread.currentThread().getStackTrace()[2]);
        return newFunction(m);
    }

    public static final <T> Predicate<T> newPredicate(final Method m) {
        return Modifier.isStatic(m.getModifiers()) ? new StaticPredicate(m) : new VirtualPredicate(m);
    }

    public static final <T> Predicate<T> newPredicate(String reference) {
        Method m = Methods.fromName(reference, Thread.currentThread().getStackTrace()[2]);
        return newPredicate(m);
    }

}
