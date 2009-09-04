package org.codemap.util;

import org.codemap.CodemapCore;

public class CodemapIcons extends IconFactory {
    
    private static final String DIR_PREFIX = "icons/eclipse/";

    public static final String FILE = DIR_PREFIX + "file.gif";
    public static final String LINKED = DIR_PREFIX + "linked.gif";
    public static final String LAYERS = DIR_PREFIX + "layers.gif";  
    public static final String LABELS = DIR_PREFIX + "labels.gif";
    public static final String FORCE_SELECTION = DIR_PREFIX + "force_selection.gif";
    public static final String PACKAGES = DIR_PREFIX + "packages.gif";  
    public static final String PALETTE = DIR_PREFIX + "palette.gif";        
    public static final String GREEN_CIRCLE = DIR_PREFIX + "green_circle.gif";
    
    public static final String INFO = DIR_PREFIX + "info_obj.gif";
    public static final String WARNING = DIR_PREFIX + "warning_obj.gif";
    public static final String ERROR = DIR_PREFIX + "error_obj.gif";     

    @Override
    protected CodemapCore getActivator() {
        return CodemapCore.getPlugin();
    }

}
