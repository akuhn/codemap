package ch.akuhn.values;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;


public class CollectionValue<V> extends ReferenceValue<Collection<V>> implements Collection<V> {

    public CollectionValue(Collection<V> collection) {
        this.value = collection;
    }

    public CollectionValue() {
        this(new ArrayList<V>());
    }
    
    @Override
    public synchronized boolean add(V element) {
        boolean changed = value.add(element);
        if (changed) changed();
        return changed;
    }

    @Override
    public synchronized boolean addAll(Collection<? extends V> collection) {
        boolean changed = value.addAll(collection);
        if (changed) changed();
        return changed;
    }

    @Override
    public synchronized void clear() {
        if (value.isEmpty()) return;
        value.clear();
        changed();
    }

    @Override
    public synchronized boolean contains(Object object) {
        return value.contains(object);
    }

    @Override
    public synchronized boolean containsAll(Collection<?> collection) {
        return value.containsAll(collection);
    }

    @Override
    public synchronized boolean isEmpty() {
        return value.isEmpty();
    }

    @Override
    public Iterator<V> iterator() {
        return Collections.unmodifiableCollection(value).iterator();
    }

    @Override
    public synchronized boolean remove(Object object) {
        boolean changed = value.remove(object);
        if (changed) changed();
        return changed;
    }

    @Override
    public synchronized boolean removeAll(Collection<?> collection) {
        boolean changed = value.removeAll(collection);
        if (changed) changed();
        return changed;
    }

    @Override
    public synchronized boolean retainAll(Collection<?> collection) {
        boolean changed = value.retainAll(collection);
        if (changed) changed();
        return changed;
    }
    
    @Override
    public synchronized int size() {
        return value.size();
    }

    @Override
    public synchronized Object[] toArray() {
        return value.toArray();
    }

    @Override
    public synchronized <T> T[] toArray(T[] array) {
        return value.toArray(array);
    }

    @Override
    public synchronized Collection<V> getValue() {
        return this;
    }
    
}
