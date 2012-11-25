package org.codemap.plugin.communication.util;

import org.codemap.plugin.communication.ECFPlugin;
import org.codemap.util.IconFactory;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;

public class ECFPluginIcons extends IconFactory {
    
    private static final String DIR_PREFIX = "icons/";
    public static final String MEEPLE = DIR_PREFIX + "meeple.gif"; 
    
    public static ImageDescriptor descriptor(String key) {
        return new ECFPluginIcons().getImageDescriptor(key);
    }

    @Override
    protected AbstractUIPlugin getActivator() {
        return ECFPlugin.getDefault();
    }

}
