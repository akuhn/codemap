package org.codemap.mapview;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

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
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;


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
	
	/**
	 * Tracks the editor-selection.
	 */
	private IPartListener2 partListener =  new IPartListener2() {
		
		/*
		 * (non-Javadoc)
		 * @see org.eclipse.ui.IPartListener2#partVisible(org.eclipse.ui.IWorkbenchPartReference)
		 * 
		 * For tab-bars being visible means that a given tab is selected. Not
		 * selected tabs that are shown in the bar are not considered visible. 
		 */
		@Override
		public void partVisible(IWorkbenchPartReference partRef) {
			// do nothing
		}
		
		/*
		 * (non-Javadoc)
		 * @see org.eclipse.ui.IPartListener2#partHidden(org.eclipse.ui.IWorkbenchPartReference)
		 * 
		 * Fired whenever a *visible* part changes its visibility from visible to hidden
		 */
		@Override
		public void partHidden(IWorkbenchPartReference partRef) {
			// do nothing
		}

		@Override
		public void partDeactivated(IWorkbenchPartReference partRef) {
			// do nothing
		}
		
		@Override
		public void partBroughtToTop(IWorkbenchPartReference partRef) {
			// do nothing
		}
		
		/*
		 * (non-Javadoc)
		 * @see org.eclipse.ui.IPartListener2#partOpened(org.eclipse.ui.IWorkbenchPartReference)
		 * 
		 * Fired when a new editor is opened or when an inactive editor is accessed for the
		 * first time (e.g. tab-selection changes the first time to some element).
		 */
		@Override
		public void partOpened(IWorkbenchPartReference partRef) {
			theController.onEditorOpened(new EditorEvent(partRef));
		}
		
		@Override
		public void partClosed(IWorkbenchPartReference partRef) {
			theController.onEditorClosed(new EditorEvent(partRef));
		}
		
		@Override
		public void partInputChanged(IWorkbenchPartReference partRef) {
			editorSelectionChanged(partRef);
		}
		
		/*
		 * (non-Javadoc)
		 * @see org.eclipse.ui.IPartListener2#partActivated(org.eclipse.ui.IWorkbenchPartReference)
		 * 
		 * Fired when a new editor is opened or when the editor-tab selection changes.
		 */
		@Override
		public void partActivated(IWorkbenchPartReference partRef) {
			editorSelectionChanged(partRef);
		}
		
		private void editorSelectionChanged(IWorkbenchPartReference partRef) {
			theController.onEditorActivated(new EditorEvent(partRef));
		}
	};
	
	private boolean enabled = LinkWithSelectionAction.DEFAULT_VALUE;
	MapController theController;	
	
	public SelectionTracker(MapController controller) {
		theController = controller;
		addListeners();
	}

	private void addListeners() {
		IWorkbenchPage page = getWorkbenchPage();
		page.addPostSelectionListener(selectionListener);
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
			if (javaElement == null) {
				continue;
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

}
