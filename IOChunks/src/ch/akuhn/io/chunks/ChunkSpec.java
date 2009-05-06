package ch.akuhn.io.chunks;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class ChunkSpec<Kind> {

	private int name;
	private String mnemonic;
	private Class<Kind> kind;
	private Method readFrom;
	private Method writeOn;
	
	public ChunkSpec(String mnemonic, Class<Kind> kind) {
		this.mnemonic = mnemonic;
		this.name = Chunks.makeName(mnemonic);
		this.kind = kind;
		this.readFrom = findReadFromMethod(kind);
		this.writeOn = findWriteOnMethod(kind);
	}

	private static final Method findWriteOnMethod(Class<?> kind) {
		try {
			Method method = kind.getMethod("writeOn", ChunkOutput.class);
			if (Modifier.isStatic(method.getModifiers())) throw new RuntimeException();
			if (!Modifier.isPublic(method.getModifiers())) throw new RuntimeException();
			return method;
		} catch (SecurityException ex) {
			throw new RuntimeException(ex);
		} catch (NoSuchMethodException ex) {
			throw new RuntimeException(ex);
		}
	}

	private static final Method findReadFromMethod(Class<?> kind) {
		try {
			Method method = kind.getMethod("readFrom", ChunkInput.class);
			if (!Modifier.isStatic(method.getModifiers())) throw new RuntimeException();
			if (!Modifier.isPublic(method.getModifiers())) throw new RuntimeException();
			if (method.getReturnType().isAssignableFrom(kind)) throw new RuntimeException();
			return method;
		} catch (SecurityException ex) {
			throw new RuntimeException(ex);
		} catch (NoSuchMethodException ex) {
			throw new RuntimeException(ex);
		}
	}
	
	@SuppressWarnings("unchecked")
	public Kind readFrom(ChunkInput input) throws IOException {
		try {
			return (Kind) readFrom.invoke(null, input);
		} catch (IllegalArgumentException ex) {
			throw new RuntimeException(ex);
		} catch (IllegalAccessException ex) {
			throw new RuntimeException(ex);
		} catch (InvocationTargetException ex) {
			if (ex.getTargetException() instanceof IOException) 
				throw (IOException) ex.getTargetException();
			throw new RuntimeException(ex);
		}
	}
	
	public void writeOn(Kind element, ChunkOutput output) throws IOException {
		ChunkOutput chunk = output.beginChunk(name);
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
		chunk.endChunk(name);
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
