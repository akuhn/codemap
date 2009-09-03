package org.codemap.communication;

import java.util.HashMap;
import java.util.Map;

import org.codemap.CodemapCore;
import org.codemap.MapPerProject;
import org.eclipse.ecf.core.IContainerManager;
import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.util.ECFException;
import org.eclipse.ecf.datashare.IChannelContainerAdapter;
import org.eclipse.ecf.sync.doc.IDocumentSynchronizationStrategyFactory;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import ch.deif.meander.MapSelection;
import ch.deif.meander.swt.SWTLayer;

/**
 * The activator class controls the plug-in life cycle
 */
public class ECFPlugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.codemap.communication";

	// The shared instance
	private static ECFPlugin plugin;
	
	private Map<ID, SelectionShare> shares = new HashMap<ID, SelectionShare>();

    private ServiceTracker containerManagerTracker;
    private ServiceTracker syncStrategyFactoryServiceTracker;
    private BundleContext context;

    private SWTLayer openFilesLayer;
    private MapSelection communicationSelection;
	
	/**
	 * The constructor
	 */
	public ECFPlugin() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		super.start(bundleContext);
		
		communicationSelection = new MapSelection();
		openFilesLayer = new CommunicationOvleray(communicationSelection);
		plugin = this;
		context = bundleContext;
	}

    /*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
	    if (syncStrategyFactoryServiceTracker != null)
	        syncStrategyFactoryServiceTracker.close();
	    if (containerManagerTracker != null)
	        containerManagerTracker.close();
	    // "free"
	    syncStrategyFactoryServiceTracker = null;	    
	    containerManagerTracker = null;
	    communicationSelection = null;
	    shares = null;
	    openFilesLayer = null;
		plugin = null;
		context = null;
		
		super.stop(context);
	}

    /**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static ECFPlugin getDefault() {
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

    public SelectionShare getStringShare(ID containerID) {
        return shares.get(containerID);
    }
    
    public SelectionShare addStringShare(ID id, IChannelContainerAdapter channelAdapter) throws ECFException {
        SelectionShare share = shares.get(id);
        if (share == null) {
            share = new SelectionShare(channelAdapter);
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

    public SelectionShare removeStringShare(ID containerID) {
        return shares.remove(containerID);
    }

    public IDocumentSynchronizationStrategyFactory getColaSynchronizationStrategyFactory() {
        if (syncStrategyFactoryServiceTracker == null) {
            syncStrategyFactoryServiceTracker = new ServiceTracker(context, IDocumentSynchronizationStrategyFactory.class.getName(), null);
            syncStrategyFactoryServiceTracker.open();
        }
        return (IDocumentSynchronizationStrategyFactory) syncStrategyFactoryServiceTracker.getService();
    }

    public MapSelection getCommunicationSelection() {
        registerSelectionWithActiveMap();
        return communicationSelection;
    }

    private void registerSelectionWithActiveMap() {
        MapPerProject activeMap = CodemapCore.getPlugin().getActiveMap();
        // FIXME: find a way to queue incoming selections as long as there is no map selected
        if (activeMap == null) return;
        if (! activeMap.containsLayer(openFilesLayer)){
            activeMap.addSelectionLayer(openFilesLayer, communicationSelection);
        }
    }
}
