package org.codemap.plugin.search;

import org.eclipse.search.ui.IQueryListener;
import org.eclipse.search.ui.ISearchQuery;

public class QueryListener implements IQueryListener {
	
	private final SearchResultListener searchResultListener;

	public QueryListener(SearchResultListener searchResultListener) {
		this.searchResultListener = searchResultListener;
	}

	@Override
	public void queryAdded(ISearchQuery query) {
		query.getSearchResult().addListener(searchResultListener);
	}

	@Override
	public void queryFinished(ISearchQuery query) {}

	@Override
	public void queryRemoved(ISearchQuery query) {
//		System.out.println("query removed.");
	}

	@Override
	public void queryStarting(ISearchQuery query) {
//		System.out.println("query starting");
	}

}
