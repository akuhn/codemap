package ch.unibe.softwaremap.ui;

import static ch.unibe.eclipse.util.ID.CONTENT_OUTLINE;
import static ch.unibe.eclipse.util.ID.PACKAGE_EXPLORER;
import static ch.unibe.eclipse.util.ID.RESOURCE_NAVIGATOR;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.ISetSelectionTarget;
import org.eclipse.ui.part.ViewPart;

import ch.deif.meander.Location;
import ch.deif.meander.ui.EclipseProcessingBridge;
import ch.deif.meander.ui.MeanderEventListener;
import ch.deif.meander.viz.MapVisualization;
import ch.unibe.eclipse.util.EclipseUtil;
import ch.unibe.eclipse.util.SelectionProviderAdapter;
import ch.unibe.softwaremap.Log;
import ch.unibe.softwaremap.SoftwareMapCore;

public class MapView extends ViewPart implements ISelectionListener, ISelectionProvider, MeanderEventListener {

	public static final String MAP_VIEW_ID = SoftwareMapCore.makeID(MapView.class);
	
	private EclipseProcessingBridge softwareMap;
	private IProject project;
	private Collection<ICompilationUnit> selectedUnits;
	private SelectionProviderAdapter selectionProvider;

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
		this.selectionProvider = new SelectionProviderAdapter();
	}

	@Override
	public void createPartControl(final Composite parent) {
		softwareMap = new EclipseProcessingBridge(parent);
		softwareMap.getApplet().addListener(this);
		addSelectionListener(PACKAGE_EXPLORER.id, CONTENT_OUTLINE.id, RESOURCE_NAVIGATOR.id);
		getSite().setSelectionProvider(this);
		SoftwareMapCore.setMapView(this);
		
		new ResizeUpdate(parent, softwareMap);
	}
	
	protected void mapDimensionChanged(Point point) {
		int newDimension = Math.min(point.x, point.y);
		SoftwareMapCore.updateMapdimension(newDimension);
	}	

	/**
	 * Sent when view is closed.
	 * 
	 */
	@Override
	public void dispose() {
		SoftwareMapCore.setMapView(null);
		removeSelectionListener(CONTENT_OUTLINE.id, PACKAGE_EXPLORER.id, RESOURCE_NAVIGATOR.id);
	}

	private void removeSelectionListener(String... viewPartID) {
		for (String each: viewPartID) {
			getSite().getWorkbenchWindow().getSelectionService().removeSelectionListener(each, this);
		}
	}

	private void addSelectionListener(String... viewPartID) {
		for (String each: viewPartID) {
			getSite().getWorkbenchWindow().getSelectionService().addSelectionListener(each, this);
		}
	}

	@Override
	public void setFocus() {
		softwareMap.setFocus();
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		if (part == this) return;
		if (!(selection instanceof IStructuredSelection)) return;

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
		this.project = EclipseUtil.adapt(javaProject, IProject.class);
		this.selectedUnits = units;
		compilationUnitsSelected(units);
	}

	private void compilationUnitsSelected(Collection<ICompilationUnit> units) {
		MapVisualization viz = SoftwareMapCore.at(project).enableBuilder().getVisualization();
		if (viz == null) return;
		updateMapVisualization(viz);
		softwareMapUpdateSelection(units);
	}

	public void updateMapVisualization(MapVisualization viz) {
		softwareMap.setMapVizualization(viz);
	}

	private void softwareMapUpdateSelection(Collection<ICompilationUnit> units) {
		List<String> handleIdentifiers = new ArrayList<String>();
		for (ICompilationUnit each: units) {
			handleIdentifiers.add(each.getHandleIdentifier());
		}
		updateSelection(handleIdentifiers);
	}
	
	public void addSelection(String handleIdentifier) {
		List<String> list = new ArrayList<String>();
		list.add(handleIdentifier);
		softwareMap.addSelection(list);
	}

	public void updateSelection(List<String> handleIdentifiers) {
		softwareMap.updateSelection(handleIdentifiers);
		
	}

	public void newProjectMapAvailable(IProject project) {
		if (!this.project.equals(project)) return;
		this.compilationUnitsSelected(selectedUnits);
	}

	@Override
	public void addSelectionChangedListener(ISelectionChangedListener listener) {
		this.selectionProvider.addSelectionChangedListener(listener);
	}

	@Override
	public ISelection getSelection() {
		return this.selectionProvider.getSelection();
	}

	@Override
	public void removeSelectionChangedListener(ISelectionChangedListener listener) {
		this.selectionProvider.removeSelectionChangedListener(listener);
	}

	@Override
	public void setSelection(ISelection selection) {
		this.selectionProvider.setSelection(selection);
	}
	

	@Override
	public void selectionChanged(Location... locations) {
		final ArrayList<IJavaElement> selection = new ArrayList<IJavaElement>();
		for (Location each: locations) {
			if (each.document().getIdentifier() == null) continue;
			IJavaElement javaElement = JavaCore.create(each.document().getIdentifier());
			selection.add(javaElement);
		}
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				try {
					IViewPart showView = getSite().getPage().showView(PACKAGE_EXPLORER.id);
					((ISetSelectionTarget) showView).selectReveal(new StructuredSelection(selection));
				} catch (PartInitException e) {
					Log.error(e);
				}
			}
		});
	}	

	private void openInEditor(IJavaElement javaElement) {
		final IWorkbenchPage page = getSite().getPage();
		final IResource resource = javaElement.getResource();
		if (resource == null || !resource.exists() || !(resource instanceof IFile)) return;

		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				try {
					IDE.openEditor(page, (IFile) resource, true);
				} catch (PartInitException e) {
					Log.error(e);
				}
			}
		});
	}

	@Override
	public void doubleClicked(Location location) {
		if (location.document().getIdentifier() == null) return;
		IJavaElement javaElement = JavaCore.create(location.document().getIdentifier());		
		openInEditor(javaElement);
	}

}