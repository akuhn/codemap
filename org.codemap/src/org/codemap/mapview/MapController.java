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
	
	private MapView view;
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
		//
	}
	
	public void onShowMap() {
		//
	}
	
	public void onProjectSelected(IJavaProject javaProject) {
		if (currentProject == javaProject) return;
		// TODO show 'Create map...' button of there is not default.map
		currentProject = javaProject;
		view.updateVisualization();
	}
	
	public void onSelectionChanged(Collection<ICompilationUnit> units) {
		MapSelection selection = getCurrentSelection().clear();
		for (ICompilationUnit each: units) {
			selection.add(Resources.asPath(each));
		} 
		redrawCodemap();
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