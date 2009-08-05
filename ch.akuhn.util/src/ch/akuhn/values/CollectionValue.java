package ch.akuhn.values;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;


public class CollectionValue<V> extends Value<Collection<V>> implements Collection<V> {

    public CollectionValue() {
        this.value = new ArrayList<V>();
    }
    
    @Override
    public boolean add(V element) {
        boolean changed = value.add(element);
        if (changed) changed();
        return changed;
    }

    @Override
    public boolean addAll(Collection<? extends V> collection) {
        boolean changed = value.addAll(collection);
        if (changed) changed();
        return changed;
    }

    @Override
    public void clear() {
        if (value.isEmpty()) return;
        value.clear();
        changed();
    }

    @Override
    public boolean contains(Object object) {
        return value.contains(object);
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        return value.containsAll(collection);
    }

    @Override
    public boolean isEmpty() {
        return value.isEmpty();
    }

    @Override
    public Iterator<V> iterator() {
        return Collections.unmodifiableCollection(value).iterator();
    }

    @Override
    public boolean remove(Object object) {
        boolean changed = value.remove(object);
        if (changed) changed();
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        boolean changed = value.removeAll(collection);
        if (changed) changed();
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        boolean changed = value.retainAll(collection);
        if (changed) changed();
        return changed;
    }

    @Override
    public int size() {
        return value.size();
    }

    @Override
    public Object[] toArray() {
        return value.toArray();
    }

    @Override
    public <T> T[] toArray(T[] array) {
        return value.toArray(array);
    }

}
