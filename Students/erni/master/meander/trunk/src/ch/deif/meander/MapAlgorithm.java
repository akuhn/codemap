package ch.deif.meander;

public abstract class MapAlgorithm implements Runnable {
	
	protected final Map map;
	
	public MapAlgorithm(Map map) {
		this.map = map;
	}
	
	public abstract void run();
	
}
