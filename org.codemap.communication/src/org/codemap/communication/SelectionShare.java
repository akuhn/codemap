package org.codemap.communication;

import java.util.Collection;

import org.codemap.communication.messages.Message;
import org.codemap.communication.messages.SelectionMessage;
import org.codemap.communication.messages.StartMessage;
import org.codemap.communication.messages.StopMessage;
import org.codemap.util.Log;
import org.eclipse.core.runtime.Assert;
import org.eclipse.ecf.core.IContainer;
import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.util.ECFException;
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
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPage;

import ch.deif.meander.MapSelection;

public class SelectionShare extends AbstractShare {
    
    IRosterListener rosterListener = new IRosterListener() {
        public void handleRosterEntryAdd(IRosterEntry entry) {
            // nothing to do
        }

        public void handleRosterEntryRemove(IRosterEntry entry) {
            // nothing to do
        }
        
        /**
         * handle changes to the roster.
         * AFAIK: this might be a local or a remote disconnect. 
         */
        public void handleRosterUpdate(IRoster roster, IRosterItem changedValue) {
             if (!(changedValue instanceof IRosterEntry)) return;
             
             IRosterEntry rosterEntry = (IRosterEntry) changedValue;
             System.out.println(rosterEntry);
             ID changedID = rosterEntry.getUser().getID();
             String message = null;
             
             synchronized (SelectionShare.this) {
                if (changedID.equals(ourID)) {
                    onLocalAbort();
                    message = "Sharing aborted locally.";
                }
                else if (changedID.equals(remoteID)) {
                    onRemoteAbort();
                    message = "Sharing aborted remotely.";
                }
            }
            
            if (message != null) showStopShareMessage(message);                
        }

        private void showStopShareMessage(final String message) {
            final Display display = Display.getDefault();
            display.asyncExec(new Runnable() {
                public void run() {
                    MessageDialog.openInformation(display.getActiveShell(), "Stopped sharing.", message);
                }
            });               
            
        }

        private void onRemoteAbort() {
            localStopShare();
        }

        private void onLocalAbort() {
            localStopShare();
        }
    };
    
    private IModelSynchronizationStrategy syncStrategy;
    private IDocumentSynchronizationStrategyFactory factory;
    private IRosterManager rosterManager;

    private ID ourID;
    private ID remoteID;

    private EditorPartListener listener;    

    public SelectionShare(IChannelContainerAdapter adapter) throws ECFException {
        super(adapter);
        factory = ECFPlugin.getDefault().getColaSynchronizationStrategyFactory();
    }

    @Override
    protected void handleMessage(ID fromContainerID, byte[] data) {
        Message message;
        try {
            message = Message.deserialize(data);
            Assert.isNotNull(message);
            message.applyOn(this);
        } catch (SerializationException e) {
            Log.error(e);
        }
    }

    protected boolean openReceiverDialog(ID sender) {
        return MessageDialog.openQuestion(null, "Share your Selection.", "Do you want to share your Codemap selection with " + sender.getName());
    }    

    /**
     * Called when we are on the remote side.
     */
    public synchronized void handleStartRequest(Message message) {
        remoteID = message.senderID;
        Assert.isNotNull(remoteID);
        ourID = message.receiverID;
        Assert.isNotNull(ourID);
        
        if (openReceiverDialog(message.senderID)) {
            // SYNC API. Create an instance of the synchronization strategy on
            // the receiver
            syncStrategy = createSynchronizationStrategy(false);
            Assert.isNotNull(syncStrategy);
            addEditorListener();            
        } else {
            sendStopMessage();
            localStopShare();
        }        
    }

    /**
     * Add a listener that notifies us when the files are opened/closed in an
     * editor.
     */
    private void addEditorListener() {
        // needs to run in an UI Thread to have access to the workbench.
        if (listener == null) {
            listener = new EditorPartListener(SelectionShare.this);
        }
        
        Display.getDefault().asyncExec(new Runnable() {
            @Override
            public void run() {
                // needs to run in an UI Thread to have access to the workbench.            	
                IWorkbenchPage page = ECFPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage();
                page.addPartListener(listener);
            }
        });
    }

    public synchronized boolean isSharing() {
        return this.remoteID != null;
    }

    public synchronized void startShare(final ID our, final ID remote) {
        Assert.isNotNull(our);
        ourID = our;

        Assert.isNotNull(remote);
        remoteID = remote;

        syncStrategy = createSynchronizationStrategy(true);
        Assert.isNotNull(syncStrategy);
        
        sendMessageToRemote(new StartMessage(ourID, remoteID));
        localStartShare(getLocalRosterManager());
    }

    private void localStartShare(IRosterManager localRosterManager) {
        this.rosterManager = localRosterManager;
        if (this.rosterManager != null) {
            this.rosterManager.addRosterListener(rosterListener);
        }
        addEditorListener();
    }

    private IRosterManager getLocalRosterManager() {
        IContainer container = (IContainer) this.adapter.getAdapter(IContainer.class);
        if (container != null) {
            IPresenceContainerAdapter presenceContainerAdapter = (IPresenceContainerAdapter) container
                    .getAdapter(IPresenceContainerAdapter.class);
            if (presenceContainerAdapter != null) {
                return presenceContainerAdapter.getRosterManager();
            }
        }
        return null;
    }

    private IModelSynchronizationStrategy createSynchronizationStrategy(boolean isInitiator) {
        // Instantiate the service
        Assert.isNotNull(factory);
        return factory.createDocumentSynchronizationStrategy(getChannel().getID(), isInitiator);
    }

    public synchronized void onLocalSelectionChanged(Collection<String> selection) {
        sendMessageToRemote(new SelectionMessage(ourID, remoteID, selection));
    }

    public synchronized String getRemoteName() {
        if (remoteID == null)
            return "";
        return remoteID.getName();
    }

    public synchronized void stopShare() {
        sendStopMessage();
        localStopShare();
    }

    private void sendStopMessage() {
        sendMessageToRemote(new StopMessage(remoteID, ourID));
    }

    private void localStopShare() {
        getSelection().clear();
        ourID = null;
        remoteID = null;
        syncStrategy = null;    
        
    }

    private void sendMessageToRemote(Message message) {
        try {
            sendMessage(remoteID, message.serialize());
        } catch (SerializationException e) {
            Log.error(e);
        } catch (ECFException e) {
            Log.error(e);
        }
    }

    public synchronized void handleRemoteStop(StopMessage stopMessage) {
        getSelection().clear();
        ourID = null;
        remoteID = null;
        syncStrategy = null;
        removeEditorListener();
    }

    private void removeEditorListener() {
    	if (listener == null) return;
        Display.getDefault().syncExec(new Runnable() {
            @Override
            public void run() {
                IWorkbenchPage page = ECFPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage();
                page.removePartListener(listener);
            }
        });
    }

    public synchronized void handleRemoteSelection(Collection<String> selection) {
        getSelection().replaceAll(selection);
    }

    private MapSelection getSelection() {
        return ECFPlugin.getDefault().getCommunicationSelection();
    }
}
