package org.codemap.ecftest.views;

import org.codemap.ecftest.StringShare;
import org.codemap.ecftest.ECFTestPlugin;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ecf.core.IContainer;
import org.eclipse.ecf.core.user.IUser;
import org.eclipse.ecf.presence.roster.IRoster;
import org.eclipse.ecf.presence.roster.IRosterEntry;
import org.eclipse.ecf.presence.ui.menu.AbstractRosterMenuHandler;
import org.eclipse.jface.dialogs.ErrorDialog;

public class CodemapRosterMenuHandler extends AbstractRosterMenuHandler {
    
    public CodemapRosterMenuHandler(IRosterEntry entry) {
        super(entry);
    }

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        IRosterEntry rosterEntry = getRosterEntry();
        if (rosterEntry == null) return null;
        
        IRoster roster = rosterEntry.getRoster();
        IContainer container = (IContainer) roster.getPresenceContainerAdapter().getAdapter(IContainer.class);
        if (container.getConnectedID() == null)
            showErrorMessage("not connected.");
            StringShare sender = ECFTestPlugin.getDefault().getStringShare(container.getID());
            if (sender == null)
                showErrorMessage("no sender.");
//            currently share anyway to be able do do better debugging
//            if (sender.isSharing())
//                showErrorMessage("sharing already started.");
            
            
//            final ITextEditor textEditor = getTextEditor();
//            if (textEditor == null)
//                showErrorMessage(Messages.DocShareRosterMenuHandler_EXCEPTION_EDITOR_NOT_TEXT);
//            final String inputName = getInputName(textEditor);
//            if (inputName == null)
//                showErrorMessage(Messages.DocShareRosterMenuHandler_NO_FILENAME_WITH_CONTENT);
            final IUser user = roster.getUser();
            sender.startShare(user.getID(), user.getName(), rosterEntry.getUser().getID());
        
        return null;
    }
    

    private void showErrorMessage(String errorMessage) {
        ErrorDialog.openError(null, "error", errorMessage, new Status(IStatus.ERROR, ECFTestPlugin.PLUGIN_ID, errorMessage, null));
    }    

}
