package ch.unibe.softwaremap.util;

public enum ID {
	
	PACKAGE_EXPLORER ("org.eclipse.jdt.ui.PackageExplorer"),
	RESOURCE_NAVIGATOR ("org.eclipse.ui.views.ResourceNavigator"),
	CONTENT_OUTLINE ("org.eclipse.ui.views.ContentOutline");
	
	public final String id;

	ID(String name) {
		this.id = name;
	}
	
}
