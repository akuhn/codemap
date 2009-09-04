package org.codemap.plugin.communication.views;

import org.codemap.plugin.communication.ECFPlugin;
import org.codemap.plugin.communication.SelectionShare;
import org.eclipse.ecf.core.IContainer;
import org.eclipse.ecf.presence.IPresenceContainerAdapter;
import org.eclipse.ecf.presence.roster.IRoster;
import org.eclipse.ecf.presence.roster.IRosterEntry;
import org.eclipse.ecf.presence.ui.menu.AbstractRosterMenuContributionItem;
import org.eclipse.ecf.presence.ui.menu.AbstractRosterMenuHandler;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.Separator;

public class CodemapRosterMenuItem extends AbstractRosterMenuContributionItem {
    
    /*
     * @see DocShareRosterMenuContributionItem
     * @see DocShareRosterMenuHandler
     */
    public CodemapRosterMenuItem() {
        super();
        setTopMenuName("Share open files");
    }

    @Override
    protected IContributionItem[] getContributionItems() {
        // If we are already engaged in a share (either as initiator or as receiver)
        // Then present menu item to stop
        for (Object each: getPresenceContainerAdapters()) {
            if (!(each instanceof IPresenceContainerAdapter)) continue;
            IPresenceContainerAdapter pca = (IPresenceContainerAdapter) each;
            
            SelectionShare share = getStringShareForPresenceContainerAdapter(pca);
            if (share != null && share.isSharing()) {
                return getMenuContributionForStopShare(pca.getRosterManager().getRoster(), share);
            }
        }
        return super.getContributionItems();
    }

    protected IContributionItem[] getMenuContributionForStopShare(IRoster roster, final SelectionShare share) {
        final IAction stopEditorShare = new Action() {
            public void run() {
                share.stopShare();
            }
        };
        stopEditorShare.setText("Stop sharing with: " + share.getRemoteName());
        stopEditorShare.setImageDescriptor(getTopMenuImageDescriptor());
        return new IContributionItem[] {new Separator(), new ActionContributionItem(stopEditorShare)};
    }        

    protected SelectionShare getStringShareForPresenceContainerAdapter(IPresenceContainerAdapter presenceContainerAdapter) {
        final IContainer container = (IContainer) presenceContainerAdapter.getAdapter(IContainer.class);
        if (container == null)
            return null;
        return ECFPlugin.getDefault().getStringShare(container.getID());
    }        

    @Override
    protected AbstractRosterMenuHandler createRosterEntryHandler(IRosterEntry rosterEntry) {
        return new CodemapRosterMenuHandler(rosterEntry);
    }

}
