package ch.akuhn.util.blocks;

import static java.lang.String.format;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

@SuppressWarnings("unchecked")
public class Methods {

	private static class MethodReference {
		
		public String path;
		public String name;
		public String[] args;
		
		public MethodReference(String reference) {
			int n = reference.indexOf('#');
			this.path = n < 0 ? null : reference.substring(0, n);
			this.name = n < 0 ? reference : reference.substring(n + 1);
		}

		public Method resolve(Class context) {
			Class jclass = resolveClass(context);
			for (Method m : jclass.getMethods()) {
				if (m.getName().equals(name)) return m;
			}
			throw new Error(String.format("Method %s not found in %s.", this, jclass));
		}

		private Class resolveClass(Class context) {
			if (path == null) return context;
			Class $ = findClass(path);
			if ($ != null) return $;
			$ = findClass(context.getPackage() + "." + path);
			if ($ != null) return $;
			$ = findClass("java.lang." + path);
			if ($ != null) return $;
			return context;
		}

		@Override
		public String toString() {
			return format("%s#%s%s", path, name, args == null ? "" : args);
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

	public static Method fromName(String reference, StackTraceElement context) {
		MethodReference ref = new MethodReference(reference);
		Class jclass = findClass(context.getClassName());
		return ref.resolve(jclass);
	}
	
	private static final Class findClass(String name) {
		try {
			return Class.forName(name);
		} catch (ClassNotFoundException ex) {
			return null;
		}
	}

	public static final <T> Predicate<T> newPredicate(final Method m) {
		return Modifier.isStatic(m.getModifiers())
				? new StaticPredicate(m)
				: new VirtualPredicate(m);
	}

	public static final <T> Predicate<T> newPredicate(String reference) {
		Method m = Methods.fromName(reference, Thread.currentThread().getStackTrace()[2]);
		return newPredicate(m);
	}
	
	public static final <T,A> Function<T,A> newFunction(String reference) {
		Method m = Methods.fromName(reference, Thread.currentThread().getStackTrace()[2]);
		return newFunction(m);
	}
	
	public static final <T,A> Function<T,A> newFunction(final Method m) {
		return Modifier.isStatic(m.getModifiers())
				? new StaticFunction(m)
				: new VirtualFunction(m);
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

	public static Method fromName(String reference) {
		return fromName(reference, caller());
	}	

	public static StackTraceElement caller() {
		// getStackTrace()[0] --> Thread#getStackTrace
		// getStackTrace()[1] --> Methods#caller
		// getStackTrace()[2] --> active
		// getStackTrace()[3] --> caller of active method
		return Thread.currentThread().getStackTrace()[3];
	}	
	
	
}
