package ch.akuhn.io.chunks;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class ChunkSpec<Kind> {

	public static final String MATCH_ALL = "*";
	
	private int name;
	private String mnemonic;
	private Class<Kind> kind;
	private Method readFrom;
	private Method writeOn;
	private Constructor<Kind> createFrom;
	
	public ChunkSpec(Class<Kind> kind, String mnemonic) {
		this.mnemonic = mnemonic;
		this.name = Chunks.makeName(mnemonic);
		this.kind = kind;
		this.createFrom = findReadFromConstructor();
		this.readFrom = findReadFromMethod();
		this.writeOn = findWriteOnMethod();
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

	@SuppressWarnings("unchecked")
	private final Constructor<Kind> findReadFromConstructor() {
		for (Constructor<?> method: kind.getConstructors())
			if (method.isAnnotationPresent(ReadFromChunk.class)) 
				return (Constructor<Kind>) method;
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public Kind readFrom(ChunkInput input) throws IOException {
		try {
			if (createFrom != null) return createFrom.newInstance(input);
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
	
	public void writeOn(Kind element, ChunkOutput output) throws IOException {
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
	
	public Class<Kind> getKind() {
		return kind;
	}
	
}
