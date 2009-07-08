package org.codemap.plugin.search;

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


public class MeanderSearchResultListener implements ISearchResultListener {

	@Override
	public void searchResultChanged(SearchResultEvent e) {
		if (e instanceof MatchEvent) {
			MatchEvent me = (MatchEvent) e;
			List<Match> matches = Arrays.asList(me.getMatches());
			for (Match match: matches) {
				processMatch(match);
			}
			CodemapCore.getPlugin().redrawCodemap();
		}
	}

	private void processMatch(Match match) {		
		Object element = match.getElement();
		if (element instanceof IJavaElement) {
			// create it that way to get the correct element (the compilation unit)
			IJavaElement javaElement = crateJavaElement(element);
			
			if (javaElement == null || javaElement.isReadOnly()) return;
			if (javaElement.getElementType() != IJavaElement.COMPILATION_UNIT) return;

			ICompilationUnit compilationUnit = (ICompilationUnit) javaElement.getAdapter(ICompilationUnit.class);
			String ident = compilationUnit.getHandleIdentifier();

			System.out.println("got new selection: " + ident);
			SearchPluginCore.getPlugin().getSearchSelection().add(ident);			
		}
	}

	private IJavaElement crateJavaElement(Object element) {
		IResource resource = ((IJavaElement) element).getResource();
		IJavaElement javaElement = JavaCore.create(resource);
		return javaElement;
	}
}