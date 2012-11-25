package ch.akuhn.values;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MapValue<K, V> extends ReferenceValue<Map<K, V>> implements Map<K, V> {
    
    public MapValue() {
        this(new HashMap<K, V>());
    }

    protected MapValue(Map<K, V> map) {
        this.value = map;
    }

    @Override
    public void clear() {
        if (value.isEmpty()) return;
        value.clear();
        changed();
    }

    @Override
    public boolean containsKey(Object key) {
        return value.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return this.value.containsValue(value);
    }

    @Override
    public Set<java.util.Map.Entry<K, V>> entrySet() {
        return value.entrySet();
    }

    @Override
    public V get(Object key) {
       return value.get(key);
    }

    @Override
    public boolean isEmpty() {
       return value.isEmpty();
    }

    @Override
    public Set<K> keySet() {
        return value.keySet();
    }

    @Override
    public V put(K key, V value) {
        V oldValue = this.value.put(key, value);
        changed();
        return oldValue;
        
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> map) {
        value.putAll(map);
        changed();
    }

    @Override
    public V remove(Object key) {
        V removed = value.remove(key);
        changed();
        return removed;
    }

    @Override
    public int size() {
        return value.size();
    }

    @Override
    public Collection<V> values() {
        return value.values();
    }

}
