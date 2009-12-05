package org.codemap.mapview;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.codemap.CodemapCore;
import org.codemap.MapPerProject;
import org.codemap.layers.CodemapVisualization;
import org.codemap.mapview.action.CodemapAction;
import org.codemap.mapview.action.ColorDropDownAction;
import org.codemap.mapview.action.ForceSelectionAction;
import org.codemap.mapview.action.LabelDrowDownAction;
import org.codemap.mapview.action.LayerDropDownAction;
import org.codemap.mapview.action.LinkWithSelectionAction;
import org.codemap.mapview.action.ReloadMapAction;
import org.codemap.mapview.action.SaveAsPNGAction;
import org.codemap.mapview.action.SaveHapaxDataAction;
import org.codemap.mapview.action.ShowTestsAction;
import org.codemap.util.ExtensionPoints;
import org.codemap.util.Log;
import org.codemap.util.MColor;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;


public class MapView extends ViewPart {

    private List<CodemapAction> actions = new ArrayList<CodemapAction>();

    public static final String MAP_VIEW_ID = CodemapCore.makeID(MapView.class);
    private static final String ATTR_CLASS = "class";

    private final MapController theController;
    private MapSelectionProvider selectionProvider;
    private SelectionTracker selectionTracker;
    private int currentSize;
    private Canvas canvas;
    private Composite container;

    private CanvasListener canvasListener;

    private LinkWithSelectionAction linkWithSelection;

    private ForceSelectionAction forceSelection;

    private IMemento memento;

    class ViewLabelProvider extends LabelProvider implements
            ITableLabelProvider {

        public String getColumnText(Object obj, int index) {
            return getText(obj);
        }

        public Image getColumnImage(Object obj, int index) {
            return getImage(obj);
        }

        @Override
        public Image getImage(Object obj) {
            return PlatformUI.getWorkbench().getSharedImages().getImage(
                    ISharedImages.IMG_OBJ_ELEMENT);
        }
    }

    public MapView() {
        theController = new MapController(this);
    }

    @Override
    public void createPartControl(final Composite parent) {
        container = new Composite(parent, SWT.NONE);
        container.setLayout(new FillLayout(SWT.LEFT));

        Color swtColor = MColor.WATER.asSWTColor(parent.getDisplay());
        container.setBackground(swtColor);

        canvas = new Canvas(container, SWT.DOUBLE_BUFFERED);
        canvasListener = new CanvasListener(canvas);
        container.layout();

        selectionProvider = new MapSelectionProvider(this);
        selectionTracker = new SelectionTracker(theController);
        
        configureToolbar();
        configureActionBar();

        showMap();
        theController.onOpenView();
    }


    private void showMap() {
        // clearContainer();
        CodemapCore.getPlugin().setMapView(this);
        new ResizeListener(container, theController);
        theController.onShowMap();
    }
    
    private void configureActionBar() {
        IActionBars actionBars = getViewSite().getActionBars();
        IMenuManager viewMenu = actionBars.getMenuManager();
        viewMenu.add(new Separator());
        viewMenu.add(new SaveAsPNGAction());
        viewMenu.add(new ReloadMapAction());
        viewMenu.add(new SaveHapaxDataAction());        
        viewMenu.add(registerAction(new ShowTestsAction()));
//        viewMenu.add(new DebugLocationsAction());
        
        IExtensionPoint extensionPoint = Platform.getExtensionRegistry().getExtensionPoint(CodemapCore.PLUGIN_ID, ExtensionPoints.ACTION_BAR);
        IExtension[] extensions_arr = extensionPoint.getExtensions();
        List<IExtension> extensions = Arrays.asList(extensions_arr);
        for (IExtension extension: extensions) {
            parseActionbarExtensionPoint(extension.getConfigurationElements(), viewMenu);
        }        
    }

    private void parseActionbarExtensionPoint(IConfigurationElement[] configurationElements, IMenuManager viewMenu) {
        List<IConfigurationElement> configelems = Arrays.asList(configurationElements);
        for (IConfigurationElement each: configelems) {
            try {
                IContributionItem item = (IContributionItem) each.createExecutableExtension(ATTR_CLASS);
                viewMenu.add(item);
            } catch (Exception e) {
                Log.instantiatePluginError(e, each, ATTR_CLASS);
            }            
        }
    }

    private void configureToolbar() {
        IToolBarManager tbm = getToolBarManager();
        tbm.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
        tbm.add(new Separator());

        tbm.add(registerAction(new ColorDropDownAction(theController)));
        tbm.add(registerAction(new LayerDropDownAction(theController)));
        tbm.add(registerAction(new LabelDrowDownAction()));

        tbm.add(linkWithSelection = new LinkWithSelectionAction(selectionTracker, memento));
        tbm.add(forceSelection = new ForceSelectionAction(selectionProvider, memento));
    }

    private IAction registerAction(CodemapAction action) {
        actions.add(action);
        return action;
    }

    protected IToolBarManager getToolBarManager() {
        return getViewSite().getActionBars().getToolBarManager();
    }

    /**
     * Sent when view is closed.
     * 
     */
    @Override
    public void dispose() {
        CodemapCore.getPlugin().setMapView(null);
        theController.dispose();        
        selectionTracker.dispose();
    }
    
    @Override
    public void setFocus() {
        // FIXME: correct?
        container.setFocus();
    }
    
    @Override
    public void saveState(IMemento memento) {
        linkWithSelection.saveState(memento);
        forceSelection.saveState(memento);
    }
    
    @Override
    public void init(IViewSite site, IMemento memento) throws PartInitException {
        super.init(site, memento);
        this.memento = memento;
    }
    
    public void newProjectSelected() {
        MapPerProject activeMap = CodemapCore.getPlugin().getActiveMap();
        configureActions(activeMap);
        CodemapVisualization viz = activeMap
                .updateSize(currentSize)
                .getVisualization();
        if (viz == null)
            return;
        updateMapVisualization(viz);
    }

    public void configureActions(MapPerProject activeMap) {
        for (CodemapAction each: actions) {
            each.configureAction(activeMap);
        }
    }

    private void updateMapVisualization(CodemapVisualization viz) {
        canvasListener.setVisualization(viz);
        redrawAsync();
    }

    public IJavaProject getCurrentProject() {
        return theController.getCurrentProject();
    }

    public void setCurrentSize(int newDimension) {
        currentSize = newDimension;
    }

    public void updateMapdimension(int newDimension) {
        currentSize = newDimension;
        IJavaProject project = getCurrentProject();
        if (project == null) return;
        CodemapVisualization viz = CodemapCore.getPlugin()
                .mapForProject(project)
                .updateSize(currentSize)
                .getVisualization();
        if (viz != null) {
            updateMapVisualization(viz);
        }
    }

    private void redrawAsync() {
        Display.getDefault().asyncExec(new Runnable() {
            @Override
            public void run() {
                if (container.isDisposed()) return;
                container.redraw();
                canvas.redraw(); // needs both!
            }
        });
    }
    
    /**
     * Might return null if the image could not be rendered.
     * Please make sure to dispose the image once you do not need
     * it any longer.
     * 
     * @return a new Image instance representing the current Codemap.
     */
    public Image newCodemapImage() {
        if (canvas == null) return null;
        Point size = canvas.getSize();
        Image image = new Image(Display.getDefault(), size.x, size.y);
        GC gc = new GC(image);
        boolean success = canvas.print(gc);
        if (!success) return null;
        return image;
    }

    public MapSelectionProvider getSelectionProvider() {
        return selectionProvider;
    }
}
