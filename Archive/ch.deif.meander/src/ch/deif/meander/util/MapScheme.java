package ch.deif.meander.util;

import ch.deif.meander.Point;

/** Maps handles to values.
 * 
 * @author akuhn
 *
 * @param <Value>
 */
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
