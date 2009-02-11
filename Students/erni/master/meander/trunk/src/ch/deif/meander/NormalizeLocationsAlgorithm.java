package ch.deif.meander;

public class NormalizeLocationsAlgorithm extends MapAlgorithm {

	public NormalizeLocationsAlgorithm(Map map) {
		super(map);
	}

	@Override
	public void run() {
		double minX, maxX, minY, maxY;
		minX = minY = -3;
		maxX = maxY = 3;
		double maxHeight = 0; 
		for (Location l: this.map.locations) {
			//minX = Math.min(l.xNormed, minX);
			//minY = Math.min(l.yNormed, minY);			
			//maxX = Math.max(l.xNormed, maxX);
			//maxY = Math.max(l.yNormed, maxY);
			maxHeight = Math.max(l.height, maxHeight);
		}
		double scale = Math.max((maxX - minX),(maxY -  minY));
		for (Location l: this.map.locations) {
			l.x = (l.x - minX)/scale;
			l.y = (l.y - minY)/scale;
			l.height = l.height * 100 / maxHeight;
		}
	}

}
