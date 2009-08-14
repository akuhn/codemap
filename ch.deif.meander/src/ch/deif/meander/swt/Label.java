/**
 * 
 */
package ch.deif.meander.swt;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;


public class Label implements  Comparable<Label> {

    public final int fontHeight;
    public final Rectangle bounds;
    private final int px, py;
    public final String text;

    public Label(int px, int py, Point extent, int height, String text) {
        this.px = px;
        this.py = py;
        this.bounds = new Rectangle(px, py, extent.x, extent.y);
        this.fontHeight = height;
        this.text = text;
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

}