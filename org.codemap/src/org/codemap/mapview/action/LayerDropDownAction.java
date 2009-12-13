package org.codemap.mapview.action;

import static org.codemap.util.CodemapIcons.LAYERS;

import org.codemap.util.CodemapIcons;
import org.eclipse.swt.widgets.Menu;

public class LayerDropDownAction extends DropDownAction {
    
    private ShowSearchResultsAction showSearchResultsAction;
    private LinkWithCallHierarchyAction linkWithCallHierarchyAction;
    private ShowMarkersAction showMarkersAction;
    private ShowYouAreHereAction showYouAreHere;

    public LayerDropDownAction(ActionStore actionStore) {
        super();
        showSearchResultsAction = new ShowSearchResultsAction(actionStore);
        linkWithCallHierarchyAction = new LinkWithCallHierarchyAction(actionStore);
        showMarkersAction = new ShowMarkersAction(actionStore);
        showYouAreHere = new ShowYouAreHereAction(actionStore);
    }

    @Override
    protected void createMenu(Menu menu) {
        addActionToMenu(menu, showSearchResultsAction);
        addActionToMenu(menu, linkWithCallHierarchyAction);
        addActionToMenu(menu, showMarkersAction);
        addActionToMenu(menu, showYouAreHere);
    }

    @Override
	protected void setup() {
	    setImageDescriptor(CodemapIcons.descriptor(LAYERS));
		setText("Layers"); 
	}
}
