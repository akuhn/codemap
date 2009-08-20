package org.codemap.ecftest.views;

import org.eclipse.ecf.presence.roster.IRosterEntry;
import org.eclipse.ecf.presence.ui.menu.AbstractRosterMenuContributionItem;
import org.eclipse.ecf.presence.ui.menu.AbstractRosterMenuHandler;

public class CodemapRosterMenuItem extends AbstractRosterMenuContributionItem {
    
    public CodemapRosterMenuItem() {
        super();
        setTopMenuName("Share open files");
    }

    @Override
    protected AbstractRosterMenuHandler createRosterEntryHandler(IRosterEntry rosterEntry) {
        return new CodemapRosterMenuHandler(rosterEntry);
    }

}
