package org.codemap.mapview;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.codemap.CodemapCore;
import org.codemap.util.Resources;
import org.eclipse.core.resources.IResource;
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

	private MapView view;
	private State state = State.UNINITIALIZED;
	private IJavaProject currentProject;

	public MapController(MapView view) {
		this.view = view;
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
		// TODO show 'Create map...' button of there is not default.map
		currentProject = javaProject;
		view.updateVisualization();
	}
	
	public void onSelectionChanged(Collection<ICompilationUnit> units) {
		log("-- selectionChanged@");
		MapSelection selection = getCurrentSelection().clear();
		for (ICompilationUnit each: units) {
			selection.add(Resources.asPath(each));
		} 
		redrawCodemap();
	}
	
	public void onEditorOpened(EditorEvent editorEvent) {
		if (! editorEvent.hasInput()) return;
		IJavaElement javaElement = editorEvent.getInput();
		compilationUnitOpened(javaElement.getResource()); // TODO FIXME make EditorEvent use resources instead of java elements
		log("-- editorOpened(" + editorEvent.getInput().getHandleIdentifier() + ")@");
	}

	public void onEditorClosed(EditorEvent editorEvent) {
		if (! editorEvent.hasInput()) return;
		IJavaElement javaElement = editorEvent.getInput();
		compilationUnitClosed(javaElement.getResource()); // TODO FIXME make EditorEvent use resources instead of java elements
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
		onProjectSelected(javaElement.getJavaProject());
		youAreHereChanged(javaElement.getResource()); // TODO FIXME make EditorEvent use resources instead of java elements
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
	
	private void youAreHereChanged(IResource resource) {
		getYouAreHereSelection().clear().add(Resources.asPath(resource));
		redrawCodemap();
	}	

	private void compilationUnitsOpened(Set<ICompilationUnit> units) {
		for(ICompilationUnit unit: units) {
			compilationUnitOpened(unit.getResource());
		}
	}
	
	private void compilationUnitOpened(IResource resource) {
		getOpenFilesSelection().add(Resources.asPath(resource));
		redrawCodemap();
	}
	
	private void compilationUnitClosed(IResource resource) {
		getYouAreHereSelection().remove(Resources.asPath(resource));
		getOpenFilesSelection().remove(Resources.asPath(resource));
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