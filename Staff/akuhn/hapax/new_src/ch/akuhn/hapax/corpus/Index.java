package ch.akuhn.hapax.corpus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Index<E> {

    private Map<E,Integer> map;
    private List<E> list;
    
    public Index() {
        map = new HashMap<E,Integer>();
        list = new ArrayList<E>();
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
    
    public E get(int index) {
        return list.get(index);
    }
    
    public int get(E element) {
        Integer index = map.get(element);
        return index == null ? -1 : index;
    }   
    
}
