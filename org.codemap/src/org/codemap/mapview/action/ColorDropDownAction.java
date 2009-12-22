package org.codemap.mapview.action;

import static org.codemap.util.CodemapIcons.PALETTE;

import org.codemap.util.CodemapIcons;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
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

public class ColorDropDownAction extends DropDownAction {

	private ShowDefaultColorsAction showDefaultColorsAction;
    private ShowPackageColorsAction showPackageColorsAction;
    private ShowHeatMapColorsAction showHeatmapColorsAction;
    private ShowCoverageAction showCoverageAction;

	public ColorDropDownAction(ActionStore actionStore) {
	    super();
	    showDefaultColorsAction = new ShowDefaultColorsAction(actionStore);
	    showPackageColorsAction = new ShowPackageColorsAction(actionStore);
	    showHeatmapColorsAction = new ShowHeatMapColorsAction(actionStore);
	    showCoverageAction = new ShowCoverageAction(actionStore);            
    }

    @Override
	protected void setup() {
		setText("Colors"); 
		setImageDescriptor(CodemapIcons.descriptor(PALETTE));
	}

	@Override
	protected void createMenu(Menu menu) {
		addActionToMenu(menu, showDefaultColorsAction);		
		addActionToMenu(menu, showPackageColorsAction);
		addActionToMenu(menu, showHeatmapColorsAction);
		addActionToMenu(menu, showCoverageAction);
	}
}
