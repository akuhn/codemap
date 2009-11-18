package org.codemap.mapview.action;

import org.codemap.CodemapCore;
import org.codemap.resources.EclipseMapValues;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;

public class ShowTestsAction extends Action {
    
    public ShowTestsAction() {
        super("Show Tests", IAction.AS_CHECK_BOX);
    }
    
    @Override
    public void run() {
        EclipseMapValues values = CodemapCore.getPlugin().getActiveMap().getValues();
        values.showTests.setValue(isChecked());
    }
}
