package org.codemap.mapview.action;

import static org.codemap.util.CodemapIcons.LAYERS;

import org.codemap.util.CodemapIcons;
import org.eclipse.swt.widgets.Menu;

public class LayerDropDownAction extends DropDownAction {
    
    private ShowSearchResultsAction showSearchResultsAction;
    private LinkWithCallHierarchyAction linkWithCallHierarchyAction;
    private ShowMarkersAction showMarkersAction;
    private ShowYouAreHereAction showYouAreHere;
    private ShowOpenFilesAction showOpenFiles;
    private ShowSelectionAction showSelection;

    public LayerDropDownAction(ActionStore actionStore) {
        super();
        showSearchResultsAction = new ShowSearchResultsAction(actionStore);
        linkWithCallHierarchyAction = new LinkWithCallHierarchyAction(actionStore);
        showMarkersAction = new ShowMarkersAction(actionStore);
        showYouAreHere = new ShowYouAreHereAction(actionStore);
        showOpenFiles = new ShowOpenFilesAction(actionStore);
        showSelection = new ShowSelectionAction(actionStore);
    }

    @Override
    protected void createMenu(Menu menu) {
        addActionToMenu(menu, showSearchResultsAction);
        addActionToMenu(menu, linkWithCallHierarchyAction);
        addActionToMenu(menu, showMarkersAction);
        addActionToMenu(menu, showYouAreHere);
        addActionToMenu(menu, showOpenFiles);
        addActionToMenu(menu, showSelection);
    }

    @Override
	protected void setup() {
	    setImageDescriptor(CodemapIcons.descriptor(LAYERS));
		setText("Layers"); 
	}
}
