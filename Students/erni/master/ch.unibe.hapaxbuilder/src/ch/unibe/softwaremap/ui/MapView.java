package ch.unibe.softwaremap.ui;

import static ch.unibe.eclipse.util.EclipseUtil.adapt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import ch.deif.meander.ui.EclipseProcessingBridge;
import ch.deif.meander.viz.MapVisualization;
import ch.unibe.eclipse.util.EclipseUtil;
import ch.unibe.softwaremap.SoftwareMapCore;

public class MapView extends ViewPart implements ISelectionListener {

	public static final String MAP_VIEW_ID = SoftwareMapCore.makeID(MapView.class);
	private EclipseProcessingBridge softwareMap;

	private static final String RESOURCE_NAVIGATOR_ID = "org.eclipse.ui.views.ResourceNavigator";
	private static final String CONTENT_OUTLINE_ID = "org.eclipse.ui.views.ContentOutline";
	public static final String PACKAGE_EXPLORER_ID = "org.eclipse.jdt.ui.PackageExplorer";

	class ViewLabelProvider extends LabelProvider implements ITableLabelProvider {
		public String getColumnText(Object obj, int index) {
			return getText(obj);
		}

		public Image getColumnImage(Object obj, int index) {
			return getImage(obj);
		}

		@Override
		public Image getImage(Object obj) {
			return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT);
		}
	}

	public MapView() {
	}

	@Override
	public void createPartControl(Composite parent) {
		softwareMap = new EclipseProcessingBridge(parent);
		addSelectionListener(PACKAGE_EXPLORER_ID, CONTENT_OUTLINE_ID, RESOURCE_NAVIGATOR_ID);
	}

	/**
	 * Sent when view is closed.
	 * 
	 */
	@Override
	public void dispose() {
		removeSelectionListener(CONTENT_OUTLINE_ID, PACKAGE_EXPLORER_ID, RESOURCE_NAVIGATOR_ID);
	}

	private void removeSelectionListener(String... viewPartID) {
		for (String each : viewPartID)
			getSite().getWorkbenchWindow().getSelectionService().removeSelectionListener(each, this);
	}

	private void addSelectionListener(String... viewPartID) {
		for (String each : viewPartID)
			getSite().getWorkbenchWindow().getSelectionService().addSelectionListener(each, this);
	}

	@Override
	public void setFocus() {
		softwareMap.setFocus();
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		if (part == this)
			return;
		if (selection instanceof IStructuredSelection)
			selectionChanged((IStructuredSelection) selection);
	}

	/**
	 * Filters selected IJavaProject and ICompilationUnit.
	 * 
	 * @param selection
	 */
	private void selectionChanged(IStructuredSelection selection) {
		IJavaProject project = null;
		Collection<ICompilationUnit> units = new HashSet<ICompilationUnit>();
		for (Object each : selection.toList()) {
			IJavaElement javaElement = adapt(each, IJavaElement.class);
			if (javaElement == null) continue;
			if (project == null)
				project = javaElement.getJavaProject();
			if (project != javaElement.getJavaProject()) {
				multipleProjectSelected();
				return;
			}
			if (javaElement instanceof ICompilationUnit) {
				units.add((ICompilationUnit) javaElement);
			}
			if (javaElement instanceof IMember) {
				javaElement = javaElement.getAncestor(IJavaElement.COMPILATION_UNIT);
				if (javaElement != null)
					units.add((ICompilationUnit) javaElement);
			}
		}
		if (project != null)
			compilationUnitsSelected(project, units);
	}

	private void multipleProjectSelected() {
		System.out.println("!!! multiple projects selected !!!");
	}

	private void compilationUnitsSelected(IJavaProject project, Collection<ICompilationUnit> units) {
		System.out.println(units.size() + " in " + project.getHandleIdentifier());
		IProject resource = adapt(project, IProject.class);
		MapVisualization<?> viz = SoftwareMapCore.at(resource).enableBuilder().getVisualization();
		if (viz == null) return;
		softwareMap.setMapVizualization(viz);
		
		List<String> handleIdentifiers = new ArrayList<String>();
		for(ICompilationUnit each : units) {
			handleIdentifiers.add(each.getHandleIdentifier());
		}
		softwareMap.updateSelection(handleIdentifiers);
	}
}