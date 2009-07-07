package ch.unibe.softwaremap.mapview;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.IEditorReference;

import ch.deif.meander.MapSelection;
import ch.unibe.softwaremap.CodemapCore;
import ch.unibe.softwaremap.MapPerProject;

public class MapController {
	
	private enum State {
		UNINITIALIZED, 
		INITIALIZED
	}

	private static int serial = 0;
	
	private MapView view;
	private CodemapCore plugin;
	
	private State state = State.UNINITIALIZED;

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
	
	public void onProjectChanged() {
		log("-- projectChanged@");
	}
	
	public void onSelectionChanged(IJavaProject javaProject, Collection<ICompilationUnit> units) {
		log("-- selectionChanged@");
		view.compilationUnitsSelected(javaProject, units);
	}
	
	public void onEditorOpened(EditorEvent editorEvent) {
		if (! editorEvent.hasInput()) return;

		IJavaElement javaElement = editorEvent.getInput();
		if (!(javaElement instanceof ICompilationUnit)) return;		
		// TODO merge this with other methods
		
		compilationUnitOpen((ICompilationUnit)javaElement);
		log("-- editorOpened(" + editorEvent.getInput().getHandleIdentifier() + ")@");
	}

	public void onEditorClosed(EditorEvent editorEvent) {
		if (! editorEvent.hasInput()) return;
		
		IJavaElement javaElement = editorEvent.getInput();
		if (!(javaElement instanceof ICompilationUnit)) return;		
		// TODO merge this with other methods
		
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
		
		youAreHereChanged((ICompilationUnit)javaElement);
//		view.updateVisualization();
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
			compilationUnitsOpen(units);				
		}
		log("-- firstEditorEvent@");
	}
	
	private void youAreHereChanged(ICompilationUnit unit) {
		getYouAreHereSelection().clear().add(unit.getHandleIdentifier());
	}	

	private void compilationUnitsOpen(Set<ICompilationUnit> units) {
		for(ICompilationUnit unit: units) {
			compilationUnitOpen(unit);
		}
	}
	
	private void compilationUnitOpen(ICompilationUnit unit) {
		getOpenFilesSelection().add(unit.getHandleIdentifier());
	}
	
	private void compilationUnitClosed(ICompilationUnit unit) {
		getYouAreHereSelection().remove(unit.getHandleIdentifier());
		getOpenFilesSelection().remove(unit.getHandleIdentifier());
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
		System.out.println(msg + serial++);
	}
	
	private MapSelection getYouAreHereSelection() {
		return CodemapCore.getPlugin().getYouAreHereSelection();
	}
	
	private MapSelection getOpenFilesSelection() {
		return CodemapCore.getPlugin().getOpenFilesSelection();
	}	

}