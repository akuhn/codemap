package org.codemap.mapview;

import java.util.ArrayList;
import java.util.Collection;

import org.codemap.CodemapCore;
import org.codemap.MapSelection;
import org.codemap.callhierarchy.CallHierarchyTracker;
import org.codemap.marker.MarkerController;
import org.codemap.search.SearchResultController;
import org.codemap.util.Resources;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.swt.graphics.Point;


public class MapController {

    private MapView view;
    private IJavaProject currentProject;
    private CallHierarchyTracker callHierarchyTracker;
    private SearchResultController searchResultController;
    private MarkerController markerController;
        

    public MapController(MapView view) {
        this.view = view;
        callHierarchyTracker = new CallHierarchyTracker();
        searchResultController = new SearchResultController();
        markerController = new MarkerController();
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
        currentProject = javaProject;
        view.newProjectSelected();
    }

    public void onSelectionChanged(Collection<ICompilationUnit> units) {
        Collection<String> newPaths = new ArrayList<String>();
        for (ICompilationUnit each: units) {
            newPaths.add(Resources.asPath(each));
        }
        getCurrentSelection().replaceAll(newPaths);
    }

    private MapSelection getCurrentSelection() {
        return CodemapCore.getPlugin().getCurrentSelection();
    }

    public IJavaProject getCurrentProject() {
        return currentProject;
    }

    public void dispose() {
        callHierarchyTracker.dispose();
        searchResultController.dispose();
        markerController.dispose();        
    }

    public CallHierarchyTracker getCallHierarchyTracker() {
        return callHierarchyTracker;
    }

    public SearchResultController getSearchResultController() {
        return searchResultController;
    }

    public MarkerController getMarkerController() {
        return markerController;
    }	

}