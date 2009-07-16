package ch.akuhn.hapax.index;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import ch.akuhn.foreach.Each;

public class AssociativeList<E> implements Cloneable, Iterable<E> {

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

    protected int add(E element) {
        Integer index = map.get(element);
        if (index == null) {
            index = list.size();
            map.put(element, index);
            list.add(element);
        }
        return index;
    }

    @Override
    public AssociativeList<E> clone() {
        return new AssociativeList<E>(this);
    }

    public int get(E element) {
        Integer index = map.get(element);
        return index == null ? -1 : index;
    }

    public E get(int index) {
        return list.get(index);
    }

    //@Override
    public Iterator<E> iterator() {
        return list.iterator();
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
				return new Iterator<Each<E>>() {

					private Iterator<E> iterator = list.iterator();
					private Each<E> each = new Each<E>();
					private int index = 0;
					
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
				};
			}
    	};
    }

}
