package org.codemap.commands;

import static org.codemap.commands.Commands.makeCommandId;

import org.codemap.CodemapCore;
import org.codemap.MapPerProject;
import org.codemap.commands.Commands.Command;
import org.codemap.search.SearchResultController;
import org.codemap.search.ShowSearchResultsAction;

public class SearchResultCommand extends Command {

    private static final String SEARCH_RESULTS_KEY = makeCommandId("search_results");
    private boolean showSearchResults;

    public SearchResultCommand(MapPerProject mapPerProject) {
        super(mapPerProject);
        showSearchResults = getMyMap().getPropertyOrDefault(SEARCH_RESULTS_KEY, true);
    }

    public void setShowSearchResults(boolean checked) {
        showSearchResults = checked;
        applyState();
    }
    
    private void applyState() {
        if (showSearchResults) showSearchResults();
        else hideSearchResults();
        getMyMap().setProperty(SEARCH_RESULTS_KEY, showSearchResults);
    }

    private void hideSearchResults() {
        getSearchResultController().onLayerDeactivated();
    }

    private void showSearchResults() {
        getSearchResultController().onLayerActivated();
    } 
    
    protected SearchResultController getSearchResultController() {
        return CodemapCore.getPlugin().getController().getSearchResultController();
    }

    public void apply(ShowSearchResultsAction showSearchResultsAction) {
        showSearchResultsAction.setChecked(showSearchResults);
        applyState();
    }    
}
