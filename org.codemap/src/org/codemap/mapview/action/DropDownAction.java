package org.codemap.mapview.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

public abstract class DropDownAction extends Action implements IMenuCreator {

    private Menu menu;

    public DropDownAction() {
        super("", AS_DROP_DOWN_MENU);
        setMenuCreator(this);
        setup();
    }

    @Override
    public void dispose() {
        if (menu != null) menu.dispose();
    }

    @Override
    public Menu getMenu(Control parent) {
        if (menu == null) {
            menu = new Menu(parent);
            createMenu(menu);
        }
        return menu;
    }

    @Override
    public Menu getMenu(Menu parent) {
        // submenus are not supported ...
        return null;
    }

    public void addActionToMenu(Menu parent, Action action) {
        ActionContributionItem item = new ActionContributionItem(action);
        item.fill(parent, -1);
    }

    protected void setup() {
        // maybe do stuff ...
    }

    /**
     * Create and show the menu when clicked. 
     * 
     * Creates and shows the same menu that appears when clicking the black menu-arrow. 
     * The menu appears when the Action/Icon is clicked. 
     */
    public void runWithEvent(Event event) {
        if (event.widget instanceof ToolItem) {
            ToolItem toolItem= (ToolItem) event.widget;
            Control control= toolItem.getParent();
            Menu menu= getMenu(control);

            Rectangle bounds= toolItem.getBounds();
            Point topLeft= new Point(bounds.x, bounds.y + bounds.height);
            menu.setLocation(control.toDisplay(topLeft));
            menu.setVisible(true);
        }
    } 

    /**
     * Tells the DropDownAction that it should listen to mouse events on a given
     * ToolBarManager. Used to display the Menu when the mouse is clicked.
     */
    public IAction listenToMouseOn(IToolBarManager tbm) {
        final ToolBar tb = ((ToolBarManager)tbm).getControl();
        tb.addMouseListener(new MouseAdapter() {
            public void mouseDown(MouseEvent e) {
                ToolItem ti = tb.getItem(new Point(e.x, e.y));
                if (!(ti.getData() instanceof ActionContributionItem)) return;

                ActionContributionItem actionContributionItem = (ActionContributionItem) ti.getData();
                IAction action = actionContributionItem.getAction();
                if (action != DropDownAction.this) return;
                
                Event event = new Event();
                event.widget = ti;
                event.x = e.x;
                event.y = e.y;
                action.runWithEvent(event);
            }
        });  
        return this;
    } 

    protected abstract void createMenu(Menu newMenu); 
}
