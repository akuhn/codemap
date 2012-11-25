package org.codemap.mapview.action;

import org.eclipse.jface.action.IAction;

public abstract class RadioButtonAction extends CommandAction {

    public RadioButtonAction(String text, ActionStore actionStore) {
        super(text, IAction.AS_RADIO_BUTTON, actionStore);
    }
}
