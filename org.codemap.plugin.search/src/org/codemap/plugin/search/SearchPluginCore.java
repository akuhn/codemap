package org.codemap.plugin.search;

import org.codemap.CodemapCore;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.search.ui.ISearchQuery;
import org.eclipse.search.ui.ISearchResult;
import org.eclipse.search.ui.ISearchResultViewPart;
import org.eclipse.search.ui.NewSearchUI;
import org.eclipse.search2.internal.ui.SearchMessages;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import ch.deif.meander.MapSelection;

/**
 * The activator class controls the plug-in life cycle
 */
public class SearchPluginCore extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.codemap.plugin.search";

	// The shared instance
	private static SearchPluginCore plugin;

	private QueryListener queryListener;

	private SearchResultsOverlay searchResultsOverlay;

	private MapSelection searchSelection;
	
	/**
	 * The constructor
	 */
	public SearchPluginCore() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		
		init();
		registerQueryListener();
		handleOldQueries();
	}

	private void handleOldQueries() {
		ISearchQuery[] queries = NewSearchUI.getQueries();
		if (queries.length == 0) return;
		
		appendListeners(queries);
		// new queries are at position 0
		reRunNewestQuery(queries[0]);
	}

	private void reRunNewestQuery(ISearchQuery query) {
		// copy-paste from package org.eclipse.search2.internal.ui.SearchAgainAction#run;
		NewSearchUI.cancelQuery(query);
		ISearchResultViewPart fView = NewSearchUI.getSearchResultView();
		if (query.canRerun()) {
			if (query.canRunInBackground())
				NewSearchUI.runQueryInBackground(query, fView);
			else {
				Shell shell= fView.getSite().getShell();
				ProgressMonitorDialog pmd= new ProgressMonitorDialog(shell);
				IStatus status= NewSearchUI.runQueryInForeground(pmd, query, fView);
				if (!status.isOK() && status.getSeverity() != IStatus.CANCEL) {
					ErrorDialog.openError(shell, "Search Again", "Problems while searching again", status);
				}
			}
		}
	}

	private void appendListeners(ISearchQuery[] queries) {
		for(ISearchQuery each: queries) {
			each.getSearchResult().addListener(new SearchResultListener());
		}
	}

	private void init() {
		searchSelection = new MapSelection();
		searchResultsOverlay = new SearchResultsOverlay();
		searchResultsOverlay.setSelection(searchSelection);
		CodemapCore.getPlugin().addLayer(searchResultsOverlay);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		unregisterQueryListener();
		destroy();
		super.stop(context);
	}

	private void destroy() {
		plugin = null;
		CodemapCore.getPlugin().removeLayer(searchResultsOverlay);
		searchResultsOverlay = null;
		searchSelection = null;
	}
	
	public MapSelection getSearchSelection() {
		return searchSelection;
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static SearchPluginCore getPlugin() {
		return plugin;
	}

	private void registerQueryListener() {
		SearchResultListener searchResultListener = new SearchResultListener();
		queryListener = new QueryListener(searchResultListener);
		NewSearchUI.addQueryListener(queryListener);
	}

	private void unregisterQueryListener() {
		NewSearchUI.removeQueryListener(queryListener);
	}	
}
