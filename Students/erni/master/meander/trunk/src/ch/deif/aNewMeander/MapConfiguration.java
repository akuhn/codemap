package ch.deif.aNewMeander;

import java.util.Arrays;
import java.util.Collection;

import ch.deif.meander.util.Collect;

public class MapConfiguration {

	private Location[] locations;
	
	private MapConfiguration(Location... locations) {
		this.locations = locations;
	}
	
	public MapConfiguration(MapConfiguration map) {
		this(Arrays.copyOf(map.locations, map.locations.length));
	}
	
	public MapConfiguration(Collection<? extends Location> locations) {
		this(locations.toArray(new Location[locations.size()]));
	}
	
	public Iterable<Location> locations() {
		return Arrays.asList(locations);
	}
	
	public MapConfiguration normalize() {
		double minX = 0;
		double maxX = 0;
		double minY = 0;
		double maxY = 0;
		for (Point each: this.locations()) {
			minY = Math.min(minY, each.getY());
			maxY = Math.max(maxY, each.getY());
			minX = Math.min(minX, each.getX());
			maxX = Math.max(maxX, each.getX());
		}
		double width = maxX - minX;
		double height = maxY - minY;
		Collect<Location> query = Collect.fromArray(locations);
		for (Collect<Location> each: query) {
			each.value = each.value.normalize(minX, minY, width, height);
		}
		return new MapConfiguration(query.asArray());
	}

	public MapConfigurationWithSize withSize(int size) {
		return new MapConfigurationWithSize(this, size);
	}

}
  