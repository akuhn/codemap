package org.codemap.ecftest;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.ecf.core.IContainerManager;
import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.util.ECFException;
import org.eclipse.ecf.datashare.IChannelContainerAdapter;
import org.eclipse.ecf.sync.doc.IDocumentSynchronizationStrategyFactory;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

/**
 * The activator class controls the plug-in life cycle
 */
public class ECFTestPlugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.codemap.ecftest";

	// The shared instance
	private static ECFTestPlugin plugin;
	
	private Map<ID, StringShare> shares = new HashMap<ID, StringShare>();

    private ServiceTracker containerManagerTracker;
    private ServiceTracker syncStrategyFactoryServiceTracker;
    private BundleContext context;

	
	/**
	 * The constructor
	 */
	public ECFTestPlugin() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		super.start(bundleContext);
		plugin = this;
		context = bundleContext;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		plugin = null;
		context = null;
		//FIXME: dispose the containerManagerTracker et cetera ...
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static ECFTestPlugin getDefault() {
		return plugin;
	}
	
	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

    public StringShare getStringShare(ID containerID) {
        return shares.get(containerID);
    }
    
    public StringShare addStringShare(ID id, IChannelContainerAdapter channelAdapter) throws ECFException {
        StringShare share = shares.get(id);
        if (share == null) {
            share = new StringShare(channelAdapter);
            shares.put(id, share);
        }
        return share;
    }

    public IContainerManager getContainerManager() {
        if (containerManagerTracker == null) {
            containerManagerTracker = new ServiceTracker(context, IContainerManager.class.getName(), null);
            containerManagerTracker.open();
        }
        return (IContainerManager) containerManagerTracker.getService();
    }

    public StringShare removeStringShare(ID containerID) {
        return shares.remove(containerID);
    }

    public IDocumentSynchronizationStrategyFactory getColaSynchronizationStrategyFactory() {
        if (syncStrategyFactoryServiceTracker == null) {
            syncStrategyFactoryServiceTracker = new ServiceTracker(context, IDocumentSynchronizationStrategyFactory.class.getName(), null);
            syncStrategyFactoryServiceTracker.open();
        }
        return (IDocumentSynchronizationStrategyFactory) syncStrategyFactoryServiceTracker.getService();
    }
	
}
