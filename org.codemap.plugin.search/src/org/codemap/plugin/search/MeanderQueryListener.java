package org.codemap.plugin.search;

import org.eclipse.search.ui.IQueryListener;
import org.eclipse.search.ui.ISearchQuery;

public class MeanderQueryListener implements IQueryListener {

	@Override
	public void queryAdded(ISearchQuery query) {
		query.getSearchResult().addListener(new MeanderSearchResultListener());
	}

	@Override
	public void queryFinished(ISearchQuery query) {}

	@Override
	public void queryRemoved(ISearchQuery query) {}

	@Override
	public void queryStarting(ISearchQuery query) {}

}
