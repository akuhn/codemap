package ch.unibe.softwaremap.ui;

import static ch.unibe.eclipse.util.ID.CONTENT_OUTLINE;
import static ch.unibe.eclipse.util.ID.PACKAGE_EXPLORER;
import static ch.unibe.eclipse.util.ID.RESOURCE_NAVIGATOR;

import java.util.Collection;
import java.util.HashSet;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;

import processing.core.PApplet;
import ch.unibe.eclipse.util.EclipseUtil;
import ch.unibe.softwaremap.SoftwareMapCore;

public class TagCloudView extends ViewPart implements ISelectionListener, ControlListener {

	private EclipseProcessingBridge2 view;

	/**
	 * Sent when view is opened.
	 * 
	 */
	@SuppressWarnings("serial")
	@Override
	public void createPartControl(Composite parent) {
		view = new EclipseProcessingBridge2(parent);
		view.setApplet(new PApplet() {
			@Override
			public void draw() {
				rect(50, 50, 200, 200);
			}

			@Override
			public void setup() {
				size(400, 400);
			}
		});
		addSelectionListener(PACKAGE_EXPLORER.id, CONTENT_OUTLINE.id, RESOURCE_NAVIGATOR.id);
		view.addControlListener(this);
	}

	@Override
	public void setFocus() {
		view.setFocus();
	}

	/**
	 * Sent when view is closed.
	 * 
	 */
	@Override
	public void dispose() {
		removeSelectionListener(PACKAGE_EXPLORER.id, CONTENT_OUTLINE.id, RESOURCE_NAVIGATOR.id);
	}

	private void addSelectionListener(String... viewPartID) {
		for (String each: viewPartID)
			getSite().getWorkbenchWindow().getSelectionService().addSelectionListener(each, this);
	}

	private void removeSelectionListener(String... viewPartID) {
		for (String each: viewPartID)
			getSite().getWorkbenchWindow().getSelectionService().removeSelectionListener(each, this);
	}

	/**
	 * Sent when selection in package explorer or content outline changes.
	 * 
	 */
	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		if (part == this) return;
		if (selection instanceof IStructuredSelection) selectionChanged((IStructuredSelection) selection);
	}

	/**
	 * Filters selected IJavaProject and ICompilationUnit.
	 * 
	 * @param selection
	 */
	private void selectionChanged(IStructuredSelection selection) {
		IJavaProject project = null;
		Collection<ICompilationUnit> units = new HashSet<ICompilationUnit>();
		for (Object each: selection.toList()) {
			IJavaElement javaElement = EclipseUtil.adapt(each, IJavaElement.class);
			if (project == null) project = javaElement.getJavaProject();
			if (project != javaElement.getJavaProject()) {
				multipleProjectSelected();
				return;
			}
			if (javaElement instanceof ICompilationUnit) {
				units.add((ICompilationUnit) javaElement);
			}
			if (javaElement instanceof IMember) {
				javaElement = javaElement.getAncestor(IJavaElement.COMPILATION_UNIT);
				if (javaElement != null) units.add((ICompilationUnit) javaElement);
			}
		}
		if (project != null) compilationUnitsSelected(project, units);
	}

	private void compilationUnitsSelected(IJavaProject project, Collection<ICompilationUnit> units) {
		System.out.println(units.size() + " in " + project.getHandleIdentifier());
		IProject resource = EclipseUtil.adapt(project, IProject.class);
		SoftwareMapCore.at(resource).enableBuilder();
	}

	private void multipleProjectSelected() {
		System.out.println("!!! multiple projects selected !!!");
	}

	@Override
	public void controlMoved(ControlEvent e) {
		// Do nothing.
	}

	/**
	 * Sent when view is resized.
	 * 
	 */
	@Override
	public void controlResized(ControlEvent e) {
		// TODO handle this event somehow
	}

}
