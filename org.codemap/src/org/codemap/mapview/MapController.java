package org.codemap.mapview;

import java.util.ArrayList;
import java.util.Collection;

import org.codemap.CodemapCore;
import org.codemap.MapPerProject;
import org.codemap.MapPerProjectCache;
import org.codemap.MapSelection;
import org.codemap.callhierarchy.CallHierarchyTracker;
import org.codemap.eclemma.CoverageListener;
import org.codemap.eclemma.ICodemapCoverage;
import org.codemap.eclemma.NullCoverageListener;
import org.codemap.layers.CodemapVisualization;
import org.codemap.marker.MarkerController;
import org.codemap.search.SearchResultController;
import org.codemap.util.Resources;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.swt.graphics.Point;

/**
 * Single instance of the MapController for the Codemap plug-in.
 * <p>
 * Keeps a cache of MapPerProject instances, a pointer to the open MapView (if
 * any) and listens to Eclipse search queries. Contains all sub-controllers 
 * needed to track/control the different funcionality.
 * Do not use this class to store any other information. Either use the view 
 * (for UI related state) or the map instances (for project specific state).
 * </p>
 *   
 */
public class MapController {

    private MapView view;
    private IJavaProject currentProject;
    private CallHierarchyTracker callHierarchyTracker;
    private SearchResultController searchResultController;
    private MarkerController markerController;
    private SelectionTracker selectionTracker;
    private MapPerProjectCache cache;
    private ControllerUtils utils;
    private MapSelectionProvider selectionProvider;
    private ResizeListener resizeListener;
    private int currentSize;
    private AllTextUpdater textUpdater;
    private ICodemapCoverage coverageListener;

    public MapController(MapView view) {
        CodemapCore.getPlugin().register(this);        
        this.view = view;
        cache = new MapPerProjectCache(this);
        callHierarchyTracker = new CallHierarchyTracker();
        searchResultController = new SearchResultController();
        markerController = new MarkerController();
        selectionTracker = new SelectionTracker(this);
        selectionProvider = new MapSelectionProvider(view);
        // FIXME deif, move textUpdater to MapView, it should not be in the controller        
        textUpdater = new AllTextUpdater(view);
        utils = new ControllerUtils(this);
        try {
            coverageListener = new CoverageListener(this);
        } catch (NoClassDefFoundError e) {
            coverageListener = new NullCoverageListener();
        }        
    }

    public MapView getView() {
        return view;
    }


    public void onOpenView() {
        resizeListener = new ResizeListener(view.getContainer(), this);
    }

    public void onProjectSelected(IJavaProject javaProject) {
        if (currentProject == javaProject) return;
        currentProject = javaProject;
        onNewProjectSelected();
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
        CodemapCore.getPlugin().unregister(this);        
        selectionTracker.dispose();
        callHierarchyTracker.dispose();
        searchResultController.dispose();
        markerController.dispose();
        if (coverageListener != null) {
            coverageListener.dispose();
        }
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

    public SelectionTracker getSelectionTracker() {
        return selectionTracker;
    }

    public MapSelectionProvider getSelectionProvider() {
        return selectionProvider;
    }

    public void onNewProjectSelected() {
        getActiveMap().configureOn(view);
        updateVisualization();        
    }

    public void onResize(Point dimension) {     
        currentSize =  Math.min(dimension.x, dimension.y);
        updateVisualization();
    }
    
    private void updateVisualization() {
        MapPerProject activeMap = getActiveMap();
        if (activeMap == null) return;
        
        CodemapVisualization viz = activeMap
                .updateSize(currentSize)
                .getVisualization();
        textUpdater.setVisualization(viz);
        view.updateMapVisualization(viz);
    }
    
    public MapPerProject getActiveMap() {
        return mapForProject(getCurrentProject());
    }

    public MapPerProject mapForProject(IJavaProject project) {
        return cache.forProject(project);
    }

    public ControllerUtils utils() {
        return utils;
    }

    public void onSaveState() {
        cache.saveMapState();
    }

    public void onRedraw() {
        view.redraw();
    }

    public ICodemapCoverage getCoverageListener() {
        return coverageListener;
    }    

}