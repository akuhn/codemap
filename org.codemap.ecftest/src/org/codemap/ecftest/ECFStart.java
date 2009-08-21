package org.codemap.ecftest;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ecf.core.IContainer;
import org.eclipse.ecf.core.IContainerListener;
import org.eclipse.ecf.core.IContainerManager;
import org.eclipse.ecf.core.IContainerManagerListener;
import org.eclipse.ecf.core.events.IContainerConnectedEvent;
import org.eclipse.ecf.core.events.IContainerDisconnectedEvent;
import org.eclipse.ecf.core.events.IContainerDisposeEvent;
import org.eclipse.ecf.core.events.IContainerEjectedEvent;
import org.eclipse.ecf.core.events.IContainerEvent;
import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.start.IECFStart;
import org.eclipse.ecf.core.util.ECFException;
import org.eclipse.ecf.datashare.IChannelContainerAdapter;
import org.eclipse.osgi.util.NLS;

public class ECFStart implements IECFStart {
    
    
    IContainerListener containerListener = new IContainerListener() {

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.ecf.core.IContainerListener#handleEvent(org.eclipse.ecf.core.events.IContainerEvent)
         */
        public void handleEvent(IContainerEvent event) {
            final IContainerManager containerManager = ECFTestPlugin.getDefault().getContainerManager();
            if (containerManager == null)
                return;
            IContainer container = containerManager.getContainer(event.getLocalContainerID());
            if (container == null)
                return;
            if (event instanceof IContainerConnectedEvent || event instanceof IContainerDisconnectedEvent) {
                // connected
                IChannelContainerAdapter cca = (IChannelContainerAdapter) container.getAdapter(IChannelContainerAdapter.class);
                if (cca == null)
                    return;
                ID containerID = container.getID();
                if (event instanceof IContainerConnectedEvent) {
                    try {
                        ECFTestPlugin.getDefault().addStringShare(containerID, cca);
                    } catch (ECFException e) {
                        ECFTestPlugin.getDefault().getLog().log(new Status(IStatus.WARNING, ECFTestPlugin.PLUGIN_ID, IStatus.WARNING, NLS.bind("Document share not created.", container.getID()), null));
                    }
                } else if (event instanceof IContainerDisconnectedEvent || event instanceof IContainerEjectedEvent) {
                    StringShare share = ECFTestPlugin.getDefault().removeStringShare(containerID);
                    if (share != null)
                        share.dispose();
                }
            } else if (event instanceof IContainerDisposeEvent) {
                containerManager.removeListener(containerManagerListener);
                container.removeListener(containerListener);
            }
        }

    };

    IContainerManagerListener containerManagerListener = new IContainerManagerListener() {

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.ecf.core.IContainerManagerListener#containerAdded(org.eclipse.ecf.core.IContainer)
         */
        public void containerAdded(IContainer container) {
            IChannelContainerAdapter cca = (IChannelContainerAdapter) container.getAdapter(IChannelContainerAdapter.class);
            if (cca == null)
                return;
            container.addListener(containerListener);
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.ecf.core.IContainerManagerListener#containerRemoved(org.eclipse.ecf.core.IContainer)
         */
        public void containerRemoved(IContainer container) {
            container.removeListener(containerListener);
        }
    };
    

    @Override
    public IStatus run(IProgressMonitor monitor) {
        System.out.println("starting ecf");
        
        IContainerManager containerManager = ECFTestPlugin.getDefault().getContainerManager();
        if (containerManager == null)
            return new Status(IStatus.WARNING, ECFTestPlugin.PLUGIN_ID, IStatus.WARNING, "no container manager available", null);
        containerManager.addListener(containerManagerListener);
        return Status.OK_STATUS;        
    }

}
