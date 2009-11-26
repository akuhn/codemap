package edu.berkeley.guir.prefuse.util;

import java.awt.Font;
import java.util.HashMap;

/**
 * Maintains a cache of fonts and other useful font computation
 * routines.
 *
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public class FontLib {

    private static final HashMap fontMap = new HashMap();
    private static int misses = 0;
    private static int lookups = 0;
    
    public static Font getFont(String name, int style, int size) {
        Integer key = new Integer((name.hashCode()<<8)+(size<<2)+style);
        Font f = null;
        if ( (f=(Font)fontMap.get(key)) == null ) {
            f = new Font(name, style, size);
            fontMap.put(key, f);
            misses++;
        }
        lookups++;
        return f;
    } //
    
    public static int getCacheMissCount() {
        return misses;
    } //
    
    public static int getCacheLookupCount() {
        return lookups;
    } //
    
    public static void clearCache() {
        fontMap.clear();
    } //
    
    public static Font getIntermediateFont(Font f1, Font f2, double frac) {
        String name;
        int size, style;
        if ( frac < 0.5 ) {
            name  = f1.getName();
            style = f1.getStyle();
        } else {
            name  = f2.getName();
            style = f2.getStyle();
        }
        size = (int)Math.round(frac*f2.getSize()+(1-frac)*f1.getSize());
        return getFont(name,style,size);
    } //
    
} // end of class FontLib
