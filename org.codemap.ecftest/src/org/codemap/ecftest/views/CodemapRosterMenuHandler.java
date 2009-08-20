package org.codemap.ecftest.views;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ecf.presence.roster.IRosterEntry;
import org.eclipse.ecf.presence.ui.menu.AbstractRosterMenuHandler;

public class CodemapRosterMenuHandler extends AbstractRosterMenuHandler {

    public CodemapRosterMenuHandler(IRosterEntry entry) {
        super(entry);
    }

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        // TODO Auto-generated method stub
        return null;
    }

}
