package org.codemap.util;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Device;
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

    public abstract MColor getArrowColor();
}

class BlackWhiteColorScheme extends ColorScheme {

    @Override
    public MColor getWaterColor() {
        return MColor.WHITE;
    }

    @Override
    public MColor getShoreColor() {
        return MColor.GRAY_SHORE;
    }

    @Override
    public MColor getHillColor() {
        return MColor.GRAY_HILL;
    }

    @Override
    public double getDarkenFactor() {
        return .8;
    }

    @Override
    public void renderLabel(GC gc, String text, Rectangle bounds) {
        gc.setForeground(gc.getDevice().getSystemColor(SWT.COLOR_BLACK));
        gc.drawText(text, bounds.x, bounds.y, SWT.DRAW_TRANSPARENT);
    }

    @Override
    public double getLabelHeightFactor() {
        return 3.0;
    }

    @Override
    public MColor getArrowColor() {
        return MColor.BLACK;
    }
}

class ColoredColorScheme extends ColorScheme {

    @Override
    public MColor getWaterColor() {
        return MColor.WATER;
    }

    @Override
    public MColor getShoreColor() {
        return MColor.SHORE;
    }

    @Override
    public MColor getHillColor() {
        return MColor.HILLGREEN;
    }

    @Override
    public double getDarkenFactor() {
        return .5;
    }

    @Override
    public void renderLabel(GC gc, String text, Rectangle bounds) {
        Device device = gc.getDevice();
        gc.setAlpha(128);
        gc.setForeground(device.getSystemColor(SWT.COLOR_BLACK));
        gc.drawText(text, bounds.x + 1, bounds.y + 1, SWT.DRAW_TRANSPARENT);
        gc.setAlpha(255);
        
        gc.setForeground(device.getSystemColor(SWT.COLOR_WHITE));
        gc.drawText(text, bounds.x, bounds.y, SWT.DRAW_TRANSPARENT);
    }

    @Override
    public double getLabelHeightFactor() {
        return 2.0;
    }

    @Override
    public MColor getArrowColor() {
        return MColor.WHITE;
    }
}
