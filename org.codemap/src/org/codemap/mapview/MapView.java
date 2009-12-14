package org.codemap.mapview;

import org.codemap.CodemapCore;
import org.codemap.communication.views.CodemapRosterMenuItem;
import org.codemap.layers.CodemapVisualization;
import org.codemap.mapview.action.ActionStore;
import org.codemap.mapview.action.ColorDropDownAction;
import org.codemap.mapview.action.CommandAction;
import org.codemap.mapview.action.ForceSelectionAction;
import org.codemap.mapview.action.LabelDrowDownAction;
import org.codemap.mapview.action.LayerDropDownAction;
import org.codemap.mapview.action.LinkWithSelectionAction;
import org.codemap.mapview.action.ReloadMapAction;
import org.codemap.mapview.action.SaveAsPNGAction;
import org.codemap.mapview.action.SaveHapaxDataAction;
import org.codemap.util.CompositeActionGroup;
import org.codemap.util.EclipseUtil;
import org.codemap.util.MColor;
import org.eclipse.jdt.ui.actions.CCPActionGroup;
import org.eclipse.jdt.ui.actions.GenerateActionGroup;
import org.eclipse.jdt.ui.actions.JavaSearchActionGroup;
import org.eclipse.jdt.ui.actions.OpenEditorActionGroup;
import org.eclipse.jdt.ui.actions.OpenViewActionGroup;
import org.eclipse.jdt.ui.actions.RefactorActionGroup;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionContext;
import org.eclipse.ui.actions.ActionGroup;
import org.eclipse.ui.part.ViewPart;


public class MapView extends ViewPart {
    
    CompositeActionGroup menuActionGroups;
    
    private IMenuListener menuListener = new IMenuListener() {
        @Override
        public void menuAboutToShow(IMenuManager menu) {
            ISelection selection = theController.getSelectionProvider().getSelection();
            EclipseUtil.createStandardGroups(menu);
            menuActionGroups.setContext(new ActionContext(selection));
            menuActionGroups.fillContextMenu(menu);
            menuActionGroups.setContext(null);            
        }
    };
    
    private ActionStore actionStore = new ActionStore();

    public static final String MAP_VIEW_ID = CodemapCore.makeID(MapView.class);
    
    private static final String SEARCHBOX_MESSAGE_NO_PROJECT = "select a project to enable searching ...";
    private static final String SEARCHBOX_MESSAGE = "type text to search in ";

    private MapController theController;
    private Canvas canvas;
    private Composite mapContainer;

    private CanvasListener canvasListener;

    private LinkWithSelectionAction linkWithSelection;
    private ForceSelectionAction forceSelection;

    private IMemento memento;

    private String projectName = "";

    private SearchBar searchBar;

    private HoverShell hoverShell;

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

    @Override
    public void createPartControl(final Composite parent) {
        theController = new MapController(this);
        menuActionGroups = new CompositeActionGroup(new ActionGroup[] {
                new OpenEditorActionGroup(this),
                new OpenViewActionGroup(this),
                new CCPActionGroup(this),
                new GenerateActionGroup(this),
                new RefactorActionGroup(this),
                new JavaSearchActionGroup(this)
        });
        
        Composite composite = new Composite(parent, SWT.NONE);
        composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        GridLayoutFactory.fillDefaults().spacing(0, 0).applyTo(composite);
        
        searchBar = new SearchBar(composite, theController);
        updateSearchMessage();
        searchBar.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        mapContainer = new Composite(composite, SWT.NONE);
        mapContainer.setLayout(new FillLayout(SWT.LEFT));
        mapContainer.setLayoutData(new GridData(GridData.FILL_BOTH));
        mapContainer.setBackground(MColor.WATER.asSWTColor(parent.getDisplay()));

        canvas = new Canvas(mapContainer, SWT.DOUBLE_BUFFERED);
        hoverShell = new HoverShell(canvas);
        MenuManager menuMgr= new MenuManager();
        menuMgr.setRemoveAllWhenShown(true);
        menuMgr.addMenuListener(menuListener);
        Menu menu = menuMgr.createContextMenu(canvas);
        canvas.setMenu(menu);
        getSite().registerContextMenu(menuMgr, theController.getSelectionProvider());        
        canvasListener = new CanvasListener(canvas);
        
        composite.layout();
        updateContentDescription(" ");
        configureToolbar();
        configureActionBar();
        theController.onOpenView();
    }
    
    private void configureActionBar() {
        IActionBars actionBars = getViewSite().getActionBars();
        IMenuManager viewMenu = actionBars.getMenuManager();
        viewMenu.add(new Separator());
        viewMenu.add(new SaveAsPNGAction(theController));
        viewMenu.add(new ReloadMapAction(theController));
        viewMenu.add(new SaveHapaxDataAction(theController));        
        try {
            viewMenu.add(new CodemapRosterMenuItem());
        } catch (NoClassDefFoundError e) {
            
        }
//        viewMenu.add(new DebugLocationsAction());
    }

    private void configureToolbar() {
        IToolBarManager tbm = getToolBarManager();
        tbm.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
        tbm.add(new Separator());

        tbm.add(new ColorDropDownAction(actionStore));
        tbm.add(new LayerDropDownAction(actionStore));
        tbm.add(new LabelDrowDownAction(actionStore));

        tbm.add(linkWithSelection = new LinkWithSelectionAction(theController, memento));
        tbm.add(forceSelection = new ForceSelectionAction(theController, memento));
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
        theController.dispose();
        super.dispose();
    }
    
    @Override
    public void setFocus() {
        // FIXME: correct?
        mapContainer.setFocus();
    }
    
    @Override
    public void saveState(IMemento memento) {
        theController.onSaveState();
        linkWithSelection.saveState(memento);
        forceSelection.saveState(memento);
    }
    
    @Override
    public void init(IViewSite site, IMemento memento) throws PartInitException {
        super.init(site, memento);
        this.memento = memento;
    }

    /*default*/ void updateMapVisualization(CodemapVisualization viz) {
        canvasListener.setVisualization(viz);
        redrawAsync();
    }

    private void redrawAsync() {
        Display.getDefault().asyncExec(new Runnable() {
            @Override
            public void run() {
                if (mapContainer.isDisposed()) return;
                mapContainer.redraw();
                canvas.redraw(); // needs both!
            }
        });
    }

    protected Image newCodemapImage() {
        if (canvas == null) return null;
        Point size = canvas.getSize();
        Image image = new Image(Display.getDefault(), size.x, size.y);
        GC gc = new GC(image);
        boolean success = canvas.print(gc);
        if (!success) return null;
        return image;
    }

    /**
     * Redraw request from the controller
     */
    protected void redraw() {
        redrawAsync();
    }

    /*default*/ Composite getContainer() {
        return mapContainer;
    }

    public void updateToolTip(String name) {
        hoverShell.setText(name);
    }

    public void updateContentDescription(String name) {
        // FIXME: NN search is triggered for composite outside the map
        // and this name might be null which displays null in the content
        // description as well.
        if (name == null) return;
        setContentDescription(name);
    }

    public void updateProjectName(String projectName) {
        this.projectName = projectName.trim();
        updateSearchMessage();
    }

    private void updateSearchMessage() {
        if (projectName.equals("")) {
            searchBar.setMessage(SEARCHBOX_MESSAGE_NO_PROJECT);
        } else {
            searchBar.setMessage(SEARCHBOX_MESSAGE + projectName);
        }
    }

    public CommandAction getAction(Class<? extends CommandAction> clazz) {
        return actionStore.get(clazz);
    }
}
