package org.codemap.mapview.action;

import static org.codemap.util.CodemapIcons.LAYERS;

import org.codemap.callhierarchy.LinkWithCallHierarchyAction;
import org.codemap.mapview.MapController;
import org.codemap.marker.ShowMarkersAction;
import org.codemap.search.ShowSearchResultsAction;
import org.codemap.util.CodemapIcons;
import org.codemap.util.ExtensionPoints;
import org.eclipse.swt.widgets.Menu;

public class LayerDropDownAction extends ExtensionPointDropDownAction {
    
    private ShowSearchResultsAction showSearchResultsAction;
    private LinkWithCallHierarchyAction linkWithCallHierarchyAction;
    private ShowMarkersAction showMarkersAction;

    public LayerDropDownAction(MapController theController) {
        super(theController);
        registerAction(showSearchResultsAction = new ShowSearchResultsAction(getController()));
        registerAction(linkWithCallHierarchyAction = new LinkWithCallHierarchyAction(getController()));
        registerAction(showMarkersAction = new ShowMarkersAction(getController()));        
    }

    @Override
    protected void createMenu(Menu menu) {
        super.createMenu(menu);
        addActionToMenu(menu, showSearchResultsAction);
        addActionToMenu(menu, linkWithCallHierarchyAction);
        addActionToMenu(menu, showMarkersAction);
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
