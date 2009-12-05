package org.codemap.mapview.action;

import static org.codemap.util.CodemapIcons.LAYERS;

import org.codemap.callhierarchy.CallHierarchyTracker;
import org.codemap.callhierarchy.LinkWithCallHierarchyAction;
import org.codemap.mapview.MapController;
import org.codemap.marker.MarkerController;
import org.codemap.marker.ShowMarkersAction;
import org.codemap.search.SearchResultController;
import org.codemap.search.ShowSearchResultsAction;
import org.codemap.util.CodemapIcons;
import org.codemap.util.ExtensionPoints;
import org.eclipse.swt.widgets.Menu;

public class LayerDropDownAction extends ExtensionPointDropDownAction {
    
    private MapController theController;

    public LayerDropDownAction(MapController controller) {
        theController = controller;
    }

    @Override
    protected void createMenu(Menu menu) {
        super.createMenu(menu);
        addActionToMenu(menu, new ShowSearchResultsAction(theController));
        addActionToMenu(menu, new LinkWithCallHierarchyAction(theController));
        addActionToMenu(menu, new ShowMarkersAction(theController));
    }

    @Override
	protected void setup() {
	    setImageDescriptor(CodemapIcons.descriptor(LAYERS));
		setText("Layers"); 
	}

	@Override
	protected String getExtensionPointName() {
		return ExtensionPoints.LAYERS;
	}

	@Override
	protected int getActionStyle() {
		return AS_CHECK_BOX;
	}

}
