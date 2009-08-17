package org.codemap.plugin.search;

import org.codemap.mapview.ICodemapPluginAction;
import org.eclipse.jface.action.Action;

public class ShowSearchResultsAction implements ICodemapPluginAction {
	
	private Action action;
	private SearchResultController controller;
	
	public ShowSearchResultsAction() {
		controller = SearchPlugin.getPlugin().getController();
		controller.registerAction(this);
	}

	@Override
	public void run(Action act) {
		action = act;
		if (action.isChecked()) showSearchResults();
		else hideSearchResults();
	}

	private void hideSearchResults() {
		controller.onLayerDeactivated();
	}

	private void showSearchResults() {
		controller.onLayerActivated();
	}

	public boolean isChecked() {
		if (action == null) return false;
		return action.isChecked();
	}

}
