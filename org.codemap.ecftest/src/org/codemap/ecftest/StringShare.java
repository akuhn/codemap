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
                if (! isSharing() ) {
                    handleRemoteStart(message);
                }
                // XXX: _NOW do stuff here
                System.out.println("recieved selection update from: " + message.fromUsername + " with selection: " + message.selection);
//            logError("could not handle message.", e);
            } catch (SerializationException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
    }
    
    /**
     * Called when we are on the remote side.
     */
    private void handleRemoteStart(SelectionMessage message) {
        synchronized (stateLock) {
            remoteID = message.senderID;
            Assert.isNotNull(remoteID);
            remoteUsername = message.fromUsername;
            Assert.isNotNull(remoteUsername);
            
            ourID = message.receiverID;

            //SYNC API. Create an instance of the synchronization strategy on the receiver
            syncStrategy = createSynchronizationStrategy(false);
            Assert.isNotNull(syncStrategy);
        }
        addEditorListener();
    }
    
    /**
     * Add a listener that notifies us when the files are opened/closed in an
     * editor.
     * 
     */
    private void addEditorListener() {
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

    public void startShare(final ID our, String fromName, final ID remote) {
        final String fName = (fromName == null) ? our.getName() : fromName;
        Assert.isNotNull(fName);
        ourUsername = fName;
        
        Assert.isNotNull(our);
        ourID = our;
        
        Assert.isNotNull(remote);
        remoteID = remote;
        
        Display.getDefault().syncExec(new Runnable() {
            public void run() {
                try {
                    // SYNC API.  Create the synchronization strategy instance
                    syncStrategy = createSynchronizationStrategy(true);
                    Assert.isNotNull(syncStrategy);

                    // Send start message with empty selection
                    sendMessage(remoteID, new SelectionMessage(ourID, ourUsername, remoteID, null).serialize());
                    // Set local sharing start
                    localStartShare(getLocalRosterManager());
                } catch (final Exception e) {
                    logError("Could not initiate sharing.", e);
                    showErrorToUser("Could not initiate sharing.", NLS.bind("Could not initiate sharing.", e.getLocalizedMessage()));
                }
            }
        });
    }
    
    private void showErrorToUser(String title, String message) {
        MessageDialog.openError(null, title, message);
    }

    private void logError(String string, Exception e) {
        System.out.println(string);
        e.printStackTrace();
    }

    private void localStartShare(IRosterManager localRosterManager) {
        synchronized (stateLock ) {
//            localStopShare();
            this.rosterManager = localRosterManager;
            if (this.rosterManager != null) {
                this.rosterManager.addRosterListener(rosterListener);
            }
        }
        addEditorListener();
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
