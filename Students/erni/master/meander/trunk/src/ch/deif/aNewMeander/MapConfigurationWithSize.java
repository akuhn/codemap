package ch.deif.aNewMeander;

public class MapConfigurationWithSize extends MapConfiguration {

	public final int width, height;
	
	public MapConfigurationWithSize(MapConfiguration map, int size) {
		super(map);
		this.width = this.height = size;
	}
	
}
