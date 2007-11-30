package ch.akuhn.util;

public interface BinaryFunction<T,A,B> {

	public T eval(A a, B b);
	
}
