package org.codemap.plugin.eclemma;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.mountainminds.eclemma.core.CoverageTools;

/**
 * The activator class controls the plug-in life cycle
 */
public class EclemmaOverlay extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.codemap.plugin.eclemma";

	// The shared instance
	private static EclemmaOverlay plugin;

	private ShowCoverageAction showCoverageAction;
	private MeanderCoverageListener coverageListener;
	
	/**
	 * The constructor
	 */
	public EclemmaOverlay() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		
		coverageListener = new MeanderCoverageListener();
		// call it on plug-in startup to load a coverage report if there is any.
//		coverageListener.coverageChanged();
		CoverageTools.addJavaCoverageListener(coverageListener);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		CoverageTools.removeJavaCoverageListener(coverageListener);
		
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static EclemmaOverlay getPlugin() {
		return plugin;
	}

	public void registerCoverageAction(ShowCoverageAction action) {
		showCoverageAction = action;
	}

	public ShowCoverageAction getCoverageAction() {
		return showCoverageAction;
	}
}
