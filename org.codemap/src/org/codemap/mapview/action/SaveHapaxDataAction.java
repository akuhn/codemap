package org.codemap.mapview.action;

import org.codemap.CodemapCore;
import org.codemap.util.EclipseUtil;
import org.eclipse.jface.action.Action;

public class SaveHapaxDataAction extends Action {
    
    public SaveHapaxDataAction() {
        super();
        setText("Save vocabulary data");
    }

    @Override
    public void run() {
        String fname = EclipseUtil.filenameFromUser("Vocabulary of "+CodemapCore.activeProjectName().replace('.', '-'), ".png");
        if (fname == null) return;
        saveTo(fname);
    }

    private void saveTo(String fname) {
        //
    }
    
}
