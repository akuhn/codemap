package org.codemap.mapview;

import static org.codemap.util.ArrayUtil.asArray;
import static org.codemap.util.ArrayUtil.isEmpty;

import org.codemap.util.Log;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.search.ui.ISearchQuery;
import org.eclipse.search.ui.ISearchResult;
import org.eclipse.search.ui.ISearchResultListener;
import org.eclipse.search.ui.NewSearchUI;
import org.eclipse.search.ui.SearchResultEvent;
import org.eclipse.search.ui.text.TextSearchQueryProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class SearchBar extends Composite {
    
    ISearchResultListener resultListener = new ISearchResultListener() {
        
        @Override
        public void searchResultChanged(SearchResultEvent e) {
            onQueryRunFromSelf(e.getSearchResult());
        }
    };

    private Text text;
    private MapController theController;
    private ISearchResult lastResult;

    public SearchBar(Composite parent, MapController theController) {
        super(parent, SWT.NONE);
        this.theController = theController;
        GridLayoutFactory.fillDefaults().margins(0, 3).applyTo(this);
        
        text = new Text(this, SWT.SEARCH | SWT.ICON_CANCEL);
        text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        text.addSelectionListener(new SelectionAdapter() {
            public void widgetDefaultSelected(SelectionEvent e) {
                if (e.detail == SWT.CANCEL) {
                    handleSearchCancelled();
                } else {
                    handleSearchPerformed(text.getText());
                }
            }
        });
    }

    protected void onQueryRunFromSelf(ISearchResult searchResult) {
        lastResult = searchResult;
    }
    
    /**
     * Fire a new file search for the given string in the context of the current
     * project
     * 
     * @param text to search for
     */
    protected void handleSearchPerformed(String text) {
        TextSearchQueryProvider queryProvider = TextSearchQueryProvider.getPreferred();
        try {
            IJavaProject currentJavaProject = theController.getCurrentProject();
            if (currentJavaProject == null) return;
            
            IResource currentProject = currentJavaProject.getResource();
            ISearchQuery query = queryProvider.createQuery(text, asArray(currentProject));
            NewSearchUI.runQueryInBackground(query);
            query.getSearchResult().addListener(resultListener);
        } catch (CoreException e) {
            Log.error(e);
        }
    }
    
    /**
     * if cancel is pressed remove the latest query if it was run from codemap
     */
    protected void handleSearchCancelled() {
        ISearchQuery[] queries = NewSearchUI.getQueries();
        if (isEmpty(queries)) return;
        
        ISearchQuery latest = queries[0];
        if (lastResult == null || !latest.getSearchResult().equals(lastResult)) return;
        NewSearchUI.removeQuery(latest);
        lastResult = null;
    }

    public void setMessage(String string) {
        text.setMessage(string);
    }

}
