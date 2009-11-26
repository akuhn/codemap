package edu.berkeley.guir.prefuse.util.display;

import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

/**
 * Represents a clipping rectangle in a prefuse <code>Display</code>.
 *
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public class Clip {
    
    private double[] clip = new double[8];
    private double[] tmp  = new double[8];
    
    public void setClip(double x1, double y1, double x2, double y2) {
        clip[0] = x1; clip[1] = y1;
        clip[2] = x1; clip[3] = y2;
        clip[4] = x2; clip[5] = y1;
        clip[6] = x2; clip[7] = y2;
    } //
    
    public void setClip(Clip c) {
        System.arraycopy(c.clip, 0, clip, 0, 4);
    } //
    
    public void setClip(Rectangle2D r) {
        clip[0] = r.getX(); clip[1] = r.getY();
        clip[6] = clip[0]+r.getWidth();
        clip[7] = clip[1]+r.getHeight();
        clip[2] = clip[0]; clip[3] = clip[7];
        clip[4] = clip[6]; clip[5] = clip[1];
    } //
    
    public void transform(AffineTransform at) {
        at.transform(clip,0,tmp,0,4);
        double[] s = tmp;
        tmp = clip;
        clip = s;
        // make safe against rotation
        double xmin = clip[0], ymin = clip[1];
        double xmax = clip[6], ymax = clip[7];
        for ( int i=0; i<7; i+=2 ) {
            if ( clip[i] < xmin )
                xmin = clip[i];
            if ( clip[i] > xmax )
                xmax = clip[i];
            if ( clip[i+1] < ymin )
                ymin = clip[i+1];
            if ( clip[i+1] > ymax )
                ymax = clip[i+1];
        }
        clip[0] = xmin; clip[1] = ymin;
        clip[2] = xmin; clip[3] = ymax;
        clip[4] = xmax; clip[5] = ymin;
        clip[6] = xmax; clip[7] = ymax;
    } //
    
    public void limit(double x, double y, double w, double h) {
        clip[0] = Math.max(clip[0],x);
        clip[1] = Math.max(clip[1],y);
        clip[6] = Math.min(clip[6],w);
        clip[7] = Math.min(clip[7],h);
        clip[2] = clip[0]; clip[3] = clip[7];
        clip[4] = clip[6]; clip[5] = clip[1];
    } //
    
    public boolean intersects(Rectangle2D r) {
        double tw = clip[6]-clip[0];
        double th = clip[7]-clip[1];
        double rw = r.getWidth();
        double rh = r.getHeight();
        if (rw < 0 || rh < 0 || tw < 0 || th < 0) {
            return false;
        }
        double tx = clip[0];
        double ty = clip[1];
        double rx = r.getX();
        double ry = r.getY();
        rw += rx;
        rh += ry;
        tw += tx;
        th += ty;
        //      overflow || intersect
        return ((rw < rx || rw > tx) &&
                (rh < ry || rh > ty) &&
                (tw < tx || tw > rx) &&
                (th < ty || th > ry));
    } //
    
    public void union(Clip c) {
        clip[0] = Math.min(clip[0], c.clip[0]);
        clip[1] = Math.min(clip[1], c.clip[1]);
        clip[2] = Math.max(clip[6], c.clip[6]);
        clip[3] = Math.max(clip[7], c.clip[7]);
    } //
    
    public void union(Rectangle2D r) {
        clip[0] = Math.min(clip[0], r.getX()-1);
        clip[1] = Math.min(clip[1], r.getY()-1);
        clip[2] = Math.max(clip[6], r.getX()+r.getWidth()+1);
        clip[3] = Math.max(clip[7], r.getX()+r.getHeight()+1);
    } //
    
    public double getX() {
        return clip[0];
    } //
    
    public double getY() {
        return clip[1];
    } //
    
    public double getWidth() {
        return clip[6]-clip[0];
    } //
    
    public double getHeight() {
        return clip[7]-clip[1];
    } //
    
} // end of inner class Clip
