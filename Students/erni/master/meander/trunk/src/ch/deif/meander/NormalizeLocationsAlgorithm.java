package ch.deif.meander;

public class NormalizeLocationsAlgorithm extends MapAlgorithm {

	public NormalizeLocationsAlgorithm(Map map) {
		super(map);
	}

	@Override
	public void run() {
		double minX, maxX, minY, maxY;
		minX = minY = 0;
		maxX = maxY = 0;
		double maxHeight = 0; 
		for (Location l: this.map.locations) {
			minX = Math.min(l.xNormed, minX);
			minY = Math.min(l.yNormed, minY);			
			maxX = Math.max(l.xNormed, maxX);
			maxY = Math.max(l.yNormed, maxY);
			maxHeight = Math.max(l.height, maxHeight);
		}
		for (Location l: this.map.locations) {
			l.xNormed = (l.xNormed - minX)/(maxX - minX);
			l.yNormed = (l.yNormed - minY)/(maxY -  minY);
			l.height = l.height*100/maxHeight;
		}
	}

}
