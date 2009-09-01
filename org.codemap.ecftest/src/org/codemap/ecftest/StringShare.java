package org.codemap.ecftest;

import java.util.Collection;

import org.codemap.ecftest.messages.Message;
import org.codemap.ecftest.messages.SelectionMessage;
import org.codemap.ecftest.messages.StartMessage;
import org.codemap.ecftest.messages.StopMessage;
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
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPage;

public class StringShare extends AbstractShare {

    IRosterListener rosterListener = new IRosterListener() {
        public void handleRosterEntryAdd(IRosterEntry entry) {
            // nothing to do
        }

        public void handleRosterEntryRemove(IRosterEntry entry) {
            // nothing to do
        }

        public void handleRosterUpdate(IRoster roster, IRosterItem changedValue) {
            // XXX: Implement
            // if (changedValue instanceof IRosterEntry) {
            // ID changedID = ((IRosterEntry) changedValue).getUser().getID();
            // ID oID = null;
            // ID otherID = null;
            // Shell shell = null;
            // synchronized (stateLock) {
            // oID = getOurID();
            // otherID = getOtherID();
            // IWorkbenchPartSite wps = getTextEditor().getSite();
            // shell = wps.getShell();
            // }
            // if (oID != null && changedID.equals(oID)) {
            // localStopShare();
            // showStopShareMessage(shell,
            // Messages.DocShare_STOP_SHARED_EDITOR_US);
            // } else if (otherID != null && changedID.equals(otherID)) {
            // localStopShare();
            // showStopShareMessage(shell,
            // Messages.DocShare_STOP_SHARED_EDITOR_REMOTE);
            // }
            // }
        }
    };
    
    private IModelSynchronizationStrategy syncStrategy;
    private IDocumentSynchronizationStrategyFactory factory;
    private IRosterManager rosterManager;

    private ID ourID;
    private ID remoteID;

    private EditorPartListener listener;    

    public StringShare(IChannelContainerAdapter adapter) throws ECFException {
        super(adapter);
        factory = ECFTestPlugin.getDefault().getColaSynchronizationStrategyFactory();
    }

    @Override
    protected void handleMessage(ID fromContainerID, byte[] data) {
        Message message;
        try {
            message = Message.deserialize(data);
            Assert.isNotNull(message);
            message.applyOn(this);
            // XXX: _NOW do stuff here
        } catch (SerializationException e) {
            Log.error(e);
        }
    }

    /**
     * Called when we are on the remote side.
     */
    public synchronized void handleRemoteStart(Message message) {
        remoteID = message.senderID;
        Assert.isNotNull(remoteID);
        ourID = message.receiverID;
        Assert.isNotNull(ourID);

        // SYNC API. Create an instance of the synchronization strategy on
        // the receiver
        syncStrategy = createSynchronizationStrategy(false);
        Assert.isNotNull(syncStrategy);
        addEditorListener();
    }

    /**
     * Add a listener that notifies us when the files are opened/closed in an
     * editor.
     */
    private void addEditorListener() {
        // needs to run in an UI Thread to have access to the workbench.
        if (listener == null) {
            listener = new EditorPartListener(StringShare.this);
        }
        
        Display.getDefault().syncExec(new Runnable() {
            @Override
            public void run() {
                // needs to run in an UI Thread to have access to the workbench.            	
                IWorkbenchPage page = ECFTestPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage();
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

    private synchronized void localStartShare(IRosterManager localRosterManager) {
        // localStopShare();
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

    IModelSynchronizationStrategy createSynchronizationStrategy(boolean isInitiator) {
        // Instantiate the service
        Assert.isNotNull(factory);
        return factory.createDocumentSynchronizationStrategy(getChannel().getID(), isInitiator);
    }

    public void selectionChanged(Collection<String> selection) {
        sendMessageToRemote(new SelectionMessage(ourID, remoteID, selection));
    }

    public String getRemoteName() {
        if (remoteID == null)
            return "";
        return remoteID.getName();
    }

    public void stopShare() {
        sendMessageToRemote(new StopMessage(remoteID, ourID));
        localStopShare();
    }

    private synchronized void localStopShare() {
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
        ourID = null;
        remoteID = null;
        syncStrategy = null;
        removeEditorListener();
    }

    private void removeEditorListener() {
        Display.getDefault().syncExec(new Runnable() {
            @Override
            public void run() {
                IWorkbenchPage page = ECFTestPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage();
                page.removePartListener(listener);
            }
        });
    }
}
