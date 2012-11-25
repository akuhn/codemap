package org.codemap.mapview.action;

import static org.codemap.util.CodemapIcons.PALETTE;

import org.codemap.util.CodemapIcons;
import org.eclipse.swt.widgets.Menu;

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
