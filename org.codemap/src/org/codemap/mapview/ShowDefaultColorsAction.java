package org.codemap.mapview;

import static org.codemap.util.Icons.GREEN_CIRCLE;

import org.codemap.CodemapCore;
import org.codemap.MapPerProject;
import org.codemap.util.Icons;

public class ShowDefaultColorsAction extends CodemapAction {
	
	private static final boolean DEFAULT_VALUE = true;
	private static final String KEY = "show_default_colors";

	public ShowDefaultColorsAction(int style) {
		super("Show default colors", style);
		setChecked(DEFAULT_VALUE);
		setImageDescriptor(Icons.getImageDescriptor(GREEN_CIRCLE));
	}
	
	@Override
	public void run() {
		CodemapCore.getPlugin().getActiveMap().setProperty(KEY, isChecked());
		if (!isChecked()) return;
		enable();
	}

	private void enable() {
		CodemapCore core = CodemapCore.getPlugin();
		core.getColorScheme().clearColors();
		core.redrawCodemapBackground();
	}

	@Override
	public void configureAction(MapPerProject map) {
		setChecked(map.getPropertyOrDefault(KEY, DEFAULT_VALUE));
	}	

}
