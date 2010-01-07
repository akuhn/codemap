package org.codemap.util;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;


public class ColoredColorScheme extends ColorScheme {

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
