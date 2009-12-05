package org.codemap.search;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import org.codemap.CodemapCore;
import org.codemap.MapPerProject;
import org.codemap.MapSelection;
import org.codemap.util.Resources;
import org.eclipse.search.ui.ISearchQuery;
import org.eclipse.search.ui.ISearchResult;
import org.eclipse.search.ui.NewSearchUI;
import org.eclipse.search.ui.text.AbstractTextSearchResult;


public class SearchResultController {

    private SearchResultListener searchListener;
    private ShowSearchResultsAction action;
    private MapSelection mapSelection;
    private QueryListener theQueryListener;

    public SearchResultController() {
        searchListener = new SearchResultListener(this);
        mapSelection = new MapSelection();
        theQueryListener = new QueryListener(this);
        registerQueryListener();        
    }

    private void registerQueryListener() {
        NewSearchUI.addQueryListener(theQueryListener);
    }
    
    private void unregisterQueryListener() {
        NewSearchUI.removeQueryListener(theQueryListener);
    }       

    public void onLayerActivated() {
        ISearchQuery[] queries = NewSearchUI.getQueries();
        if (queries.length == 0) return;

        // TODO check if this is necessary.
        addListener(queries);
        // new queries are at the first position
        loadNewestQueryResult(queries);
    }

    private boolean isActive() {
        if (action == null) return false;
        return action.isChecked();
    }

    public void onLayerDeactivated() {
        clearSelection();
    }

    public void onQueryAdded(ISearchQuery query) {
        query.getSearchResult().addListener(searchListener);
    }

    public void onAllQueriesRemoved() {
        clearSelection();
    }

    private void clearSelection() {
        getSearchSelection().clear();
    }

    public void onElementsAdded(Collection<Object> elements) {
        if (!isActive()) return;
        getSearchSelection().addAll(extractMatches(elements));
    }

    public void onElementsRemoved(Collection<Object> elements) {
        getSearchSelection().removeAll(extractMatches(elements));
        // FIXME might cause redraws even when not enabled?!?
    }

    private void loadNewestQueryResult(ISearchQuery[] queries) {
        // new queries are added at first position
        ISearchQuery query = queries[0];
        ISearchResult searchResult = query.getSearchResult();
        if (searchResult instanceof AbstractTextSearchResult) {
            AbstractTextSearchResult res = (AbstractTextSearchResult) searchResult;
            onElementsAdded(Arrays.asList(res.getElements()));
        }		
    }

    private void addListener(ISearchQuery[] queries) {
        for(ISearchQuery each: queries) {
            onQueryAdded(each);
        }
    }

    protected MapSelection getSearchSelection() {
        MapPerProject activeMap = CodemapCore.getPlugin().getActiveMap();
        if (! activeMap.containsLayer(SearchResultsOverlay.class)) {
            SearchResultsOverlay searchOverlay = new SearchResultsOverlay(this);
            activeMap.addSelectionLayer(searchOverlay, mapSelection);
        }
        return mapSelection;           
    }

    private Collection<String> extractMatches(Collection<Object> elements) {
        Collection<String> idents = new HashSet<String>();
        for (Object each: elements) {
            String path = Resources.asPath(each);
            if (path != null) idents.add(path);
        }
        return idents;		
    }

    public void registerAction(ShowSearchResultsAction showSearchResultsAction) {
        action = showSearchResultsAction;
    }

    public void dispose() {
        unregisterQueryListener();
    }

}
