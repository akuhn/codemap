package ch.akuhn.util;

import java.util.Collection;

public interface MCollection<T> extends Collection<T> {

	public <V> Collection<V> collect(Function<V,T> block);
	
	public Collection<T> select(Predicate<T> block);

	public Collection<T> reject(Predicate<T> block);

	public T detect(Predicate<T> block);

	public <V> V inject(V initialValue, BinaryFunction<V,V,T> block);

	public int count(Predicate<T> block);

	public boolean anySatisfy(Predicate<T> block);

	public boolean allSatisfy(Predicate<T> block);

	public void forEach(Procedure<T> block);
	
	public void remove(Predicate<T> block);
	
	public Collection<T> splice(Predicate<T> block);

	public T any();
	
}
