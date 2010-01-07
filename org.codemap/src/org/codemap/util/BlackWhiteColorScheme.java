package org.codemap.util;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;

public class BlackWhiteColorScheme extends ColorScheme {

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

}
