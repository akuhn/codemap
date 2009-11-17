package org.codemap.mapview.action;

import static org.codemap.util.CodemapIcons.PALETTE;

import org.codemap.mapview.MapController;
import org.codemap.util.CodemapIcons;
import org.codemap.util.ExtensionPoints;
import org.eclipse.swt.widgets.Menu;

public class ColorDropDownAction extends ExtensionPointDropDownAction {

	private MapController theController;

	public ColorDropDownAction(MapController controller) {
		theController = controller;
	}

	@Override
	protected void setup() {
		setText("Colors"); 
		setImageDescriptor(new CodemapIcons().descriptor(PALETTE));
	}

	@Override
	protected void createDefaultMenu(Menu menu) {
		addActionToMenu(menu, new ShowDefaultColorsAction(getActionStyle()));		
		addActionToMenu(menu, new ShowPackageColorsAction(theController, getActionStyle()));
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
