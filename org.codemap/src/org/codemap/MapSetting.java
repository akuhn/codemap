package org.codemap;

/** A settable property of a map. 
 * Use {@link #define} to define new properties as a static final field of your client class,
 * and use MapInstance's #set #get and #reset methods to access to property of a map instance. 
 * 
 * @author Adrian Kuhn
 *
 * @param <V> type of property value.
 */
public class MapSetting<V> implements  Cloneable {
	
	public final String key;
	public final V defaultValue;
	
	private MapSetting(String key, V defaultValue) {
		this.key = key.intern();
		this.defaultValue = defaultValue;
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean equals(Object obj) {
		return (obj instanceof MapSetting) && ((MapSetting) obj).key == key;
	}

	@Override
	public int hashCode() {
		return this.key.hashCode();
	}

	public static <V> MapSetting<V> define(String key, V defaultValue) {
		return new MapSetting<V>(key, defaultValue);
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}

}
