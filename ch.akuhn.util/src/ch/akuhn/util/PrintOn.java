package ch.akuhn.util;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Method;

public class PrintOn implements Appendable, Closeable {

    private Appendable buf;

    public PrintOn() {
        this(new StringBuilder());
    }

    public PrintOn(Appendable buf) {
        this.buf = buf;
    }

    public final PrintOn append(char c) {
        try {
            buf.append(c);
        } catch (IOException ex) {
            throw Throw.exception(ex);
        }
        return this;
    }

    public final PrintOn append(CharSequence string) {
        try {
            buf.append(string);
        } catch (IOException ex) {
            throw Throw.exception(ex);
        }
        return this;
    }

    public final PrintOn append(CharSequence string, int start, int end) {
        try {
            buf.append(string, start, end);
        } catch (IOException ex) {
            throw Throw.exception(ex);
        }
        return this;
    }

    public final PrintOn cr() {
        return append('\n');
    }

    public final PrintOn print(double value) {
        return append(Double.toString(value));
    }

    public final PrintOn print(int value) {
        return append(Integer.toString(value));
    }

    public final PrintOn print(Object object) {
        if (object == null) {
            append("null");
        }
        // Custom #toString precedes.
        else if (overridesToString(object)) {
        	append(object.toString());
        }    	
        // Print arrays as [elem, elem, elem, ...]
        else if (object.getClass().isArray()) {
    		printEach(As.iterable(object));
    	}
        // Print arrays as [elem, elem, elem, ...]
        else if (object instanceof Iterable<?>) {
    		printEach(object);
        }
        else {
        	append(object.toString());
        }
        return this;
    }

	private void printEach(Object object) {
		PrintOn out = new PrintOn(this);
		out.append('[');
		for (Object each: (Iterable<?>) object) out.separatedBy(", ").print(each);
		out.append(']');
	}

    public final PrintOn space() {
        return append(' ');
    }

    public final PrintOn tab() {
        return append('\t');
    }

    @Override
    public final String toString() {
        return buf.toString();
    }

    public final void close() {
        if (buf instanceof Closeable) {
            try {
                ((Closeable) buf).close();
            } catch (IOException ex) {
                throw Throw.exception(ex);
            }
        }
    }

    boolean separate = false;
    
	public final PrintOn separatedBy(String string) {
		if (separate) this.append(string);
		separate = true;
		return this;
	}

    private boolean overridesToString(Object object) {
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

	public void beginLoop() {
		separate = false;
	}

	public PrintOn p(String string) {
		return this.append(string);
	}
	
	public PrintOn p(int n) {
		return this.append(Integer.toString(n));
	}

	public PrintOn p(double d) {
		return this.append(Double.toString(d));
	}

	public PrintOn comma() {
		if (separate) this.append(',');
		separate = true;
		return this;
	}
	
}
