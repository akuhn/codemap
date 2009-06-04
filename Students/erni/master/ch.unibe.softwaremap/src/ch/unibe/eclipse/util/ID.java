package ch.unibe.eclipse.util;

import ch.unibe.softwaremap.SoftwareMapCore;
import ch.unibe.softwaremap.ui.SelectionView;

public enum ID {
	
	PACKAGE_EXPLORER ("org.eclipse.jdt.ui.PackageExplorer"),
	RESOURCE_NAVIGATOR ("org.eclipse.ui.views.ResourceNavigator"),
	CONTENT_OUTLINE ("org.eclipse.ui.views.ContentOutline"),
	SELECTION_VIEW (SoftwareMapCore.makeID(SelectionView.class));
	
	public final String id;

	ID(String name) {
		this.id = name;
	}
	
}
