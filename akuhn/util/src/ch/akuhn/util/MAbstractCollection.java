package ch.akuhn.util;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;

public abstract class MAbstractCollection<T> extends AbstractCollection<T> implements
		MCollection<T> {

	@Override
	public boolean allSatisfy(Predicate<T> block) {
		return Magic.allSatisfy(this, block);
	}

	@Override
	public T any() {
		return Magic.any(this);
	}

	@Override
	public boolean anySatisfy(Predicate<T> block) {
		return Magic.anySatisfy(this, block);
	}

	@Override
	public <V> Collection<V> collect(Function<V,T> block) {
		return Magic.collect(this, block);
	}

	@Override
	public int count(Predicate<T> block) {
		return Magic.count(this, block);
	}

	@Override
	public T detect(Predicate<T> block) {
		return Magic.detect(this, block);
	}

	@Override
	public void forEach(Procedure<T> block) {
		Magic.forEach(this, block);
	}

	@Override
	public <V> V inject(V initialValue, BinaryFunction<V,V,T> block) {
		return Magic.inject(this, initialValue, block);
	}

	@Override
	public Collection<T> reject(Predicate<T> block) {
		return Magic.reject(this, block);
	}

	@Override
	public void remove(Predicate<T> block) {
		Magic.remove(this, block);
	}

	@Override
	public Collection<T> select(Predicate<T> block) {
		return Magic.select(this, block);
	}

	@Override
	public Collection<T> splice(Predicate<T> block) {
		return Magic.splice(this, block);
	}

}
