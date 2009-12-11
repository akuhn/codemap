package org.codemap.mapview.action;

import static org.codemap.util.CodemapIcons.PALETTE;

import org.codemap.eclemma.ShowCoverageAction;
import org.codemap.mapview.MapController;
import org.codemap.util.CodemapIcons;
import org.codemap.util.ExtensionPoints;
import org.eclipse.swt.widgets.Menu;

public class ColorDropDownAction extends ExtensionPointDropDownAction {

	private ShowDefaultColorsAction showDefaultColorsAction;
    private ShowPackageColorsAction showPackageColorsAction;
    private ShowHeatMapColorsAction showHeatmapColorsAction;
    private ShowCoverageAction showCoverageAction;

    public ColorDropDownAction(MapController theController) {
	    super(theController);
	    registerAction(showDefaultColorsAction = new ShowDefaultColorsAction(getActionStyle(), getController()));
	    registerAction(showPackageColorsAction = new ShowPackageColorsAction(getActionStyle(), getController()));
        registerAction(showHeatmapColorsAction = new ShowHeatMapColorsAction(getActionStyle(), getController()));
        registerAction(showCoverageAction = new ShowCoverageAction(getActionStyle(), getController()));
	}

	@Override
	protected void setup() {
		setText("Colors"); 
		setImageDescriptor(CodemapIcons.descriptor(PALETTE));
	}

	@Override
	protected void createDefaultMenu(Menu menu) {
		addActionToMenu(menu, showDefaultColorsAction);		
		addActionToMenu(menu, showPackageColorsAction);
		addActionToMenu(menu, showHeatmapColorsAction);
		addActionToMenu(menu, showCoverageAction);
	}

	@Override
	protected String getExtensionPointName() {
		return ExtensionPoints.COLORS;
	}

	@Override
	protected int getActionStyle() {
		return AS_RADIO_BUTTON;
	}
}
