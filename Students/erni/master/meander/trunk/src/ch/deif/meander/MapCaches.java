/**
 * 
 */
package ch.deif.meander;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;

public class MapCaches {
	
	private static Executor executor = Executors.newCachedThreadPool();

	private Map<Class<? extends MapAlgorithm<?>>,RunnableFuture<?>> cache;
	private MapInstance map;

	public MapCaches(MapInstance map) {
		this.cache = new HashMap<Class<? extends MapAlgorithm<?>>, RunnableFuture<?>>();
		this.map = map;
	}

	private <V> RunnableFuture<?> makeIfAbsent(Class<? extends MapAlgorithm<V>> key) {
		synchronized (key) {
			RunnableFuture<?> future = cache.get(key);
			if (future == null) {
				Callable<V> callable;
				try {
					callable = key.newInstance().setMap(map);
				} catch (InstantiationException e) {
					throw new RuntimeException(e);
				} catch (IllegalAccessException e) {
					throw new RuntimeException(e);
				}
				future = new FutureTask<V>(callable);
				System.out.println("Running " + key);
				cache.put(key, future);
				executor.execute(future);
			}
			return future;
		}
	}
	
	public boolean isDone(Class<? extends MapAlgorithm<?>> key) {
		Future<?> future = cache.get(key);
		return future != null && future.isDone();
	}
	
	public <V> void run(Class<? extends MapAlgorithm<V>> key) {
		this.makeIfAbsent(key);
	}
	
	@SuppressWarnings("unchecked")
	public <V> V get(Class<? extends MapAlgorithm<V>> key) {
		try {
			return (V) this.makeIfAbsent(key).get();
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
			throw new RuntimeException(ex);
		} catch (ExecutionException ex) {
			throw new RuntimeException(ex);
		}
	}
	
}