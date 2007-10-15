package ch.akuhn.util;

import java.util.HashMap;

public abstract class CacheMap<K,V> extends HashMap<K,V> {

	public CacheMap() {
		super();
	}
	
	public CacheMap(int initialCapacity) {
		super(initialCapacity);
	}
	
	public CacheMap(int initialCapacity, float loadFactor) {
		super(initialCapacity, loadFactor);
	}
	
	public CacheMap(HashMap<? extends K,? extends V> m) {
		super(m);
	}

	@SuppressWarnings("unchecked")
	@Override
	public V get(Object key) {
		V value = super.get(key);
		if (value == null) {
			K k = (K) key;
			super.put(k, value = initialize(k));
		}
		return value;
	}

	public abstract V initialize(K key);
	
}
