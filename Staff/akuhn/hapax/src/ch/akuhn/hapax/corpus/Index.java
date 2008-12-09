package ch.akuhn.hapax.corpus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Index<E> implements Cloneable, Iterable<E> {

    private List<E> list;
    private Map<E,Integer> map;

    public Index() {
        map = new HashMap<E,Integer>();
        list = new ArrayList<E>();
    }

    public Index(Index<E> index) {
        map = new HashMap<E,Integer>(index.map);
        list = new ArrayList<E>(index.list);
    }

    public int add(E element) {
        Integer index = map.get(element);
        if (index == null) {
            index = list.size();
            map.put(element, index);
            list.add(element);
        }
        return index;
    }

    @Override
    public Index<E> clone() {
        return new Index<E>(this);
    }

    public int get(E element) {
        Integer index = map.get(element);
        return index == null ? -1 : index;
    }

    public E get(int index) {
        return list.get(index);
    }

    @Override
    public Iterator<E> iterator() {
        return list.iterator();
    }

    public int size() {
        return list.size();
    }

}
