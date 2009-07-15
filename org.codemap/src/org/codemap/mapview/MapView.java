package org.codemap.mapview;

import java.util.ArrayList;

import org.codemap.CodemapCore;
import org.codemap.util.EclipseProcessingBridge;
import org.codemap.util.Log;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
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
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.ViewPart;

import ch.deif.meander.Location;
import ch.deif.meander.swt.CodemapVisualization;
import ch.deif.meander.ui.CodemapEvent;
import ch.deif.meander.ui.CodemapListener;
import ch.deif.meander.ui.MeanderApplet;
import ch.deif.meander.util.MColor;
import ch.deif.meander.visual.CurrentSelectionOverlay;
import ch.deif.meander.visual.MapVisualization;

// TODO let MapController track the currently active project.
public class MapView extends ViewPart {

	public static final String MAP_VIEW_ID = CodemapCore.makeID(MapView.class);
	
//	private EclipseProcessingBridge bridge;
//	private IJavaProject currentProject;
	private MapSelectionProvider selectionProvider;
	private SelectionTracker selectionTracker;
//	private Composite container;
//	private MeanderApplet theApplet;
	private final MapController theController;
	private int currentSize;

	private Canvas canvas;

	private Composite container;
	
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
		
		canvas = new Canvas(container, SWT.NONE);		
		
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
//		clearContainer();
//		bridge = new EclipseProcessingBridge(container, theApplet);
//		softwareMap().getApplet().addListener(makeListener());
		CodemapCore.getPlugin().setMapView(this);
		new ResizeListener(container, theController);
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
				StructuredSelection structuredSelection = new StructuredSelection(selection);
				selectionProvider.setSelection(structuredSelection);
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
	    tbm.add(new LayerDropDownAction(selectionTracker, selectionProvider));
	    tbm.add(new LabelDrowDownAction());
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
		canvas.setFocus();
	}

	public void updateVisualization() {
		CodemapVisualization viz = CodemapCore.getPlugin()
			.mapForProject(getCurrentProject())
			.updateSize(getCurrentSize())
			.enableBuilder()
			.getVisualization();
		if (viz == null) return;
		updateMapVisualization(viz);
	}

	private int getCurrentSize() {
		return currentSize; 
	}

	public void updateMapVisualization(CodemapVisualization viz) {
		viz.link(canvas);
	}

	public void newProjectMapAvailable(IJavaProject project) {
		if (!this.getCurrentProject().equals(project)) return;
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

	public void onProjectSelectionChanged(IJavaProject project) {
//		if (project == currentProject) return;
//		currentProject = project;
		updateVisualization();
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

	public void updateMap(CodemapCore codemapCore) {
		codemapCore.mapForProject(getCurrentProject()).updateMap();
	}

	public void redraw() {
//		FIXME
//		theApplet.redraw();
	}

	public void redrawCodemapBackground() {
//		FIXME
//		theApplet.redrawBackground(true);
	}

}