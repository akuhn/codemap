package ch.akuhn.io.chunks;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class ChunkSpec {

	public static final String MATCH_ALL = "*";
	
	private int name;
	private String mnemonic;
	private Class<?> kind;
	private Method readFrom;
	private Method writeOn;
	private Constructor<?> createFrom;
	
	public ChunkSpec(Class<?> kind) {
		this.kind = kind;
		this.createFrom = findReadFromConstructor();
		this.readFrom = findReadFromMethod();
		this.writeOn = findWriteOnMethod();
		this.mnemonic = findDefaultMnemonic();
		this.name = Chunks.makeName(mnemonic);
	}

	private String findDefaultMnemonic() {
		if (createFrom != null) return createFrom.getAnnotation(ReadFromChunk.class).value();
		if (readFrom != null) return readFrom.getAnnotation(ReadFromChunk.class).value();
		if (writeOn != null) return writeOn.getAnnotation(WriteOnChunk.class).value();
		throw new UnsupportedOperationException(kind + " does not support chunks.");
	}

	private final Method findWriteOnMethod() {
		for (Method method: kind.getMethods())
			if (method.isAnnotationPresent(WriteOnChunk.class)) 
				return method;
		return null;
	}

	private final Method findReadFromMethod() {
		for (Method method: kind.getMethods())
			if (method.isAnnotationPresent(ReadFromChunk.class)) 
				return method;
		return null;
	}

	private final Constructor<?> findReadFromConstructor() {
		for (Constructor<?> method: kind.getConstructors())
			if (method.isAnnotationPresent(ReadFromChunk.class)) 
				return method;
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public <Kind> Kind readFrom(ChunkInput input) throws IOException {
		try {
			if (createFrom != null) return (Kind) createFrom.newInstance(input);
			if (readFrom != null) return (Kind) readFrom.invoke(null, input);
			throw new AssertionError(kind + " does not support reading from chunks!");
		} catch (IllegalArgumentException ex) {
			throw new RuntimeException(ex);
		} catch (IllegalAccessException ex) {
			throw new RuntimeException(ex);
		} catch (InvocationTargetException ex) {
			throw unwrap(ex);
		} catch (InstantiationException ex) {
			throw new RuntimeException(ex);
		}
	}
	
	public void writeOn(Object element, ChunkOutput output) throws IOException {
		output.beginChunk(name);
		try {
			writeOn.invoke(element, output);
		} catch (IllegalArgumentException ex) {
			throw new RuntimeException(ex);
		} catch (IllegalAccessException ex) {
			throw new RuntimeException(ex);
		} catch (InvocationTargetException ex) {
			throw unwrap(ex);
		}
		output.endChunk(name);
	}
	
	public int getName() {
		return name;
	}

	public String getMnemonic() {
		return mnemonic;
	}
	
	public Class<?> getKind() {
		return kind;
	}
	
	private RuntimeException unwrap(InvocationTargetException ex) throws IOException {
		Throwable th = ex.getTargetException();
		if (th instanceof IOException) throw (IOException) th;
		if (th instanceof RuntimeException) throw (RuntimeException) th;
		if (th instanceof Error) throw (Error) th;
		throw new RuntimeException(ex);
	}
	
}
