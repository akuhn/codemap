package org.codemap.mapview;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.codemap.CodemapCore;
import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.IEditorReference;

import ch.deif.meander.MapSelection;

public class MapController {
	
	private enum State {
		UNINITIALIZED, 
		INITIALIZED
	}

	private static int serial = 0;
	
	private MapView view;
	private CodemapCore plugin;
	
	private State state = State.UNINITIALIZED;
	private IJavaProject currentProject;

	// FIXME: move this, maybe to MapPerProjec
	
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
	
	public void onProjectSelected(IJavaProject javaProject) {
		log("-- projectSelected@");
		if (currentProject == javaProject) return;
		
		currentProject = javaProject;
		view.onProjectSelectionChanged(currentProject);
		redrawCodemap();			
	}
	
	public void onSelectionChanged(Collection<ICompilationUnit> units) {
		log("-- selectionChanged@");
		MapSelection selection = getCurrentSelection().clear();
		for (ICompilationUnit each: units) {
			selection.add(each.getHandleIdentifier());
		} 
		redrawCodemap();
	}
	
	public void onEditorOpened(EditorEvent editorEvent) {
		if (! editorEvent.hasInput()) return;
		IJavaElement javaElement = editorEvent.getInput();
		if (!(javaElement instanceof ICompilationUnit)) return;		
		
		compilationUnitOpened((ICompilationUnit)javaElement);
		log("-- editorOpened(" + editorEvent.getInput().getHandleIdentifier() + ")@");
	}

	public void onEditorClosed(EditorEvent editorEvent) {
		if (! editorEvent.hasInput()) return;
		IJavaElement javaElement = editorEvent.getInput();
		if (!(javaElement instanceof ICompilationUnit)) return;		
		
		compilationUnitClosed((ICompilationUnit)javaElement);
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
		IJavaElement javaElement = editorEvent.getInput();
		if (!(javaElement instanceof ICompilationUnit)) return;
		
		onProjectSelected(javaElement.getJavaProject());
//		view.onProjectSelectionChanged();
		youAreHereChanged((ICompilationUnit) javaElement);
		log("-- editorActivated(" + editorEvent.getInput().getHandleIdentifier() + ")@");
	}
	
	/**
	 *  Initializes the map with the CompilationUnits that are displayed in the tab-bar of the editor.  
	 */
	private void onFirstEditorEvent() {
		state = State.INITIALIZED;
		HashMap<IJavaProject,Set<ICompilationUnit>> selections = new HashMap<IJavaProject,Set<ICompilationUnit>>();
		selections = new HashMap<IJavaProject, Set<ICompilationUnit>>();
		
		IEditorReference[] editorReferences = view.getSite().getPage().getEditorReferences();
		for(IEditorReference ref: editorReferences) {
			EditorEvent event = new EditorEvent(ref);
			if (! event.hasInput()) continue;
			
			IJavaElement javaElement = event.getInput();
			if (!(javaElement instanceof ICompilationUnit)) continue;
			
			IJavaProject javaProject = javaElement.getJavaProject();
			Set<ICompilationUnit> units = getUnitsForProject(selections, javaProject);
			units.add((ICompilationUnit) javaElement);				
		}
		
		for(IJavaProject each: selections.keySet()) {
			Set<ICompilationUnit> units = selections.get(each);
			compilationUnitsOpened(units);				
		}
		log("-- firstEditorEvent@");
	}
	
	private void youAreHereChanged(ICompilationUnit unit) {
		getYouAreHereSelection().clear().add(unit.getHandleIdentifier());
		redrawCodemap();
	}	

	private void compilationUnitsOpened(Set<ICompilationUnit> units) {
		for(ICompilationUnit unit: units) {
			compilationUnitOpened(unit);
		}
	}
	
	private void compilationUnitOpened(ICompilationUnit unit) {
		getOpenFilesSelection().add(unit.getHandleIdentifier());
		redrawCodemap();
	}
	
	private void compilationUnitClosed(ICompilationUnit unit) {
		getYouAreHereSelection().remove(unit.getHandleIdentifier());
		getOpenFilesSelection().remove(unit.getHandleIdentifier());
		redrawCodemap();
	}	

	private Set<ICompilationUnit> getUnitsForProject(HashMap<IJavaProject,Set<ICompilationUnit>> selections, IJavaProject javaProject) {
		Set<ICompilationUnit> units = selections.get(javaProject);
		if (units == null) {
			units = new HashSet<ICompilationUnit>();
			selections.put(javaProject, units);
		}
		return units;
	}

	private void log(String msg) {
//		do not log at the moment.
//		System.out.println(msg + serial++);
	}
	
	private MapSelection getYouAreHereSelection() {
		return CodemapCore.getPlugin().getYouAreHereSelection();
	}
	
	private MapSelection getOpenFilesSelection() {
		return CodemapCore.getPlugin().getOpenFilesSelection();
	}
	
	private MapSelection getCurrentSelection() {
		return CodemapCore.getPlugin().getCurrentSelection();
	}

	private void redrawCodemap() {
		CodemapCore.getPlugin().redrawCodemap();
	}

	public IJavaProject getCurrentProject() {
		return currentProject;
	}	

}