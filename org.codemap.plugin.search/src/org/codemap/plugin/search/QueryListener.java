package org.codemap.plugin.search;

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
	public void queryFinished(ISearchQuery query) {}

	@Override
	public void queryRemoved(ISearchQuery query) {}

	@Override
	public void queryStarting(ISearchQuery query) {}

}
