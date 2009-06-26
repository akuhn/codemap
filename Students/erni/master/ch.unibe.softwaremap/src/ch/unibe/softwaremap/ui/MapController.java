package ch.unibe.softwaremap.ui;

import ch.unibe.softwaremap.CodemapCore;

public class MapController {

	private static int serial = 0;
	
	private MapView view;
	private CodemapCore plugin;
	
	public MapController(MapView view) {
		this.view = view;
		this.plugin = CodemapCore.getPlugin();
	}

	public void onResize() {
		System.out.println("-- resize@" + serial++);
	}
	
	public void onOpenView() {
		System.out.println("-- openView@" + serial++);
	}
	
	public void onShowMap() {
		System.out.println("-- showMap@" + serial++);
	}
	
	public void onProjectChanged() {
		System.out.println("-- projectChanged@" + serial++);
	}
	
	public void onSelectionChanged() {
		System.out.println("-- selectionChanged@" + serial++);
	}
	
	public void onEditorOpened() {

	}
	
	public void onEditorClosed() {
		
	}
	
	public void onEditorActivated() {
		System.out.println("-- editorActivated@" + serial++);
	}
	
}