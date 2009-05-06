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
		return writeOn.getAnnotation(ReadFromChunk.class).value();
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
			return (Kind) readFrom.invoke(null, input);
		} catch (IllegalArgumentException ex) {
			throw new RuntimeException(ex);
		} catch (IllegalAccessException ex) {
			throw new RuntimeException(ex);
		} catch (InvocationTargetException ex) {
			if (ex.getTargetException() instanceof IOException) 
				throw (IOException) ex.getTargetException();
			throw new RuntimeException(ex);
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
			if (ex.getTargetException() instanceof IOException) 
				throw (IOException) ex.getTargetException();
			throw new RuntimeException(ex);
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
	
}
