package org.codemap.util;

import ch.deif.meander.Point;
import ch.deif.meander.util.MapScheme;

public class CodemapLabels extends MapScheme<String> {
	
	private interface LabelStrategy {
		public String forLocation(Point location);
	}
	
	LabelStrategy identifierLabel = new LabelStrategy() {
		@Override
		public String forLocation(Point location) {
			String name = location.getDocument().getIdentifier();
			int lastPathSeparator = name.lastIndexOf('{');
			int lastDot = name.lastIndexOf('.');
			if (lastPathSeparator < lastDot) return name.substring(lastPathSeparator + 1, lastDot);
			return name;
		}
	};
	
	LabelStrategy noLabel = new LabelStrategy() {
		@Override
		public String forLocation(Point location) {
			return "";
		}
	};
	
	private static final String DEFAULT_JAVA_FILENAME = "";
	private LabelStrategy labels;
	
	public CodemapLabels() {
		super(DEFAULT_JAVA_FILENAME);
		labels = identifierLabel;
	}
	
	public void useNoLabels() {
		labels = noLabel;
	}
	
	public void useIdentifierLabels() {
		labels = identifierLabel;
	}

	@Override
	public String forLocation(Point location) {
		return labels.forLocation(location);
	}

}
