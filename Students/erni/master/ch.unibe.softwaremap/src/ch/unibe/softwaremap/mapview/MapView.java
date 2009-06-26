package ch.unibe.softwaremap.mapview;

import static ch.unibe.softwaremap.util.ID.PACKAGE_EXPLORER;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
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
import ch.deif.meander.ui.MeanderApplet;
import ch.deif.meander.ui.MeanderEventListener;
import ch.deif.meander.viz.MapVisualization;
import ch.unibe.softwaremap.CodemapCore;
import ch.unibe.softwaremap.util.EclipseProcessingBridge;
import ch.unibe.softwaremap.util.EclipseUtil;
import ch.unibe.softwaremap.util.Log;

public class MapView extends ViewPart implements MeanderEventListener {

	public static final String MAP_VIEW_ID = CodemapCore.makeID(MapView.class);
	
	private EclipseProcessingBridge bridge;
	private IProject project;
	private Collection<ICompilationUnit> selectedUnits;
	private MapSelectionProvider selectionProvider;
	private SelectionTracker selectionTracker;
	private Composite container;
	private MeanderApplet theApplet;
	private final MapController theController;

	
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
		theController = new MapController(this);
	}

	@Override
	public void createPartControl(final Composite parent) {
		container = new Composite(parent, SWT.NONE);
		container.setLayout(new FillLayout(SWT.LEFT));
		
		container.layout();
		
		theApplet = EclipseProcessingBridge.createApplet();

		selectionProvider = new MapSelectionProvider(this);
		selectionTracker = new SelectionTracker(this, theController);
		configureToolbar();
		
		showMap();
		theController.onOpenView();
	}

	private void showButton() {
		clearContainer();
		
		Button button = new Button(container, SWT.PUSH);
		button.setText("generate Map");
		button.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				MapView.this.showMap();
			}
		});
		redrawContainer();
	}

	public void redrawContainer() {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				container.layout();
				container.redraw();
			}
		});
	}

	private void clearContainer() {
		for(Control each: container.getChildren()) {
			each.dispose();
		}
	}
	
	void showMap() {
		clearContainer();
		
		bridge = new EclipseProcessingBridge(container, theApplet);
		softwareMap().getApplet().addListener(this);
		
		CodemapCore.getPlugin().setMapView(this);
		new ResizeListener(container, theController);
		redrawContainer();
		theController.onShowMap();
	}

	private void configureToolbar() {
	    IToolBarManager tbm = getToolBarManager();
	    tbm.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	    tbm.add(new Separator());
	    tbm.add(new LinkWithSelectionAction(selectionTracker));
	    
	    IExtensionPoint extensionPoint = Platform.getExtensionRegistry().getExtensionPoint(CodemapCore.PLUGIN_ID, "mapview");
	    IExtension[] extensions_arr = extensionPoint.getExtensions();
	    List<IExtension> extensions = Arrays.asList(extensions_arr);
	    for (IExtension extension: extensions) {
	    	parseConfig(extension.getConfigurationElements(), tbm);
	    }
	}
	
	private void parseConfig(IConfigurationElement[] configurationElements, IToolBarManager tbm) {
		List<IConfigurationElement> configelems = Arrays.asList(configurationElements);
		for (IConfigurationElement each: configelems) {
			tbm.add(new LazyPluginAction(each));
		}
	}

	public IToolBarManager getToolBarManager() {
		return getViewSite().getActionBars().getToolBarManager();
	}

	protected void mapDimensionChanged(Point point) {
		int newDimension = Math.min(point.x, point.y);
		CodemapCore.getPlugin().updateMapdimension(newDimension);
	}	

	/**
	 * Sent when view is closed.
	 * 
	 */
	@Override
	public void dispose() {
		CodemapCore.getPlugin().setMapView(null);
		selectionTracker.dispose();
	}

	@Override
	public void setFocus() {
		EclipseProcessingBridge map = softwareMap();
		if (map == null) return;
		map.setFocus();
	}

	void updateVisualization() {
		MapVisualization viz = CodemapCore.getPlugin().mapForChangedProject(project).enableBuilder().getVisualization();
		if (viz == null) return;
		updateMapVisualization(viz);
		softwareMapUpdateSelection(selectedUnits);
	}

	public void updateMapVisualization(MapVisualization viz) {
		softwareMap().setMapVizualization(viz);
		redrawContainer();
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
		this.updateVisualization();
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
		return bridge;
	}

	void compilationUnitsSelected(IJavaProject javaProject, Collection<ICompilationUnit> units) {
		IProject project0 = EclipseUtil.adapt(javaProject, IProject.class);
		if (project0 != project) selectionTracker.theController.onProjectChanged();
		project = project0;
		selectedUnits = units;
		selectionTracker.theController.onSelectionChanged();
		updateVisualization();
	}

}