package org.codemap.plugin.search;

import org.codemap.mapview.ICodemapPluginAction;
import org.eclipse.jface.action.Action;

// XXX factor out searchResultsController which can: 
//		- remember old results and enable/disable them on request.
//		- load the results from older queries.
public class ShowSearchResultsAction implements ICodemapPluginAction {
	
	private Action action;

	@Override
	public void run(Action act) {
		action = act;
		if (action.isChecked()) showSearchResults();
		else hideSearchResults();
	}

	private void hideSearchResults() {
		SearchPluginCore.getPlugin().getSearchSelection().clear();
	}

	private void showSearchResults() {
		
	}

	public boolean isChecked() {
		if (action == null) return false;
		return action.isChecked();
	}

}
