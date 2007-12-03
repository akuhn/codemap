package ch.akuhn.util;

import java.util.List;

public interface MList<T> extends List<T>, MCollection<T> {

	public List<List<T>> runsSatisfying(Predicate<T> block);
	
	public List<List<T>> runsFailing(Predicate<T> block);

}
