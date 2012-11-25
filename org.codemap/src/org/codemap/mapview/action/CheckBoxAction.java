package org.codemap.mapview.action;

import org.eclipse.jface.action.IAction;

public abstract class CheckBoxAction extends CommandAction {

    public CheckBoxAction(String text, ActionStore actionStore) {
        super(text, IAction.AS_CHECK_BOX, actionStore);
    }

}
