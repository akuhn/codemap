package org.codemap.mapview.action;

import static org.codemap.util.CodemapIcons.GREEN_CIRCLE;
import static org.codemap.util.CodemapIcons.descriptor;

import org.eclipse.jface.resource.ImageDescriptor;

public class ShowDefaultColorsAction extends RadioButtonAction {
	
	public ShowDefaultColorsAction(ActionStore actionStore) {
		super("Show default Colors", actionStore);
	}

    @Override
    protected ImageDescriptor getImage() {
        return descriptor(GREEN_CIRCLE);
    }

}
