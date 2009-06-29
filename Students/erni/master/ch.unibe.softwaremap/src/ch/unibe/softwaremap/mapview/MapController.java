package ch.unibe.softwaremap.mapview;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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

	// FIXME: move this, maybe to MapPerProject
	private HashMap<IJavaProject,Set<ICompilationUnit>> selections;
	
	public MapController(MapView view) {
		this.view = view;
		this.plugin = CodemapCore.getPlugin();
		
		selections = new HashMap<IJavaProject, Set<ICompilationUnit>>();		
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
//		currently disable the normal selection
//		view.compilationUnitsSelected(javaProject, units);		
	}
	
	public void onEditorOpened(EditorEvent editorEvent) {
		if (! editorEvent.hasInput()) return;

		IJavaElement javaElement = editorEvent.getInput();
		if (!(javaElement instanceof ICompilationUnit)) return;		
		// TODO merge this with other methods
		IJavaProject javaProject = javaElement.getJavaProject();
		Set<ICompilationUnit> units = getUnitsForProject(javaProject);
		units.add((ICompilationUnit) javaElement);		
		
		view.compilationUnitsSelected(javaProject, units);
		log("-- editorOpened(" + editorEvent.getInput().getHandleIdentifier() + ")@");
	}
	
	public void onEditorClosed(EditorEvent editorEvent) {
		if (! editorEvent.hasInput()) return;
		
		IJavaElement javaElement = editorEvent.getInput();
		if (!(javaElement instanceof ICompilationUnit)) return;		
		// TODO merge this with other methods
		IJavaProject javaProject = javaElement.getJavaProject();
		Set<ICompilationUnit> units = getUnitsForProject(javaProject);
		units.remove((ICompilationUnit) javaElement);		
		
		view.compilationUnitsSelected(javaProject, units);
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
	
	/**
	 *  Initializes the map with the CompilationUnits that are displayed in the tab-bar of the editor.  
	 */
	private void onFirstEditorEvent() {
		state = State.INITIALIZED;
		
		selections = new HashMap<IJavaProject, Set<ICompilationUnit>>();
		
		IEditorReference[] editorReferences = view.getSite().getPage().getEditorReferences();
		for(IEditorReference ref: editorReferences) {
			EditorEvent event = new EditorEvent(ref);
			if (! event.hasInput()) continue;
			
			IJavaElement javaElement = event.getInput();
			if (!(javaElement instanceof ICompilationUnit)) continue;
			
			IJavaProject javaProject = javaElement.getJavaProject();
			Set<ICompilationUnit> units = getUnitsForProject(javaProject);
			units.add((ICompilationUnit) javaElement);				
		}
		
		for(IJavaProject each: selections.keySet()) {
			Set<ICompilationUnit> units = selections.get(each);
			view.compilationUnitsSelected(each, units);				
		}
		log("-- firstEditorEvent@");
	}

	private Set<ICompilationUnit> getUnitsForProject(IJavaProject javaProject) {
		Set<ICompilationUnit> units = selections.get(javaProject);
		if (units == null) {
			units = new HashSet<ICompilationUnit>();
			selections.put(javaProject, units);
		}
		return units;
	}

	private void log(String msg) {
		System.out.println(msg + serial++);
	}

}