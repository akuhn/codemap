package org.codemap.mapview;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;

public abstract class DropDownAction extends Action implements IMenuCreator {
	
	private Menu menu;
	
	public DropDownAction() {
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
	
    protected void addActionToMenu(Menu parent, Action action) {
        ActionContributionItem item = new ActionContributionItem(action);
        item.fill(parent, -1);
    }

	protected void setup() {
		// maybe do stuff ...
	}
	
	protected abstract void createMenu(Menu menu);    

}
