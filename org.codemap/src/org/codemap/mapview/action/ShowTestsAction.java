package org.codemap.mapview.action;

import org.codemap.CodemapCore;
import org.codemap.resources.EclipseMapValues;
import org.eclipse.jface.action.IAction;

public class ShowTestsAction extends MenuAction {
    
    public ShowTestsAction() {
        super("Show Tests", IAction.AS_CHECK_BOX);
    }
    
    @Override
    public void run() {
        EclipseMapValues values = CodemapCore.getPlugin().getActiveMap().getValues();
        values.showTests.setValue(isChecked());
        System.out.println(values.showTests.getValue());
        super.run();
    }

    @Override
    protected String getKey() {
        return "show_tests";
    }

    @Override
    protected boolean isDefaultChecked() {
        return true;
    }
}
