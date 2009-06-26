package ch.unibe.softwaremap.ui;

import ch.unibe.softwaremap.CodemapCore;

public class MapController {

	private MapView view;
	private CodemapCore plugin;
	
	public MapController(MapView view) {
		this.view = view;
		this.plugin = CodemapCore.getPlugin();
	}

	public void onResize() {
	}
	
	public void onOpenView() {
		
	}
	
	public void onShowMap() {
		
	}
	
	public void onProjectChanged() {
		
	}
	
	public void onSelectionChanged() {
		
	}
	
	public void onEditorOpened() {
		
	}
	
	public void onEditorClosed() {
		
	}
	
	public void onEditorActivated() {
		
	}
	
}