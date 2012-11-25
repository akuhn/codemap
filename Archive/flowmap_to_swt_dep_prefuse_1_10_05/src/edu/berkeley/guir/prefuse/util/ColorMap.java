package edu.berkeley.guir.prefuse.util;

import java.awt.Color;
import java.awt.Paint;

/**
 * A color map provides a mapping from numeric values to specific colors.
 * This useful for assigning colors to visualized items. The numeric values
 * may represent different categories (i.e. nominal variables) or run along
 * a spectrum of values (i.e. quantitative variables).
 *
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public class ColorMap {

    /**
     * The default length of a color map array if its size
     * is not otherwise specified.
     */
    public static final int DEFAULT_MAP_SIZE = 64;
    
    private Paint colorMap[];
    private double minValue, maxValue;
    
    /**
     * Creates a new ColorMap instance using the given internal color map
     * array and minimum and maximum index values.
     * @param map a Paint array constituing the color map
     * @param min the minimum value in the color map
     * @param max the maximum value in the color map
     */
    public ColorMap(Paint[] map, double min, double max) {
        colorMap = map;
        minValue = min;
        maxValue = max;
    } //
    
    /**
     * Returns the color associated with the given value. If the value
     * is outside the range defined by this map's minimum or maximum
     * values, a saturated value is returned (i.e. the first entry
     * in the color map for values below the minimum, the last enty
     * for value above the maximum).
     * @param val the value for which to retrieve the corresponding color
     * @return the color (as a Paint instance) corresponding the given value
     */
    public Paint getColor(double val) {
        if ( val < minValue ) {
            return colorMap[0];
        } else if ( val > maxValue ) {
            return colorMap[colorMap.length-1];
        } else {
            int idx = (int)Math.round((colorMap.length-1) *
                          (val-minValue)/(maxValue-minValue));
            return colorMap[idx];
        }
    } //

    /**
     * Sets the internal color map, an array of Paint values.
     * @return Returns the colormap.
     */
    public Paint[] getColorMap() {
        return colorMap;
    } //

    /**
     * Sets the internal color map, an array of Paint values.
     * @param colorMap The new colormap.
     */
    public void setColorMap(Paint[] colorMap) {
        this.colorMap = colorMap;
    } //

    /**
     * Gets the maximum value that corresponds to the last
     * color in the color map.
     * @return Returns the max index value into the colormap.
     */
    public double getMaxValue() {
        return maxValue;
    } //

    /**
     * Sets the maximum value that corresponds to the last
     * color in the color map.
     * @param maxValue The max index value to set.
     */
    public void setMaxValue(double maxValue) {
        this.maxValue = maxValue;
    } //

    /**
     * Sets the minimum value that corresponds to the first
     * color in the color map.
     * @return Returns the min index value.
     */
    public double getMinValue() {
        return minValue;
    } //

    /**
     * Gets the minimum value that corresponds to the first
     * color in the color map.
     * @param minValue The min index value to set.
     */
    public void setMinValue(double minValue) {
        this.minValue = minValue;
    } //

    /**
     * Returns a color map array of default size that ranges from black to
     * white through shades of gray.
     * @return the color map array
     */
    public static Paint[] getGrayscaleMap() {
        return getGrayscaleMap(DEFAULT_MAP_SIZE);
    } //
    
    /**
     * /**
     * Returns a color map array of specified size that ranges from black to
     * white through shades of gray.
     * @param size the size of the color map array
     * @return the color map array
     */
    public static Paint[] getGrayscaleMap(int size) {
        Paint[] cm = new Paint[size];
        for ( int i=0; i<size; i++ ) {
            float g = ((float)i)/(size-1);
            cm[i] = ColorLib.getColor(g,g,g,1.f);
        }
        return cm;
    } //
    
    /**
     * Returns a color map array of default size that ranges from one
     * given color to the other.
     * @param c1 the initial color in the color map
     * @param c2 the final color in the color map
     * @return the color map array
     */
    public static Paint[] getInterpolatedMap(Color c1, Color c2) {
        return getInterpolatedMap(DEFAULT_MAP_SIZE, c1, c2);
    } //
    
    /**
     * Returns a color map array of given size that ranges from one
     * given color to the other.
     * @param size the size of the color map array
     * @param c1 the initial color in the color map
     * @param c2 the final color in the color map
     * @return the color map array
     */
    public static Paint[] getInterpolatedMap(int size, Color c1, Color c2) {
        Paint[] cm = new Paint[size];
        for ( int i=0; i<size; i++ ) {
            float f = ((float)i)/(size-1);
            cm[i] = ColorLib.getIntermediateColor(c1,c2,f);
        }
        return cm;
    } //
    
    /**
     * Returns a color map array of default size that cycles through
     * the hues of the HSB (Hue/Saturation/Brightness) color space at
     * full saturation and brightness.
     * @return the color map array
     */
    public static Paint[] getHSBMap() {
        return getHSBMap(DEFAULT_MAP_SIZE, 1.f, 1.f);
    } //
    
    /**
     * Returns a color map array of given size that cycles through
     * the hues of the HSB (Hue/Saturation/Brightness) color space.
     * @param size the size of the color map array
     * @param s the saturation value to use
     * @param b the brightness value to use
     * @return the color map array
     */
    public static Paint[] getHSBMap(int size, float s, float b) {
        Paint[] cm = new Paint[size];
        for ( int i=0; i<size; i++ ) {
            float h = ((float)i)/(size-1);
            cm[i] = ColorLib.getColor(Color.HSBtoRGB(h,s,b));
        }
        return cm;
    } //
    
    /**
     * Returns a color map of default size that moves from black to
     * red to yellow to white.
     * @return the color map array
     */
    public static Paint[] getHotMap() {
        return getHotMap(DEFAULT_MAP_SIZE);
    } //
    
    /**
     * Returns a color map that moves from black to red to yellow
     * to white.
     * @param size the size of the color map array
     * @return the color map array
     */
    public static Paint[] getHotMap(int size) {
        Paint[] cm = new Paint[size];
        for ( int i=0; i<size; i++ ) {
            int n = (3*size)/8;
            float r = ( i<n ? ((float)(i+1))/n : 1.f );
            float g = ( i<n ? 0.f : ( i<2*n ? ((float)(i-n))/n : 1.f ));
            float b = ( i<2*n ? 0.f : ((float)(i-2*n))/(size-2*n) );
            cm[i] = ColorLib.getColor(r,g,b,1.0f);
        }
        return cm;
    } //
    
    /**
     * Returns a color map array of default size that uses a "cool",
     * blue-heavy color scheme.
     * @return the color map array
     */
    public static Paint[] getCoolMap() {
        return getCoolMap(DEFAULT_MAP_SIZE);
    } //
    
    /**
     * Returns a color map array that uses a "cool",
     * blue-heavy color scheme.
     * @param size the size of the color map array
     * @return the color map array
     */
    public static Paint[] getCoolMap(int size) {
        Paint[] cm = new Paint[size];
        for( int i=0; i<size; i++ ) {
            float r = ((float)i) / Math.max(size-1,1.f);
            cm[i] = ColorLib.getColor(r,1-r,1.f,1.f);
        }
        return cm;
    } //
    
} // end of class ColorMap
