package com.example.lawofdemeter;

import org.eclipse.jdt.core.ElementChangedEvent;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class LawOfDemeter extends AbstractUIPlugin implements IStartup {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.example.lawofdemeter";

	// The shared instance
	private static LawOfDemeter plugin;
	
	/**
	 * The constructor
	 */
	public LawOfDemeter() {
	}

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static LawOfDemeter getDefault() {
		return plugin;
	}

	@Override
	public void earlyStartup() {
		JavaCore.addElementChangedListener(
				new MyElementChangedListener(), 
				ElementChangedEvent.POST_CHANGE);
	}

}
