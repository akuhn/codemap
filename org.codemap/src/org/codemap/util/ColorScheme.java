package org.codemap.util;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;

public abstract class ColorScheme {

    public static ColorScheme colors() {
        return new ColoredColorScheme();
    }
    
    public static ColorScheme blackWhite() {
        return new BlackWhiteColorScheme();
    }

    public abstract MColor getWaterColor();

    public abstract MColor getShoreColor();

    public abstract MColor getHillColor();

    public abstract double getDarkenFactor();

    public abstract void renderLabel(GC gc, String text, Rectangle bounds);

    public abstract double getLabelHeightFactor();
    
}
