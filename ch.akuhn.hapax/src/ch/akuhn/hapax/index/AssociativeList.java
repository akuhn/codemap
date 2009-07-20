package ch.akuhn.hapax.index;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import ch.akuhn.foreach.Each;

public class AssociativeList<E> implements Cloneable, Iterable<E> {

	public static final int NONE = -1;
	
    private List<E> list;
    private Map<E,Integer> map;

    public AssociativeList() {
        map = new HashMap<E,Integer>();
        list = new ArrayList<E>();
    }

    public AssociativeList(AssociativeList<E> index) {
        map = new HashMap<E,Integer>(index.map);
        list = new ArrayList<E>(index.list);
    }

    /*default*/ int add(E element) {
    	if (element == null) throw new IllegalArgumentException();
        Integer index = map.get(element);
        if (index == null) {
            index = list.size();
            map.put(element, index);
            list.add(element);
        }
        return index;
    }

	/*default*/ int remove(E element) {
		Integer index = map.remove(element);
		if (index == null) return NONE;
		list.remove(index.intValue());
		for (Map.Entry<E, Integer> each: map.entrySet()) {
			Integer eachIndex = each.getValue();
			if (eachIndex > index) each.setValue(eachIndex - 1);
		}
		return index;
	}
		
		
    @Override
    public AssociativeList<E> clone() {
        return new AssociativeList<E>(this);
    }

    public int get(E element) {
        Integer index = map.get(element);
        return index == null ? NONE : index;
    }

    public E get(int index) {
    	if (index >= list.size()) return null;
        return list.get(index);
    }

    @Override
    public Iterator<E> iterator() {
        return Collections.unmodifiableCollection(list).iterator();
    }

    public int size() {
        return list.size();
    }

    public boolean contains(E element) {
        return map.containsKey(element);
    }
    
    public Iterable<Each<E>> withIndices() {
    	return new Iterable<Each<E>>() {
			@Override
			public Iterator<Each<E>> iterator() {
				return new Iter<E>(list.iterator());
			}
    	};
    }
    
    private static class Iter<E> implements Iterator<Each<E>> {

		private Iterator<E> iterator;
		private Each<E> each = new Each<E>();
		private int index = 0;

		/*default*/ Iter(Iterator<E> iterator) {
			this.iterator = iterator;
		}
		
		@Override
		public boolean hasNext() {
			return iterator.hasNext();
		}

		@Override
		public Each<E> next() {
			if (!hasNext()) throw new NoSuchElementException();
			each.value = iterator.next();
			each.index = index++;
			return each;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
	    	
    }

}
