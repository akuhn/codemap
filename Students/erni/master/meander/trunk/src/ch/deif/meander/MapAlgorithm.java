package ch.deif.meander;

import java.util.concurrent.Callable;

public abstract class MapAlgorithm<V> implements Callable<V> {

	protected MapInstance map;

	public MapAlgorithm<V> setMap(MapInstance map) {
		this.map = map;
		return this;
	}

	@Override
	public abstract V call();
	
}
