package org.codemap.communication;

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

public class CodemapECFStart implements IECFStart {
    
    
    private final IContainerListener containerListener = new IContainerListener() {

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.ecf.core.IContainerListener#handleEvent(org.eclipse.ecf.core.events.IContainerEvent)
         */
        public void handleEvent(IContainerEvent event) {
            IContainerManager containerManager = ECFTestPlugin.getDefault().getContainerManager();
            if (containerManager == null) return;
            IContainer container = containerManager.getContainer(event.getLocalContainerID());
            if (container == null) return;
            
            if (! handledConnectionEvent(event, container)) {
                handledDisposeEvent(event, container, containerManager);
            }
        }

        private boolean handledDisposeEvent(IContainerEvent event, IContainer container, IContainerManager containerManager) {
            if (! (event instanceof IContainerDisposeEvent)) return false;
            
            containerManager.removeListener(containerManagerListener);
            container.removeListener(containerListener);
            return true;
        }

        private boolean handledConnectionEvent(IContainerEvent event, IContainer container) {
            if (! (event instanceof IContainerConnectedEvent || event instanceof IContainerDisconnectedEvent)) return false;
            
            IChannelContainerAdapter containerAdapter = (IChannelContainerAdapter) container.getAdapter(IChannelContainerAdapter.class);
            if (containerAdapter == null) return true;
            ID containerID = container.getID();
            if (! handledConnectedEvent(event, container, containerID, containerAdapter)) {
                handledDisconnectedEvent(event, containerID);
            }
            return true;
        }

        private boolean handledDisconnectedEvent(IContainerEvent event, ID containerID) {
            if (!(event instanceof IContainerDisconnectedEvent || event instanceof IContainerEjectedEvent)) return false;
            
            StringShare share = ECFTestPlugin.getDefault().removeStringShare(containerID);
            if (share != null) {
                share.dispose();            
            }
            return true;
        }

        private boolean handledConnectedEvent(IContainerEvent event, IContainer container, ID containerID, IChannelContainerAdapter containerAdapter) {
            if (!(event instanceof IContainerConnectedEvent)) return false;
            
            try {
                ECFTestPlugin.getDefault().addStringShare(containerID, containerAdapter);
            } catch (ECFException e) {
                ECFTestPlugin.getDefault().getLog().log(new Status(IStatus.WARNING, ECFTestPlugin.PLUGIN_ID, IStatus.WARNING, NLS.bind("Document share not created.", container.getID()), null));
            }            
            return true;
        }
    };

    /**
     * Add a IContainerListener whenever a new container is added.
     * 
     * for more information on containers check out this website: {@link http://www.eclipse.org/ecf/documentation.php#Containers}
     * or @see IContainer.
     * 
     */
    private final IContainerManagerListener containerManagerListener = new IContainerManagerListener() {

        public void containerAdded(IContainer container) {
            IChannelContainerAdapter cca = (IChannelContainerAdapter) container.getAdapter(IChannelContainerAdapter.class);
            if (cca == null)
                return;
            container.addListener(containerListener);
        }

        public void containerRemoved(IContainer container) {
            container.removeListener(containerListener);
        }
    };
    
    /**
     * Called on ECF-startup, registers our IContainerManagerListener with the IContainerManager, hence we are able to
     * react to added/removed containers.
     */
    @Override
    public IStatus run(IProgressMonitor monitor) {
        System.out.println("starting ECF stuff for codemap ...");
        
        IContainerManager containerManager = ECFTestPlugin.getDefault().getContainerManager();
        if (containerManager == null)
            return new Status(IStatus.WARNING, ECFTestPlugin.PLUGIN_ID, IStatus.WARNING, "no container manager available", null);
        containerManager.addListener(containerManagerListener);
        return Status.OK_STATUS;        
    }
}
