package org.codemap.mapview.action;

import org.codemap.CodemapCore;
import org.codemap.mapview.MapController;
import org.codemap.resources.MapValues;
import org.eclipse.jface.action.IAction;


public class ShowTestsAction extends MenuAction {
    
    public ShowTestsAction(MapController theController) {
        super("Show Tests", IAction.AS_CHECK_BOX, theController);
    }
    
    @Override
    public void run() {
        MapValues values = CodemapCore.getPlugin().getActiveMap().getValues();
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
