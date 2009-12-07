package org.codemap.search;

import static org.codemap.util.CodemapIcons.SEARCH;

import org.codemap.MapPerProject;
import org.codemap.commands.SearchResultCommand;
import org.codemap.mapview.MapController;
import org.codemap.mapview.action.MenuAction;
import org.codemap.util.CodemapIcons;
import org.eclipse.jface.action.IAction;

public class ShowSearchResultsAction extends MenuAction {

    private SearchResultCommand searchResultsCommand;

    public ShowSearchResultsAction(MapController theController) {
        super("Search Results", IAction.AS_CHECK_BOX, theController);
        setImageDescriptor(CodemapIcons.descriptor(SEARCH));
        // FIXME move this to the command
        theController.getSearchResultController().registerAction(this);
    }
    
    @Override
    public void run() {
        searchResultsCommand.setShowSearchResults(isChecked());
    }
    
    @Override
    public void configureAction(MapPerProject map) {
        searchResultsCommand = map.getCommands().getSearchResultsCommand();
        searchResultsCommand.apply(this);
    }
}
