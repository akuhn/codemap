package org.codemap.mapview.action;

import static org.codemap.util.CodemapIcons.GREEN_CIRCLE;

import org.codemap.CodemapCore;
import org.codemap.mapview.MapController;
import org.codemap.util.CodemapIcons;

public class ShowDefaultColorsAction extends MenuAction {
	
	private static final boolean DEFAULT_VALUE = true;

	public ShowDefaultColorsAction(int style, MapController theController) {
		super("Show default colors", style, theController);
		setChecked(DEFAULT_VALUE);
		setImageDescriptor(CodemapIcons.descriptor(GREEN_CIRCLE));
	}
	
	@Override
	public void run() {
		super.run();
		if (!isChecked()) return;
		enable();
	}

	private void enable() {
	    getController().getActiveMap().getValues().colorScheme.setValue(CodemapCore.getPlugin().getDefaultColorScheme());
	}

	@Override
	protected String getKey() {
		return "show_default_colors";
	}

	@Override
	protected boolean isDefaultChecked() {
		return true;
	}	

}
