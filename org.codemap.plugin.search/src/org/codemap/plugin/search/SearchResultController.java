package org.codemap.plugin.search;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import org.codemap.CodemapCore;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.search.ui.ISearchQuery;
import org.eclipse.search.ui.ISearchResult;
import org.eclipse.search.ui.NewSearchUI;
import org.eclipse.search.ui.text.AbstractTextSearchResult;

import ch.deif.meander.MapSelection;

public class SearchResultController {
	
	private SearchResultListener searchListener;
	private ShowSearchResultsAction action;

	public SearchResultController() {
		searchListener = new SearchResultListener(this);
	}

	public void onLayerActivated() {
		ISearchQuery[] queries = NewSearchUI.getQueries();
		if (queries.length == 0) return;
		
		// TODO check if this is necessary.
		addListener(queries);
		// new queries are at the first position
		loadNewestQueryResult(queries);
		issueRedraw();
	}
	
	private boolean isActive() {
		if (action == null) return false;
		return action.isChecked();
	}
	
	public void onLayerDeactivated() {
		clearSelection();
	}

	public void onQueryAdded(ISearchQuery query) {
		query.getSearchResult().addListener(searchListener);
	}

	public void onAllQueriesRemoved() {
		clearSelection();
	}

	private void clearSelection() {
		getSearchSelection().clear();
		issueRedraw();
	}

	public void onElementsAdded(Collection<Object> elements) {
		if (!isActive()) return;
		getSearchSelection().addAll(extractMatches(elements));
		issueRedraw();
	}

	public void onElementsRemoved(Collection<Object> elements) {
		getSearchSelection().removeAll(extractMatches(elements));
		if (!isActive()) return;		
		issueRedraw();
	}

	private void issueRedraw() {
		CodemapCore.getPlugin().redrawCodemap();
	}	

	private void loadNewestQueryResult(ISearchQuery[] queries) {
		// new queries are added at first position
		ISearchQuery query = queries[0];
		ISearchResult searchResult = query.getSearchResult();
		if (searchResult instanceof AbstractTextSearchResult) {
			AbstractTextSearchResult res = (AbstractTextSearchResult) searchResult;
			onElementsAdded(Arrays.asList(res.getElements()));
		}		
	}

	private void addListener(ISearchQuery[] queries) {
		for(ISearchQuery each: queries) {
			onQueryAdded(each);
		}
	}
	
	private MapSelection getSearchSelection() {
		return SearchPluginCore.getPlugin().getSearchSelection();
	}

	private Collection<String> extractMatches(Collection<Object> elements) {
		Collection<String> idents = new HashSet<String>();
		for (Object each: elements) {
			processMatch(each, idents);
		}
		return idents;		
	}
	
	private void processMatch(Object element, Collection<String> idents) {
//		System.out.println(element.getClass());
		if (element instanceof IFile) {
			IJavaElement javaElement = JavaCore.create((IFile)element);
			if (javaElement == null) return;
			
			idents.add(javaElement.getHandleIdentifier());
		}
		if (element instanceof IJavaElement) {
			// create it that way to get the correct element (the compilation unit)
			IJavaElement compilationElement = getCompilationUnitElement((IJavaElement)element);

			if (compilationElement == null || compilationElement.isReadOnly()) return;
			if (compilationElement.getElementType() != IJavaElement.COMPILATION_UNIT) return;

			ICompilationUnit compilationUnit = (ICompilationUnit) compilationElement.getAdapter(ICompilationUnit.class);
			String ident = compilationUnit.getHandleIdentifier();
			idents.add(ident);
		}
	}
	
	private IJavaElement getCompilationUnitElement(IJavaElement element) {
		IResource resource = element.getResource();
		IJavaElement javaElement = JavaCore.create(resource);
		return javaElement;
	}

	public void registerAction(ShowSearchResultsAction showSearchResultsAction) {
		action = showSearchResultsAction;
	}

}
