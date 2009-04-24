package ch.unibe.softwaremap;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import ch.unibe.softwaremap.builder.HapaxNature;

/**
 * The activator class controls the plug-in life cycle
 */
public class SoftwareMapCore extends AbstractUIPlugin {

    public static final String PLUGIN_ID = SoftwareMapCore.class.getPackage().getName();
    private static SoftwareMapCore plugin;

    
    public SoftwareMapCore() {
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

    public static SoftwareMapCore getDefault() {
        return plugin;
    }

    public final static String makeID(Class<?> javaClass) {
        return PLUGIN_ID + "." + javaClass.getSimpleName();
    }
}
