package org.codemap.ecftest;

import java.util.Collection;

import org.eclipse.core.runtime.Assert;
import org.eclipse.ecf.core.IContainer;
import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.util.ECFException;
import org.eclipse.ecf.core.util.Trace;
import org.eclipse.ecf.datashare.AbstractShare;
import org.eclipse.ecf.datashare.IChannelContainerAdapter;
import org.eclipse.ecf.presence.IPresenceContainerAdapter;
import org.eclipse.ecf.presence.roster.IRoster;
import org.eclipse.ecf.presence.roster.IRosterEntry;
import org.eclipse.ecf.presence.roster.IRosterItem;
import org.eclipse.ecf.presence.roster.IRosterListener;
import org.eclipse.ecf.presence.roster.IRosterManager;
import org.eclipse.ecf.sync.IModelSynchronizationStrategy;
import org.eclipse.ecf.sync.SerializationException;
import org.eclipse.ecf.sync.doc.IDocumentSynchronizationStrategyFactory;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

public class StringShare extends AbstractShare {

    protected IModelSynchronizationStrategy syncStrategy;
    private IDocumentSynchronizationStrategyFactory factory;
    private Object stateLock = new Object();
    private IRosterManager rosterManager;
    
    IRosterListener rosterListener = new IRosterListener() {
        public void handleRosterEntryAdd(IRosterEntry entry) {
            // nothing to do
        }

        public void handleRosterEntryRemove(IRosterEntry entry) {
            // nothing to do
        }

        public void handleRosterUpdate(IRoster roster, IRosterItem changedValue) {
//            XXX: Implement
//            if (changedValue instanceof IRosterEntry) {
//                ID changedID = ((IRosterEntry) changedValue).getUser().getID();
//                ID oID = null;
//                ID otherID = null;
//                Shell shell = null;
//                synchronized (stateLock) {
//                    oID = getOurID();
//                    otherID = getOtherID();
//                    IWorkbenchPartSite wps = getTextEditor().getSite();
//                    shell = wps.getShell();
//                }
//                if (oID != null && changedID.equals(oID)) {
//                    localStopShare();
//                    showStopShareMessage(shell, Messages.DocShare_STOP_SHARED_EDITOR_US);
//                } else if (otherID != null && changedID.equals(otherID)) {
//                    localStopShare();
//                    showStopShareMessage(shell, Messages.DocShare_STOP_SHARED_EDITOR_REMOTE);
//                }
//            }
        }
    };
    
    private ID ourID;
    private ID remoteID;
    private String remoteUsername;
    private String ourUsername;    


    public StringShare(IChannelContainerAdapter adapter) throws ECFException {
        super(adapter);
        factory = ECFTestPlugin.getDefault().getColaSynchronizationStrategyFactory();
    }

    @Override
    protected void handleMessage(ID fromContainerID, byte[] data) {
            SelectionMessage message;
            try {
                message = SelectionMessage.deserialize(data);
                Assert.isNotNull(message);
                System.out.println("Recieved message from: " + message.fromUsername);
                if (! isSharing() ) {
                    handleStart(message);
                }
                // XXX: _NOW do stuff here
//            logError("could not handle message.", e);
            } catch (SerializationException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
    }

    private void handleStart(SelectionMessage message) {
        synchronized (stateLock) {
            remoteID = message.senderID;
            Assert.isNotNull(remoteID);
            remoteUsername = message.fromUsername;
            Assert.isNotNull(remoteUsername);

            //SYNC API. Create an instance of the synchronization strategy on the receiver
            syncStrategy = createSynchronizationStrategy(false);
            Assert.isNotNull(syncStrategy);
        }
        // needs to run in an UI Thread to have access to the workbench.
        Display.getDefault().syncExec(new Runnable(){
            @Override
            public void run() {
                IWorkbenchPage page = ECFTestPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage();
                page.addPartListener(new EditorPartListener(StringShare.this));
            }
        });
    }

    public boolean isSharing() {
        synchronized (stateLock) {
            return this.remoteID != null;
        }
    }

    public void startShare(final ID our, String fromName, final ID toID) {
        Trace.entering(ECFTestPlugin.PLUGIN_ID, StringShareDebugOptions.METHODS_ENTERING, StringShare.class, "startShare", new Object[] {our, fromName, toID}); //$NON-NLS-1$
        Assert.isNotNull(our);
        ourID = our;
        final String fName = (fromName == null) ? our.getName() : fromName;
        ourUsername = fName;
        Assert.isNotNull(toID);
        Assert.isNotNull(fName);
        Display.getDefault().syncExec(new Runnable() {
            public void run() {
                try {
                    // SYNC API.  Create the synchronization strategy instance
                    syncStrategy = createSynchronizationStrategy(true);
                    Assert.isNotNull(syncStrategy);

                    // Get content from local document
//                    String content = "huhuu";
                    
                    // Send start message with current content
                    sendMessage(toID, new SelectionMessage(our, fName, toID, null).serialize());
                    // Set local sharing start (to setup doc listener)
                    localStartShare(getLocalRosterManager(), our, our, toID);
                } catch (final Exception e) {
                    logError("Could not initiate sharing.", e);
                    showErrorToUser("Could not initiate sharing.", NLS.bind("Could not initiate sharing.", e.getLocalizedMessage()));
                }
            }

        });
        Trace.exiting(ECFTestPlugin.PLUGIN_ID, StringShareDebugOptions.METHODS_ENTERING, StringShare.class, "startShare"); //$NON-NLS-1$
        
    }
    
    private void showErrorToUser(String title, String message) {
        MessageDialog.openError(null, title, message);
    }

    private void logError(String string, Exception e) {
        // TODO Auto-generated method stub
        
    }

    private void localStartShare(IRosterManager localRosterManager, ID our, ID initiator, ID receiver) {
        synchronized (stateLock ) {
//            localStopShare();
            this.rosterManager = localRosterManager;
            if (this.rosterManager != null) {
                this.rosterManager.addRosterListener(rosterListener);
            }
            this.ourID = our;
            this.remoteID = receiver;
        }
    }

    private IRosterManager getLocalRosterManager() {
        IContainer container = (IContainer) this.adapter.getAdapter(IContainer.class);
        if (container != null) {
            IPresenceContainerAdapter presenceContainerAdapter = (IPresenceContainerAdapter) container.getAdapter(IPresenceContainerAdapter.class);
            if (presenceContainerAdapter != null) {
                return presenceContainerAdapter.getRosterManager();
            }
        }
        return null;
    }    

    IModelSynchronizationStrategy createSynchronizationStrategy(boolean isInitiator) {
        //Instantiate the service
        Assert.isNotNull(factory);
        return factory.createDocumentSynchronizationStrategy(getChannel().getID(), isInitiator);
    }

    public void selectionChanged(Collection<String> selection) {
        updateSelection(selection);
    }

    private void updateSelection(Collection<String> selection) {
        try {
            sendMessage(remoteID, new SelectionMessage(ourID, ourUsername, remoteID, selection).serialize());
        } catch (SerializationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ECFException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }    

}
