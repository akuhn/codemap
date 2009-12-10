package org.codemap.mapview;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import org.codemap.CodemapCore;
import org.codemap.util.Adaptables;
import org.codemap.util.Log;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;

public class SelectionTracker {
	
	/**
	 * Tracks the global selection provided by eclipse. 
	 */
	private ISelectionListener selectionListener = new ISelectionListener() {
		
		@Override
		public void selectionChanged(IWorkbenchPart part, ISelection selection) {
			if (part == theController.getView()) return;
	
			if (! (selection instanceof IStructuredSelection)) return;
			
			try {
				SelectionTracker.this.selectionChanged((IStructuredSelection) selection);
			} catch (CoreException e) {
				Log.error(e);
			}
		}
	};
	
	private boolean enabled;
	MapController theController;

	private EditorPartListener partListener;	
	
	public SelectionTracker(MapController controller) {
		theController = controller;
		partListener = new EditorPartListener(
				CodemapCore.getPlugin().getOpenFilesSelection(),
				CodemapCore.getPlugin().getYouAreHereSelection(), 
				theController); 
		addListeners();
	}

	private void addListeners() {
		IWorkbenchPage page = getWorkbenchPage();
		page.addSelectionListener(selectionListener);
		page.addPartListener(partListener);
	}

	private IWorkbenchPage getWorkbenchPage() {
		return theController.getView().getSite().getPage();
	}

	public void dispose() {
	    removeListeners();
	}

	private void removeListeners() {
		IWorkbenchPage page = getWorkbenchPage();
		page.removePostSelectionListener(selectionListener);
		page.removePartListener(partListener);
	}		
	
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean checked) {
		enabled = checked;
	}
	
	/**
	 * Filters selected IJavaProject and ICompilationUnit.
	 * 
	 * @param selection
	 * @throws CoreException
	 */
	private void selectionChanged(IStructuredSelection selection) throws CoreException {
		IJavaProject javaProject = null;
		Collection<ICompilationUnit> units = new HashSet<ICompilationUnit>();
		for (Object each: selection.toList()) {
			IJavaElement javaElement = Adaptables.adapt(each, IJavaElement.class);
			if (javaElement == null) continue;
			// we can't handle binaries as their project usually contains no sources and is not
			// visible in the navigation
			if (javaElement instanceof IMember){
			    IMember member = (IMember) javaElement;
			    if (member.isBinary()) return;
			} 
			
			if (javaProject == null) {
				javaProject = javaElement.getJavaProject();
			}
			if (!javaProject.equals(javaElement.getJavaProject()) && javaElement.getJavaProject() != null) {
				multipleProjectsSelected();
				return;
			}
			if (javaElement instanceof ICompilationUnit) {
				units.add((ICompilationUnit) javaElement);
			}
			if (javaElement instanceof IPackageFragment) {
				ICompilationUnit[] children = ((IPackageFragment) javaElement).getCompilationUnits();
				units.addAll(Arrays.asList(children));
			}
			if (javaElement instanceof IMember) {
				javaElement = javaElement.getAncestor(IJavaElement.COMPILATION_UNIT);
				if (javaElement != null) {
					units.add((ICompilationUnit) javaElement);
				}
			}
		}
		if (javaProject == null) return;
		projectSelected(javaProject);
		if (isEnabled()) {
			compilationUnitsSelected(units);			
		}
	}

	private void projectSelected(IJavaProject javaProject) {
		theController.onProjectSelected(javaProject);
	}

	private void multipleProjectsSelected() {
		System.out.println("!!! multiple projects selected !!!");
	}

	private void compilationUnitsSelected(Collection<ICompilationUnit> units) {
		theController.onSelectionChanged(units);
	}

    public EditorPartListener getEditorPartListener() {
        return partListener;
    }

}
