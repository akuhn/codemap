package org.codemap.commands;

import static org.codemap.commands.CompositeCommand.makeCommandId;

import org.codemap.CodemapCore;
import org.codemap.MapPerProject;
import org.codemap.mapview.MapView;
import org.codemap.mapview.action.CommandAction;
import org.codemap.mapview.action.ShowSearchResultsAction;
import org.codemap.resources.MapValues;
import org.codemap.search.SearchResultController;

public class SearchResultCommand extends CheckedCommand {

    private static final String SEARCH_RESULTS_KEY = makeCommandId("search_results");

    public SearchResultCommand(MapPerProject mapPerProject) {
        super(mapPerProject);
    }

    @Override
    protected void applyState() {
        if (isEnabled()) showSearchResults();
        else hideSearchResults();
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

    @Override
    protected String getKey() {
        return SEARCH_RESULTS_KEY;
    }

    @Override
    protected Class<? extends CommandAction> getActionID() {
        return ShowSearchResultsAction.class;
    }

    @Override
    public void configure(MapValues mapValues) {
        applyState();
    }
    
    @Override
    public void configure(MapView view) {
        getSearchResultController().setCurrentCommand(this);
        super.configure(view);
    }
}
