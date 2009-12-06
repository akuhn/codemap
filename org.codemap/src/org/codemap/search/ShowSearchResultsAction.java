package org.codemap.search;

import static org.codemap.util.CodemapIcons.SEARCH;

import org.codemap.mapview.MapController;
import org.codemap.mapview.action.MenuAction;
import org.codemap.util.CodemapIcons;
import org.eclipse.jface.action.IAction;

public class ShowSearchResultsAction extends MenuAction {

    public ShowSearchResultsAction(MapController theController) {
        super("Search Results", IAction.AS_CHECK_BOX, theController);
        setImageDescriptor(CodemapIcons.descriptor(SEARCH));
        theController.getSearchResultController().registerAction(this);
    }
    
    protected SearchResultController getSearchResultController() {
        return getController().getSearchResultController();
    }

	@Override
	public void run() {
	    super.run();
		if (isChecked()) showSearchResults();
		else hideSearchResults();
	}
	
	@Override
	public void setChecked(boolean checked) {
	    super.setChecked(checked);
	}

	private void hideSearchResults() {
		getSearchResultController().onLayerDeactivated();
	}

	private void showSearchResults() {
		getSearchResultController().onLayerActivated();
	}

    @Override
    protected String getKey() {
        return "show_search_results";
    }

    @Override
    protected boolean isDefaultChecked() {
        return true;
    }
}
