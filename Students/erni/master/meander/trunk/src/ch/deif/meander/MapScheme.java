package ch.deif.meander;

public class MapScheme<Value> {

	private Value defaultValue;
	
	public MapScheme() {
		this(null);
	}
	
	public MapScheme(Value defaultValue) {
		this.defaultValue = defaultValue;
	}
	
	public Value forLocation(Point location) {
		return defaultValue;
	}
	
	public static <T> MapScheme<T> with(T value) {
		return new MapScheme<T>(value);
	}
	
}
