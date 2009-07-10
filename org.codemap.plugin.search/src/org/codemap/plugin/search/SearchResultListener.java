package org.codemap.plugin.search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.codemap.CodemapCore;
import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.search.ui.ISearchResultListener;
import org.eclipse.search.ui.SearchResultEvent;
import org.eclipse.search.ui.text.Match;
import org.eclipse.search.ui.text.MatchEvent;
import org.eclipse.search.ui.text.RemoveAllEvent;


public class SearchResultListener implements ISearchResultListener {
	
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
		SearchPluginCore.getPlugin().getSearchSelection().clear();
		CodemapCore.getPlugin().redrawCodemap();
	}

	private void handleMatchEvent(MatchEvent me) {
		switch (me.getKind()) {
		case MatchEvent.ADDED:
			elementsAdded(extractMatches(me));
			break;
		case MatchEvent.REMOVED:
			elementsRemoved(extractMatches(me));
			break;
		}
		CodemapCore.getPlugin().redrawCodemap();		
	}

	private void elementsRemoved(List<String> list) {
		for (String each: list) {
			SearchPluginCore.getPlugin().getSearchSelection().remove(each);			
		}
	}

	private void elementsAdded(List<String> list) {
		for (String each: list) {
			SearchPluginCore.getPlugin().getSearchSelection().add(each);			
		}
	}

	private List<String> extractMatches(MatchEvent me) {
		List<Match> matches = Arrays.asList(me.getMatches());
		List<String> idents = new ArrayList<String>();
		for (Match match: matches) {
			processMatch(match, idents);
		}
		return idents;
	}

	private void processMatch(Match match, List<String> idents) {		
		Object element = match.getElement();
		if (element instanceof IJavaElement) {
			// create it that way to get the correct element (the compilation unit)
			IJavaElement javaElement = crateJavaElement(element);

			if (javaElement == null || javaElement.isReadOnly()) return;
			if (javaElement.getElementType() != IJavaElement.COMPILATION_UNIT) return;

			ICompilationUnit compilationUnit = (ICompilationUnit) javaElement.getAdapter(ICompilationUnit.class);
			String ident = compilationUnit.getHandleIdentifier();
			idents.add(ident);
		}
	}

	private IJavaElement crateJavaElement(Object element) {
		IResource resource = ((IJavaElement) element).getResource();
		IJavaElement javaElement = JavaCore.create(resource);
		return javaElement;
	}
}
