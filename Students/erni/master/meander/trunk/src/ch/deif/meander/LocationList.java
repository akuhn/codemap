package ch.deif.meander;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * List of locations on a map.
 * 
 * @author Adrian Kuhn
 */
public class LocationList implements Iterable<Location> {

	private List<Location> locations;

	public LocationList() {
		this.locations = new ArrayList<Location>();
	}

	@Override
	public Iterator<Location> iterator() {
		return locations.iterator();
	}

	public int count() {
		return locations.size();
	}

	public Location makeLocation(double x, double y, double elevation) {
		Location location = new Location(x, y, elevation);
		locations.add(location);
		return location;
	}

//	@Override
//		public String getName() {
//		if (document == null) return String.valueOf(name);
//		String name = (new File(document.name()).getName());
//		if (name.contains("{")) {
//			name = name.substring(name.indexOf("{") + 1, name.length());
//		}
//		return $(name).removeSuffix(".java");
//	}

	public Location at(int index) {
		return locations.get(index);
	}

	protected void normalizePixelXY(int pixelSize) {
		for (Location each: this) {
			each.normalizePixelXY(pixelSize);
		}
	}

	protected void normalizeLocations() {
		double minX = 0;
		double maxX = 0;
		double minY = 0;
		double maxY = 0;
		double maxElevation = 0;
		for (Location each: this) {
			minY = Math.min(minY, each.y());
			maxY = Math.max(maxY, each.y());
			minX = Math.min(minX, each.x());
			maxX = Math.max(maxX, each.x());
			maxElevation = Math.max(maxElevation, each.elevation());
		}
		double width = maxX - minX;
		double height = maxY - minY;
		for (Location each: this) {
			each.normalizeXY(minX, width, minY, height);
			each.normalizeElevation(maxElevation);
		}
	}

}
