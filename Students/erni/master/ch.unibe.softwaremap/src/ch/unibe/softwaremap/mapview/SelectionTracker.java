package ch.unibe.softwaremap.mapview;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IStorageEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;

import ch.unibe.softwaremap.util.EclipseUtil;
import ch.unibe.softwaremap.util.Log;

public class SelectionTracker {
	
	private ISelectionListener selectionListener = new ISelectionListener() {
		
		@Override
		public void selectionChanged(IWorkbenchPart part, ISelection selection) {
			if (part == view) return;
			if (! isEnabled()) return;	
			if (! (selection instanceof IStructuredSelection)) return;
			
			try {
				SelectionTracker.this.selectionChanged((IStructuredSelection) selection);
			} catch (CoreException e) {
				Log.error(e);
			}
			
		}
	};

	private IPartListener2 partListener =  new IPartListener2() {
		
		@Override
		public void partVisible(IWorkbenchPartReference partRef) {}
		
		@Override
		public void partOpened(IWorkbenchPartReference partRef) {
			theController.onEditorOpened();
		}
		
		
		@Override
		public void partHidden(IWorkbenchPartReference partRef) {}
		
		@Override
		public void partDeactivated(IWorkbenchPartReference partRef) {}
		
		@Override
		public void partClosed(IWorkbenchPartReference partRef) {
			theController.onEditorClosed();
		}
		
		@Override
		public void partBroughtToTop(IWorkbenchPartReference partRef) {}
		
		@Override
		public void partInputChanged(IWorkbenchPartReference partRef) {
			handleEditorEvents(partRef);
//			System.out.println("input changed " + partRef);		
		}

		@Override
		public void partActivated(IWorkbenchPartReference partRef) {
			theController.onEditorActivated();
//			System.out.println("part activated " + partRef);
		}
		
		private void handleEditorEvents(IWorkbenchPartReference partRef) {
			if (partRef instanceof IEditorReference) {
				SelectionTracker.this.editorActivated(((IEditorReference) partRef).getEditor(true));
			}
		}
		
	};
	
	private MapView view;
	private boolean enabled = false;

	MapController theController;	
	
	public SelectionTracker(MapView view, MapController theController) {
		this.view = view;
		this.theController = theController;
		addListeners();
	}

	private void editorActivated(IEditorPart editor) {
		IEditorInput editorInput= editor.getEditorInput();
		if (editorInput == null)
			return;
		Object input= getInputFromEditor(editorInput);
		if (input == null)
			return;
		if (!inputIsSelected(editorInput))
			showInput(input);		
	}

	private Object getInputFromEditor(IEditorInput editorInput) {
		Object input= JavaUI.getEditorInputJavaElement(editorInput);
		if (input instanceof ICompilationUnit) {
			ICompilationUnit cu= (ICompilationUnit) input;
			if (!cu.getJavaProject().isOnClasspath(cu)) { // test needed for Java files in non-source folders (bug 207839)
				input= cu.getResource();
			}
		}
		if (input == null) {
			input= editorInput.getAdapter(IFile.class);
		}
		if (input == null && editorInput instanceof IStorageEditorInput) {
			try {
				input= ((IStorageEditorInput) editorInput).getStorage();
			} catch (CoreException e) {
				// ignore
			}
		}
		return input;
	}
	
	private boolean inputIsSelected(IEditorInput input) {
		System.out.println();
//		IStructuredSelection selection= (IStructuredSelection) fViewer.getSelection();
		// XXX: return if the given input is already selected on the mapview
		return true;
	}	
	
	private boolean showInput(Object input) {
		// XXX: show the selection on the map
		Object element= null;

		if (input instanceof IFile && isOnClassPath((IFile)input)) {
			element= JavaCore.create((IFile)input);
		}

		if (element == null) // try a non Java resource
			element= input;

		if (element != null) {
			ISelection newSelection= new StructuredSelection(element);
			System.out.println(newSelection);
//			if (fViewer.getSelection().equals(newSelection)) {
//				fViewer.reveal(element);
//			} else {
//				fViewer.setSelection(newSelection, true);
//
//				while (element != null && fViewer.getSelection().isEmpty()) {
//					// Try to select parent in case element is filtered
//					element= getParent(element);
//					if (element != null) {
//						newSelection= new StructuredSelection(element);
//						fViewer.setSelection(newSelection, true);
//					}
//				}
//			}
			return true;
		}
		return false;		
		
	}
	
	private boolean isOnClassPath(IFile file) {
		IJavaProject jproject= JavaCore.create(file.getProject());
		return jproject.isOnClasspath(file);
	}	

	private void addListeners() {
		IWorkbenchPage page = getWorkbenchPage();
		page.addPostSelectionListener(selectionListener);
		page.addPartListener(partListener);
	}

	private IWorkbenchPage getWorkbenchPage() {
		return view.getSite().getPage();
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
			IJavaElement javaElement = EclipseUtil.adapt(each, IJavaElement.class);
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
		if (javaProject != null) {
			compilationUnitsSelected(javaProject, units);
		}
	}

	private void multipleProjectsSelected() {
		System.out.println("!!! multiple projects selected !!!");
	}

	private void compilationUnitsSelected(IJavaProject javaProject, Collection<ICompilationUnit> units) {
		view.compilationUnitsSelected(javaProject, units);
	}

}
