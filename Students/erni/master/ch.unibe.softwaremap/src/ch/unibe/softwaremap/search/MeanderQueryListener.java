package ch.unibe.softwaremap.search;

import org.eclipse.search.ui.IQueryListener;
import org.eclipse.search.ui.ISearchQuery;

public class MeanderQueryListener implements IQueryListener {

	@Override
	public void queryAdded(ISearchQuery query) {
		System.out.println("query added " + query);
		query.getSearchResult().addListener(new MeanderSearchResultListener());

	}

	@Override
	public void queryFinished(ISearchQuery query) {
		System.out.println("query finished " + query);

	}

	@Override
	public void queryRemoved(ISearchQuery query) {
		System.out.println("query removed " + query);

	}

	@Override
	public void queryStarting(ISearchQuery query) {
		System.out.println("query started " + query);

	}

}
