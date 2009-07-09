package org.codemap.mapview;

import static org.codemap.util.ID.PACKAGE_EXPLORER;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.codemap.CodemapCore;
import org.codemap.util.EclipseProcessingBridge;
import org.codemap.util.Log;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
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
import ch.deif.meander.ui.CodemapEvent;
import ch.deif.meander.ui.CodemapListener;
import ch.deif.meander.ui.MeanderApplet;
import ch.deif.meander.util.MColor;
import ch.deif.meander.visual.CurrentSelectionOverlay;
import ch.deif.meander.visual.MapVisualization;

// TODO let MapController track the currently active project.
public class MapView extends ViewPart {

	public static final String MAP_VIEW_ID = CodemapCore.makeID(MapView.class);
	
	private EclipseProcessingBridge bridge;
	private IProject currentProject;
	private MapSelectionProvider selectionProvider;
	private SelectionTracker selectionTracker;
	private Composite container;
	private MeanderApplet theApplet;
	private final MapController theController;
	private int currentSize;
	
	private static class MyAction extends Action {
		
		public MyAction(String text) {
			super(text, IAction.AS_RADIO_BUTTON);
		}
		
		
	}
	
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
		MColor water = MColor.WATER;
		Color swtColor = new Color(null, water.getRed(), water.getGreen(), water.getBlue());		
		container.setBackground(swtColor);
		theApplet = EclipseProcessingBridge.createApplet();
		theApplet.addListener(makeListener());

		selectionProvider = new MapSelectionProvider(this);
		selectionTracker = new SelectionTracker(theController);
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
		System.out.println("redrawContainer on MapView");
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
	
	private void showMap() {
		clearContainer();
		bridge = new EclipseProcessingBridge(container, theApplet);
		softwareMap().getApplet().addListener(makeListener());
		CodemapCore.getPlugin().setMapView(this);
		new ResizeListener(container, theController);
//		redrawContainer();
		theController.onShowMap();
	}

	private CodemapListener makeListener() {
		return new CodemapListener() {
			@Override
			public void handleEvent(CodemapEvent event) {
				if (CurrentSelectionOverlay.EVT_DOUBLE_CLICKED == event.getKind()) {
					doubleClicked((Location) event.getValue());					
				} else if (CurrentSelectionOverlay.EVT_SELECTION_CHANGED == event.getKind()) {
					selectionChanged((Location[]) event.getValue());
				}
			}

			public void selectionChanged(Location... locations) {
				final ArrayList<IJavaElement> selection = new ArrayList<IJavaElement>();
				for (Location each: locations) {
					if (each.getIdentifier() == null) continue;
					IJavaElement javaElement = JavaCore.create(each.getIdentifier());
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

			public void doubleClicked(Location location) {
				if (location.getIdentifier() == null) return;
				IJavaElement javaElement = JavaCore.create(location.getIdentifier());		
				openInEditor(javaElement);
			}
		};
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
	    
	    IMenuManager mm = getMenuManager();
	    MenuManager labelManager = new MenuManager("Labels");
	    MyAction classNameAction = new MyAction("Class Name");
	    classNameAction.setChecked(true);
	    labelManager.add(classNameAction);
	    labelManager.add(new MyAction("Log-likelihood"));
	    mm.add(labelManager);
	    
//	    mm.add(new SelectEntryModeAction(ViewSettings.ENTRYMODE_PROJECTS, settings,
//	        this));
//	    mm.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));	    
	    
	}
	
	private IMenuManager getMenuManager() {
		return getViewSite().getActionBars().getMenuManager();
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

	public void updateVisualization() {
		MapVisualization viz = CodemapCore.getPlugin()
			.mapForProject(currentProject)
			.updateSize(getCurrentSize())
			.enableBuilder()
			.getVisualization();
		if (viz == null) return;
		updateMapVisualization(viz);
	}

	private int getCurrentSize() {
		return currentSize; 
	}

	public void updateMapVisualization(MapVisualization viz) {
		softwareMap().setMapVizualization(viz);
	}

	public void newProjectMapAvailable(IProject project) {
		if (!this.currentProject.equals(project)) return;
		this.updateVisualization();
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

	public EclipseProcessingBridge softwareMap() {
		return bridge;
	}

	public void onProjectSelectionChanged(IJavaProject javaProject) {
		IProject project = javaProject.getProject();
		if (project != currentProject) selectionTracker.theController.onProjectChanged();
		currentProject = project;
		updateVisualization();
	}

	public IProject getCurrentProject() {
		return currentProject;
	}

	public void setCurrentSize(int newDimension) {
		currentSize = newDimension;
	}

	public void updateMapdimension(int newDimension) {
		setCurrentSize(newDimension);
		IProject project = getCurrentProject();
		if (project == null) return;
		MapVisualization viz = CodemapCore.getPlugin().mapForProject(project).updateSize(newDimension).getVisualization();
		if (viz != null) {
			updateMapVisualization(viz);
		}
	}

	public void updateMap(CodemapCore codemapCore) {
		codemapCore.mapForProject(getCurrentProject()).updateMap();
	}

	public void redraw() {
		theApplet.redraw();
	}

	public void redrawCodemapBackground() {
		theApplet.redrawBackground(true);
	}

}