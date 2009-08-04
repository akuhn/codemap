package org.codemap.mapview;

import java.util.ArrayList;
import java.util.List;

import org.codemap.MapPerProject;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;

public abstract class DropDownAction extends CodemapAction implements IMenuCreator {
	
	private Menu menu;
	private List<CodemapAction> actions = new ArrayList<CodemapAction>();
	
	@Override
	public void configureAction(MapPerProject map) {
		for(CodemapAction each: actions) {
			each.configureAction(map);
		}
	}

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
	
    protected void addActionToMenu(Menu parent, CodemapAction action) {
    	actions.add(action);
        ActionContributionItem item = new ActionContributionItem(action);
        item.fill(parent, -1);
    }

	protected void setup() {
		// maybe do stuff ...
	}
	
	protected abstract void createMenu(Menu newMenu);    

}
