package org.codemap.mapview.action;

import org.codemap.mapview.MapController;
import org.codemap.util.EclipseUtil;
import org.eclipse.jface.action.Action;

public class SaveHapaxDataAction extends Action {
    
    private MapController controller;

    public SaveHapaxDataAction(MapController theController) {
        super();
        setText("Save vocabulary data");
        controller = theController;
    }

    @Override
    public void run() {
        String fname = EclipseUtil.filenameFromUser("Vocabulary of "+controller.utils().activeProjectName().replace('.', '-'), ".png");
        if (fname == null) return;
        saveTo(fname);
    }

    private void saveTo(String fname) {
        //
    }
    
}
