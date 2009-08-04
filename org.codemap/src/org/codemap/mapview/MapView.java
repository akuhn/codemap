package org.codemap.mapview;

import java.util.ArrayList;
import java.util.List;

import org.codemap.CodemapCore;
import org.codemap.MapPerProject;
import org.codemap.util.Log;
import org.codemap.util.Resources;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.ViewPart;

import ch.deif.meander.Location;
import ch.deif.meander.MapSelection;
import ch.deif.meander.swt.CodemapVisualization;
import ch.deif.meander.swt.CurrSelectionOverlay;
import ch.deif.meander.ui.CodemapEvent;
import ch.deif.meander.ui.CodemapListener;
import ch.deif.meander.util.MColor;

public class MapView extends ViewPart {
	
	private List<DropDownAction> actions = new ArrayList<DropDownAction>();
	
	private CodemapListener codemapListener = new CodemapListener() {
		@Override
		public void handleEvent(CodemapEvent event) {
			if (CurrSelectionOverlay.EVT_DOUBLE_CLICKED == event.getKind()) {
				doubleClicked((Location) event.getValue());					
			} else if (CurrSelectionOverlay.EVT_SELECTION_CHANGED == event.getKind()) {
				selectionChanged((MapSelection) event.getValue());
			}
		}

		public void selectionChanged(MapSelection mapSelection) {
			final ArrayList<IJavaElement> selection = new ArrayList<IJavaElement>();
			for (String each: mapSelection) {
				IJavaElement javaElement = Resources.asJavaElement(each);
				selection.add(javaElement);
			}
			StructuredSelection structuredSelection = new StructuredSelection(selection);
			selectionProvider.setSelection(structuredSelection);
		}	

		public void doubleClicked(Location location) {
			IResource resource = Resources.asResource(location.getDocument());
			if (!(resource instanceof IFile)) return;
			openInEditor((IFile) resource);
		}
	};

	public static final String MAP_VIEW_ID = CodemapCore.makeID(MapView.class);

	private static final String LINK_SELECTION_GROUP = CodemapCore.PLUGIN_ID + ".linkselection";
	
	private final MapController theController;
	private MapSelectionProvider selectionProvider;
	private SelectionTracker selectionTracker;
	private int currentSize;
	private Canvas canvas;
	private Composite container;

	private CodemapVisualization currentViz;
	
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
		
		canvas = new Canvas(container, SWT.DOUBLE_BUFFERED);		
		
		container.layout();
		MColor water = MColor.WATER;
		Color swtColor = new Color(null, water.getRed(), water.getGreen(), water.getBlue());		
		container.setBackground(swtColor);

		selectionProvider = new MapSelectionProvider(this);
		selectionTracker = new SelectionTracker(theController);
		configureToolbar();
		
		showMap();
		theController.onOpenView();
	}

	private void showMap() {
//		clearContainer();
//		bridge = new EclipseProcessingBridge(container, theApplet);
//		softwareMap().getApplet().addListener(makeListener());
		CodemapCore.getPlugin().setMapView(this);
		new ResizeListener(container, theController);
		theController.onShowMap();
	}

	private void configureToolbar() {
	    IToolBarManager tbm = getToolBarManager();
	    tbm.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	    tbm.add(new Separator(LINK_SELECTION_GROUP));
	    
	    tbm.appendToGroup(LINK_SELECTION_GROUP, new LinkWithSelectionAction(selectionTracker));
	    tbm.appendToGroup(LINK_SELECTION_GROUP, new ForceSelectionAction(selectionProvider));
   
	    tbm.add(new Separator());
	    tbm.add(registerAction(new ColorDropDownAction(theController)));
	    tbm.add(registerAction(new LayerDropDownAction()));
	    tbm.add(registerAction(new LabelDrowDownAction()));
	}

	private IAction registerAction(DropDownAction action) {
		actions.add(action);
		return action;
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
//		FIXME: correct?
		container.setFocus();
	}

	public void updateVisualization() {
		MapPerProject activeMap = CodemapCore.getPlugin().getActiveMap();
		configureActions(activeMap);
		CodemapVisualization viz = activeMap.updateSize(getCurrentSize()).getVisualization();
		if (viz == null) return;
		updateMapVisualization(viz);
	}

	private void configureActions(MapPerProject activeMap) {
		for (DropDownAction each: actions) {
			each.configureAction(activeMap);
		}
	}

	private int getCurrentSize() {
		return currentSize; 
	}

	public void updateMapVisualization(CodemapVisualization viz) {
		if (currentViz != null) {
			currentViz.unlink();
			currentViz.removeListener(codemapListener);
		}
		viz.link(canvas);
		viz.addListener(codemapListener);
		currentViz = viz;
		redrawAsync();
	}

	public void newProjectMapAvailable(IJavaProject project) {
		if (!this.getCurrentProject().equals(project)) return;
		this.updateVisualization();
	}

	private void openInEditor(final IFile file) {
		final IWorkbenchPage page = getSite().getPage();
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				try {
					IDE.openEditor(page, file, true);
				} catch (PartInitException e) {
					Log.error(e);
				}
			}
		});
	}

	public IJavaProject getCurrentProject() {
		return theController.getCurrentProject();
	}

	public void setCurrentSize(int newDimension) {
		currentSize = newDimension;
	}

	public void updateMapdimension(int newDimension) {
		setCurrentSize(newDimension);
		IJavaProject project = getCurrentProject();
		if (project == null) return;
		CodemapVisualization viz = CodemapCore.getPlugin().mapForProject(project).updateSize(newDimension).getVisualization();
		if (viz != null) {
			updateMapVisualization(viz);
		}
	}

	public void redrawAsync() {
		// Must call #syncExec, else we get an SWT error 		
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				redraw();
			}
		});
	}

	public void redraw() {
		container.redraw();
		canvas.redraw(); // needs both!
	}

	public void redrawCodemapBackground() {
		currentViz.redrawBackground();
	}

}