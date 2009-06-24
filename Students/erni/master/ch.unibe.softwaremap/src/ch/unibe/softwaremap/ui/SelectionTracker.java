package ch.unibe.softwaremap.ui;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPart;

import ch.unibe.eclipse.util.EclipseUtil;
import ch.unibe.softwaremap.Log;

public class SelectionTracker {

	private MapView view;
	private boolean enabled = false;

	public SelectionTracker(MapView view) {
		this.view = view;
	}
	
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean checked) {
		enabled = checked;
	}

	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		if (part == this.view) return;
		if (! isEnabled()) return;
		if (! (selection instanceof IStructuredSelection)) return;

		try {
			selectionChanged((IStructuredSelection) selection);
		} catch (CoreException e) {
			Log.error(e);
		}
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
		view.project = EclipseUtil.adapt(javaProject, IProject.class);
		view.selectedUnits = units;
		view.compilationUnitsSelected();
	}	


}
