package org.codemap.util;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

import ch.deif.meander.Point;

public class CodemapLabels extends MapScheme<String> {
	
	private interface LabelStrategy {
		public String forLocation(Point location);
	}
	
	LabelStrategy identifierLabel = new LabelStrategy() {
		@Override
		public String forLocation(Point location) {
			IPath path = new Path(location.getDocument());
			path = path.removeFileExtension();
			return path.lastSegment();
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
