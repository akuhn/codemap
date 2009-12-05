/**
 * 
 */
package org.codemap.layers;

import org.codemap.Location;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;


public class Label implements Comparable<Label> {

    public final int fontHeight;
    public final Rectangle bounds;
    private final int px, py;
    public final String text;
    private Location location;
    private boolean hasFocus;

    public Label(int px, int py, Point extent, int height, String text, Location loc) {
        this.px = px;
        this.py = py;
        this.bounds = new Rectangle(px, py, extent.x, extent.y);
        this.fontHeight = height;
        this.text = text;
        this.location = loc;
        location.setLabel(this);
    }

    public boolean intersects(Label each) {
        return this.bounds.intersects(each.bounds);
    }

    public void changeOrientation(double dx, double dy) {
        bounds.x = (int) (px + dx * bounds.width);
        bounds.y = (int) (py + dy * bounds.height);
    }

    @Override
    public int compareTo(Label each) {
        return each.getArea() - this.getArea();
    }

    private int getArea() {
        return bounds.height * bounds.width;
    }

    public void render(GC gc, FontData[] fontData) {
        int fontStyle = hasFocus ? SWT.BOLD : SWT.NORMAL;
        for (FontData fd: fontData) {
            fd.setHeight(fontHeight);
            fd.setStyle(fontStyle);
        }
        Font font = new Font(gc.getDevice(), fontData);
        gc.setFont(font);
        
        Device device = gc.getDevice();
        gc.setAlpha(128);
        gc.setForeground(device.getSystemColor(SWT.COLOR_BLACK));
        gc.drawText(text, bounds.x + 1, bounds.y + 1, SWT.DRAW_TRANSPARENT);
        gc.setAlpha(255);
        
        gc.setForeground(device.getSystemColor(SWT.COLOR_WHITE));
        gc.drawText(text, bounds.x, bounds.y, SWT.DRAW_TRANSPARENT);
        
        font.dispose();
    }

    public void setHasFocus(boolean b) {
        hasFocus = b;
    }

}