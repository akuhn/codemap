package ch.unibe.softwaremap.util;

import ch.deif.meander.Point;
import ch.deif.meander.util.MapScheme;

public class JavaLabelScheme extends MapScheme<String> {
	
	private static final String DEFAULT_JAVA_FILENAME = "";
	
	public JavaLabelScheme() {
		super(DEFAULT_JAVA_FILENAME);
	}

	@Override
	public String forLocation(Point location) {
		String name = location.getDocument().getIdentifier();
		int lastPathSeparator = name.lastIndexOf('{');
		int lastDot = name.lastIndexOf('.');
		if (lastPathSeparator < lastDot) return name.substring(lastPathSeparator + 1, lastDot);
		return name;
	}

}
