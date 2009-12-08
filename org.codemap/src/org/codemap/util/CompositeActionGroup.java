package org.codemap.util;

import org.eclipse.core.runtime.Assert;

import org.eclipse.jface.action.IMenuManager;

import org.eclipse.ui.IActionBars;
import org.eclipse.ui.actions.ActionContext;
import org.eclipse.ui.actions.ActionGroup;

public class CompositeActionGroup extends ActionGroup {

    private ActionGroup[] fGroups;

    public CompositeActionGroup() {
    }

    public CompositeActionGroup(ActionGroup[] groups) {
        setGroups(groups);
    }

    protected void setGroups(ActionGroup[] groups) {
        Assert.isTrue(fGroups == null);
        Assert.isNotNull(groups);
        fGroups= groups;
    }

    public void addGroup(ActionGroup group) {
        if (fGroups == null) {
            fGroups= new ActionGroup[] { group };
        } else {
            ActionGroup[] newGroups= new ActionGroup[fGroups.length + 1];
            System.arraycopy(fGroups, 0, newGroups, 0, fGroups.length);
            newGroups[fGroups.length]= group;
            fGroups= newGroups;
        }
    }

    public void dispose() {
        super.dispose();
        if (fGroups == null)
            return;
        for (int i= 0; i < fGroups.length; i++) {
            fGroups[i].dispose();
        }
    }

    public void fillActionBars(IActionBars actionBars) {
        super.fillActionBars(actionBars);
        if (fGroups == null)
            return;
        for (int i= 0; i < fGroups.length; i++) {
            fGroups[i].fillActionBars(actionBars);
        }
    }

    public void fillContextMenu(IMenuManager menu) {
        super.fillContextMenu(menu);
        if (fGroups == null)
            return;
        for (int i= 0; i < fGroups.length; i++) {
            fGroups[i].fillContextMenu(menu);
        }
    }

    public void setContext(ActionContext context) {
        super.setContext(context);
        if (fGroups == null)
            return;
        for (int i= 0; i < fGroups.length; i++) {
            fGroups[i].setContext(context);
        }
    }

    public void updateActionBars() {
        super.updateActionBars();
        if (fGroups == null)
            return;
        for (int i= 0; i < fGroups.length; i++) {
            fGroups[i].updateActionBars();
        }
    }
}
