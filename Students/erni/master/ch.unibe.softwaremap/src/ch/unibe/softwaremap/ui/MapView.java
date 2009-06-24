package ch.unibe.softwaremap.ui;

import static ch.unibe.eclipse.util.ID.PACKAGE_EXPLORER;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.ISetSelectionTarget;
import org.eclipse.ui.part.ViewPart;

import ch.deif.meander.Location;
import ch.deif.meander.ui.EclipseProcessingBridge;
import ch.deif.meander.ui.MeanderEventListener;
import ch.deif.meander.viz.MapVisualization;
import ch.unibe.softwaremap.Log;
import ch.unibe.softwaremap.SoftwareMap;

public class MapView extends ViewPart implements MeanderEventListener {

	public static final String MAP_VIEW_ID = SoftwareMap.makeID(MapView.class);
	
	private EclipseProcessingBridge softwareMap;
	IProject project;
	Collection<ICompilationUnit> selectedUnits;
	private SelectionProvider selectionProvider;
	private SelectionTracker selectionTracker;

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
	public void createPartControl(final Composite parent) {
		softwareMap = new EclipseProcessingBridge(parent);
		softwareMap().getApplet().addListener(this);
		
		SoftwareMap.core().setMapView(this);
		new ResizeUpdate(parent, softwareMap());
		
		selectionProvider = new SelectionProvider(this);
		selectionTracker = new SelectionTracker(this);
		configureToolbar();	
	}
	
	private void configureToolbar() {
	    IToolBarManager tbm = getViewSite().getActionBars().getToolBarManager();
	    tbm.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	    tbm.add(new Separator());
	    tbm.add(new LinkWithSelectionAction(selectionTracker));
	}

	protected void mapDimensionChanged(Point point) {
		int newDimension = Math.min(point.x, point.y);
		SoftwareMap.core().updateMapdimension(newDimension);
	}	

	/**
	 * Sent when view is closed.
	 * 
	 */
	@Override
	public void dispose() {
		SoftwareMap.core().setMapView(null);
		selectionTracker.dispose();
	}

	@Override
	public void setFocus() {
		softwareMap().setFocus();
	}

	void compilationUnitsSelected() {
		MapVisualization viz = SoftwareMap.core().mapForChangedProject(project).enableBuilder().getVisualization();
		if (viz == null) return;
		updateMapVisualization(viz);
		softwareMapUpdateSelection(selectedUnits);
	}

	public void updateMapVisualization(MapVisualization viz) {
		softwareMap().setMapVizualization(viz);
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
		softwareMap().addSelection(list);
	}

	public void updateSelection(List<String> handleIdentifiers) {
		softwareMap().updateSelection(handleIdentifiers);
		
	}

	public void newProjectMapAvailable(IProject project) {
		if (!this.project.equals(project)) return;
		this.compilationUnitsSelected();
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
					final StructuredSelection structuredSelection = new StructuredSelection(selection);
					selectionProvider.setSelection(structuredSelection);
					
					IViewPart showView = getSite().getPage().showView(PACKAGE_EXPLORER.id);
					((ISetSelectionTarget) showView).selectReveal(structuredSelection);
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

	public EclipseProcessingBridge softwareMap() {
		return softwareMap;
	}

}