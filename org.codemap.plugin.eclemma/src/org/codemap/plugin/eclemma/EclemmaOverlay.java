package org.codemap.plugin.eclemma;

import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import ch.unibe.softwaremap.IMeanderPlugin;
import ch.unibe.softwaremap.SoftwareMap;
import ch.unibe.softwaremap.ui.MapView;

import com.mountainminds.eclemma.core.CoverageTools;

/**
 * The activator class controls the plug-in life cycle
 */
public class EclemmaOverlay extends AbstractUIPlugin implements IStartup, IMeanderPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.codemap.plugin.eclemma";

	// The shared instance
	private static EclemmaOverlay plugin;

	public final static ShowCoverageAction showCoverageAction = new ShowCoverageAction();
	
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
		System.out.println("starting eclemma overlay plugin");
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static EclemmaOverlay getDefault() {
		return plugin;
	}

	@Override
	public void earlyStartup() {
		plugin = this;
		// TODO: define extension point(s) in ch.unibe.softwaremap and load this lazily
		System.out.println("starting up ...");
		CoverageTools.addJavaCoverageListener(new MeanderCoverageListener());
		
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				SoftwareMap.core().register(EclemmaOverlay.this);
				MapView mapView = SoftwareMap.core().getMapView();
				IToolBarManager tbm = mapView.getToolBarManager();
				tbm.add(showCoverageAction);
				mapView.getViewSite().getActionBars().updateActionBars();
				tbm.markDirty();
			}
		});
	}

}
