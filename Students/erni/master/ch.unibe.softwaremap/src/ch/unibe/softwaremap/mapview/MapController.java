package ch.unibe.softwaremap.mapview;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.PartInitException;

import ch.unibe.softwaremap.CodemapCore;

public class MapController {
	
	private enum State {
		UNINITIALIZED, 
		INITIALIZED
	}

	private static int serial = 0;
	
	private MapView view;
	private CodemapCore plugin;
	
	private State state = State.UNINITIALIZED;
	
	public MapController(MapView view) {
		this.view = view;
		this.plugin = CodemapCore.getPlugin();
	}
	
	public MapView getView() {
		return view;
	}

	public void onResize(Point dimension) {		
		int size =  Math.min(dimension.x, dimension.y);
		view.updateMapdimension(size);
	}
	
	public void onOpenView() {
		log("-- openView@");
	}
	
	public void onShowMap() {
		log("-- showMap@");
	}
	
	public void onProjectChanged() {
		log("-- projectChanged@");
	}
	
	public void onSelectionChanged(IJavaProject javaProject, Collection<ICompilationUnit> units) {
		log("-- selectionChanged@");
		view.compilationUnitsSelected(javaProject, units);		
	}
	
	public void onEditorOpened(EditorEvent editorEvent) {
		if (! editorEvent.hasInput()) return;				
		log("-- editorOpened(" + editorEvent.getInput().getHandleIdentifier() + ")@");
	}
	
	public void onEditorClosed(EditorEvent editorEvent) {
		if (! editorEvent.hasInput()) return;		
		log("-- editorClosed(" + editorEvent.getInput().getHandleIdentifier() + ")@");
	}
	
	/**
	 * Called whenever an editor is activated.
	 * Will load all open editor windows when called for the first time (e.g. during eclipse startup)
	 * 
	 * @param editorEvent
	 */
	public void onEditorActivated(EditorEvent editorEvent) {
		if (! editorEvent.hasInput()) return;
		if (state == State.UNINITIALIZED) {
			onFirstEditorEvent();
		}
		log("-- editorActivated(" + editorEvent.getInput().getHandleIdentifier() + ")@");
	}
	
	private void onFirstEditorEvent() {
		state = State.INITIALIZED;
		IEditorReference[] editorReferences = view.getSite().getPage().getEditorReferences();
		ArrayList<String> openTabs = new ArrayList<String>();
		for(IEditorReference ref: editorReferences) {
			EditorEvent event = new EditorEvent(ref);
			if (event.hasInput()) {
				openTabs.add(event.getInput().getHandleIdentifier());
			}
		}
		log("-- firstEditorEvent, open Tabs(" + openTabs + ")@");
	}

	private void log(String msg) {
		System.out.println(msg + serial++);
	}

}