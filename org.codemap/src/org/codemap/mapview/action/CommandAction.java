package org.codemap.mapview.action;

import org.codemap.commands.Command;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

public abstract class CommandAction extends Action {

    private Command command;

    public CommandAction(String text, int style, ActionStore actionStore) {
        super(text, style);
        ImageDescriptor img = getImage();
        if (img != null) {
            setImageDescriptor(img);            
        }
        actionStore.put(this);
    }

    protected abstract ImageDescriptor getImage();

    public void setCommand(Command c) {
        command = c;
        setChecked(c.isEnabled());
    }

    @Override
    public void run() {
        super.run();
        command.setEnabled(isChecked());
    }
}
