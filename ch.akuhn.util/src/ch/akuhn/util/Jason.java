package ch.akuhn.util;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/** Builder to print JSON files.
*
*/
public class Jason {

	private Appendable app;
	private boolean comma = false;
	private Jason parent;

	public Jason(Appendable app) {
		this.app = app;
		this.parent = null;
	}
	
	private Jason(Jason parent) {
		this.app = parent.app;
		this.parent = parent;
	}
	
	public Jason begin() {
		p('{');
		return new Jason(this);
	}
	
	public Jason end() {
		p('}');
		return parent;
	}

	public Jason put(String key) {
		if (comma) p(',');
		comma = true;
		p('"');
		p(key);
		p('"');
		p(':');
		return this;
	}

	public Jason put(String key, boolean b) {
		this.put(key);
		p(Boolean.toString(b));
		return this;
	}	
	
	public Jason put(String key, double d) {
		this.put(key);
		p(Double.toString(d));
		return this;
	}	

	public Jason put(String key, double[] ds) {
		return put(key, ds, ds.length);
	}
	
	public Jason put(String key, double[] ds, int length) {
		this.put(key);
		p('[');
		boolean comma = false;
		for (int i = 0; i < length; i++) {
			if (comma) p(',');
			comma = true;
			p(Double.toString(ds[i]));
		}
		p(']');
		return this;
	}

	public Jason put(String key, int n) {
		this.put(key);
		p(Integer.toString(n));
		return this;
	}

	public Jason put(String key, int[] ns) {
		return put(key, ns, ns.length);
	}
	
	public Jason put(String key, int[] ns, int length) {
		this.put(key);
		p('[');
		boolean comma = false;
		for (int i = 0; i < length; i++) {
			if (comma) p(',');
			comma = true;
			p(Integer.toString(ns[i]));
		}
		p(']');
		return this;
	}

	public Jason put(String key, Iterable<?> objects) {
		this.put(key);
		p('[');
		boolean comma = false;
		for (Object each: objects) {
			if (comma) p(',');
			comma = true;
			p(each);
		}
		p(']');
		return this;
	}
	
	public Jason put(String key, Object object) {
		this.put(key);
		p(object);
		return this;
	}

	public Jason put(String key, String s) {
		this.put(key);
		p('"');
		p(String.valueOf(s));
		p('"');
		return this;
	}

	private void p(char ch) {
		try {
			app.append(ch);
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	private void p(Object object) {
		try {
			Method method = object.getClass().getMethod("toJason", Jason.class);
			method.invoke(object, this);
		} catch (IllegalArgumentException ex) {
			throw new RuntimeException(ex);
		} catch (IllegalAccessException ex) {
			throw new RuntimeException(ex);
		} catch (InvocationTargetException ex) {
			throw new RuntimeException(ex);
		} catch (SecurityException ex) {
			throw new RuntimeException(ex);
		} catch (NoSuchMethodException ex) {
			throw new RuntimeException(ex);
		}
	}
	
	private void p(String string) {
		try {
			app.append(string);
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	public Jason begin(Class<?> kind) {
		return this.begin().put("kind", kind.getName());
	}
	
}
