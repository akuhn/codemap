package org.codemap.plugin.communication.util;

import org.codemap.plugin.communication.ECFPlugin;
import org.codemap.util.IconFactory;
import org.eclipse.ui.plugin.AbstractUIPlugin;

public class ECFPluginIcons extends IconFactory {
    
    private static final String DIR_PREFIX = "icons/";
    public static final String MEEPLE = DIR_PREFIX + "meeple.gif";    

    @Override
    protected AbstractUIPlugin getActivator() {
        return ECFPlugin.getDefault();
    }

}
