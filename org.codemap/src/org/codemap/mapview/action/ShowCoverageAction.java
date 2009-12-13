package org.codemap.mapview.action;

import static org.codemap.util.CodemapIcons.COVERAGE;
import static org.codemap.util.CodemapIcons.descriptor;

import org.codemap.CodemapCore;
import org.eclipse.jface.resource.ImageDescriptor;


public class ShowCoverageAction extends RadioButtonAction {
   
    public ShowCoverageAction(ActionStore actionStore) {
        super("Color by Coverage", actionStore);
        boolean enabled = CodemapCore.getPlugin().getController().utils().isEclemmaPluginAvailable();
        setEnabled(enabled);
    }

    @Override
    protected ImageDescriptor getImage() {
        return descriptor(COVERAGE);
    }

}
