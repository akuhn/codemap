package ch.deif.aNewMeander;

public class MapScheme<Value> {

	private Value defaultValue;
	
	public MapScheme() {
		this(null);
	}
	
	public MapScheme(Value defaultValue) {
		this.defaultValue = defaultValue;
	}
	
	public Value forLocation(Location location) {
		return defaultValue;
	}
	
	public static <T> MapScheme<T> with(T value) {
		return new MapScheme<T>(value);
	}
	
}
