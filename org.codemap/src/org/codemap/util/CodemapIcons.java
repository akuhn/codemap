package org.codemap.util;

import org.codemap.CodemapCore;
import org.eclipse.jface.resource.ImageDescriptor;

public class CodemapIcons extends IconFactory {
    
    private static final String DIR_PREFIX = "icons/eclipse/";
    
    private static String makeIdentifier(String suffix) {
        return DIR_PREFIX + suffix;
    }
    
    public static ImageDescriptor descriptor(String key) {
        return new CodemapIcons().getImageDescriptor(key);
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
    public static final String CALL_HIERARCHY = makeIdentifier("call_hierarchy.gif");
    public static final String SEARCH = makeIdentifier("search.gif");
    public static final String MARKER = makeIdentifier("markers.gif");
    public static final String TRACE = makeIdentifier("trace.gif");     
    public static final String COVERAGE = makeIdentifier("coverage.gif");
    public static final String YOU_ARE_HERE = makeIdentifier("youarehere.gif");
    public static final String SELECTION = makeIdentifier("selection.gif");    
    
    public static final String INFO = makeIdentifier("info_obj.gif");
    public static final String WARNING = makeIdentifier("warning_obj.gif");
    public static final String ERROR = makeIdentifier("error_obj.gif");
    

    @Override
    protected CodemapCore getActivator() {
        return CodemapCore.getPlugin();
    }

}
