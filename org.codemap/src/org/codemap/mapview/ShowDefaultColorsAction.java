package org.codemap.mapview;

import static org.codemap.util.Icons.GREEN_CIRCLE;

import org.codemap.CodemapCore;
import org.codemap.util.Icons;
import org.eclipse.jface.action.Action;

public class ShowDefaultColorsAction extends Action {
	
	private static final boolean DEFAULT_VALUE = true;

	public ShowDefaultColorsAction(int style) {
		super("Show default colors", style);
		setChecked(DEFAULT_VALUE);
		setImageDescriptor(Icons.getImageDescriptor(GREEN_CIRCLE));
	}
	
	@Override
	public void run() {
		if (!isChecked()) return;
		enable();
	}

	private void enable() {
		CodemapCore core = CodemapCore.getPlugin();
		core.getColorScheme().clearColors();
		core.redrawCodemapBackground();
	}	

}
