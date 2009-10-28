package org.codemap.util;

import org.codemap.CodemapCore;

public class CodemapIcons extends IconFactory {
    
    private static final String DIR_PREFIX = "icons/eclipse/";
    
    private static String makeIdentifier(String suffix) {
        return DIR_PREFIX + suffix;
    }

    public static final String FILE = makeIdentifier("file.gif");
    public static final String LINKED = makeIdentifier("linked.gif");
    public static final String LAYERS = makeIdentifier("layers.gif");  
    public static final String LABELS = makeIdentifier("labels.gif");
    public static final String FORCE_SELECTION = makeIdentifier("force_selection.gif");
    public static final String PACKAGES = makeIdentifier("packages.gif");  
    public static final String PALETTE = makeIdentifier("palette.gif");        
    public static final String GREEN_CIRCLE = makeIdentifier("green_circle.gif");
    public static final String FLOW = makeIdentifier("flow.gif");
    
    public static final String INFO = makeIdentifier("info_obj.gif");
    public static final String WARNING = makeIdentifier("warning_obj.gif");
    public static final String ERROR = makeIdentifier("error_obj.gif");     

    @Override
    protected CodemapCore getActivator() {
        return CodemapCore.getPlugin();
    }

}
