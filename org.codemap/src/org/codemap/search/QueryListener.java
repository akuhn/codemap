package org.codemap.search;

import org.eclipse.search.ui.IQueryListener;
import org.eclipse.search.ui.ISearchQuery;

/**
 * Listen for query-added events and forward each new query
 * {@link org.codemap.plugin.search.SearchResultController } for further
 * processing.
 * 
 * @author deif
 */
public class QueryListener implements IQueryListener {

    private SearchResultController theController;

    public QueryListener(SearchResultController controller) {
        theController = controller;
    }

    @Override
    public void queryAdded(ISearchQuery query) {
        theController.onQueryAdded(query);
    }

    @Override
    public void queryFinished(ISearchQuery query) {
        // empty block
    }

    @Override
    public void queryRemoved(ISearchQuery query) {
        theController.onQueryRemoved(query);
    }

    @Override
    public void queryStarting(ISearchQuery query) {
        // TODO does that re-start a query? in that case, remove old entries as well...
    }

}
