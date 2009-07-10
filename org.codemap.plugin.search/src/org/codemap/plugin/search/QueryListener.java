package org.codemap.plugin.search;

import org.eclipse.search.ui.IQueryListener;
import org.eclipse.search.ui.ISearchQuery;

public class QueryListener implements IQueryListener {

	@Override
	public void queryAdded(ISearchQuery query) {
		query.getSearchResult().addListener(new SearchResultListener());
	}

	@Override
	public void queryFinished(ISearchQuery query) {}

	@Override
	public void queryRemoved(ISearchQuery query) {
		System.out.println("query removed.");
	}

	@Override
	public void queryStarting(ISearchQuery query) {
		System.out.println("query starting");
	}

}
