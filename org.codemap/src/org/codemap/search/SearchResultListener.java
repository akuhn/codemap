package org.codemap.search;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.search.ui.ISearchResultListener;
import org.eclipse.search.ui.SearchResultEvent;
import org.eclipse.search.ui.text.Match;
import org.eclipse.search.ui.text.MatchEvent;
import org.eclipse.search.ui.text.RemoveAllEvent;

/**
 * Listen for changes to an {@link org.eclipse.search.ui.ISearchResult}.
 * Filter the received events and forward the ones not filtered to
 * {@link org.codemap.plugin.search.SearchResultController}.
 * 
 * @author deif
 */
public class SearchResultListener implements ISearchResultListener {
	
	private SearchResultController theController;

	public SearchResultListener(SearchResultController controller) {
		theController = controller;
	}

	@Override
	public void searchResultChanged(SearchResultEvent e) {
		if (e instanceof MatchEvent) {
			handleMatchEvent((MatchEvent) e);
		}
		if (e instanceof RemoveAllEvent) {
			handleRemoveAllEvent();
		}
	}

	private void handleRemoveAllEvent() {
		theController.onAllQueriesRemoved();
	}

	private void handleMatchEvent(MatchEvent me) {
		switch (me.getKind()) {
		case MatchEvent.ADDED:
			theController.onElementsAdded(extractElements(me));
			break;
		case MatchEvent.REMOVED:
			theController.onElementsRemoved(extractElements(me));
			break;
		}
	}

	private Collection<Object> extractElements(MatchEvent me) {
		Collection<Object> elements = new ArrayList<Object>();
		for(Match each: me.getMatches()) {
			elements.add(each.getElement());
		}
		return elements;
	}
}
