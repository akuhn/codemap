package ch.deif.meander;

public abstract class MapAlgorithm implements Runnable {

	private Map map;

	public MapAlgorithm(Map map) {
		setMap(map);
	}
	
	public MapAlgorithm() {
		
	}
	
	protected void setMap(Map map) {
		this.map = map;		
	}

	protected Map getMap() {
		return map;
	}

	public abstract void run();
}
