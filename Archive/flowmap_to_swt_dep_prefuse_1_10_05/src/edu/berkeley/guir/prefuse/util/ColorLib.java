package edu.berkeley.guir.prefuse.util;

import java.awt.Color;
import java.util.HashMap;

/**
 * Maintains a cache of colors and other useful color computation
 * routines.
 *
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public class ColorLib {

    private static final HashMap colorMap = new HashMap();
    private static int misses = 0;
    private static int lookups = 0;
    
    public static Color getColor(float r, float g, float b, float a) {
        int rgba =
           ((((int)(a*255+0.5)) & 0xFF) << 24) |
           ((((int)(r*255+0.5)) & 0xFF) << 16) | 
           ((((int)(g*255+0.5)) & 0xFF) <<  8) |
           (((int)(b*255+0.5)) & 0xFF);
        return getColor(rgba);
    } //
    
    public static Color getColor(float r, float g, float b) {
        return getColor(r,g,b,1.0f);
    } //
    
    public static Color getColor(int r, int g, int b, int a) {
        int rgba = ((a & 0xFF) << 24) | ((r & 0xFF) << 16) |
                   ((g & 0xFF) << 8)  | ((b & 0xFF) << 0);
        return getColor(rgba);
    } //
    
    public static Color getColor(int r, int g, int b) {
        return getColor(r,g,b,255);
    } //
    
    public static Color getColor(int rgba) {
        Integer key = new Integer(rgba);
        Color c = null;
        if ( (c=(Color)colorMap.get(key)) == null ) {
            c = new Color(rgba);
            colorMap.put(key,c);
            misses++;
        }
        lookups++;
        return c;
    } //
    
    public static int getCacheMissCount() {
        return misses;
    } //
    
    public static int getCacheLookupCount() {
        return lookups;
    } //
    
    public static void clearCache() {
        colorMap.clear();
    } //
    
    public static Color getIntermediateColor(Color c1, Color c2, double frac) {
        return getColor(
                (int)Math.round(frac*c2.getRed()   + (1-frac)*c1.getRed()),
                (int)Math.round(frac*c2.getGreen() + (1-frac)*c1.getGreen()),
                (int)Math.round(frac*c2.getBlue()  + (1-frac)*c1.getBlue()),
                (int)Math.round(frac*c2.getAlpha() + (1-frac)*c1.getAlpha()));
    } //
    
} // end of class ColorLib
