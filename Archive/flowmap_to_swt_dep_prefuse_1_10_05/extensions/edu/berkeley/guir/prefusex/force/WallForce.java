package edu.berkeley.guir.prefusex.force;

import java.awt.geom.Line2D;

/**
 * Uses a gravitational force model to act as a "wall". Can be used to
 * construct line segments which either attract or repel items.
 *
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public class WallForce extends AbstractForce {

    private static String[] pnames = new String[] { "GravitationalConstant" };
    
    public static final float DEFAULT_GRAV_CONSTANT = -0.1f;
    public static final int GRAVITATIONAL_CONST = 0;
    
    private float x1, y1, x2, y2;
    private float dx, dy;
    
    public WallForce(float gravConst, 
        float x1, float y1, float x2, float y2) 
    {
        params = new float[] { gravConst };
        this.x1 = x1; this.y1 = y1;
        this.x2 = x2; this.y2 = y2;
        dx = x2-x1;
        dy = y2-y1;
        float r = (float)Math.sqrt(dx*dx+dy*dy);
        if ( dx != 0.0 ) dx /= r;
        if ( dy != 0.0 ) dy /= r;
    } //
    
    public WallForce(float x1, float y1, float x2, float y2) {
        this(DEFAULT_GRAV_CONSTANT,x1,y1,x2,y2);
    } //
    
    public boolean isItemForce() {
        return true;
    } //
    
    protected String[] getParameterNames() {
        return pnames;
    } //
    
    /* (non-Javadoc)
     * @see edu.berkeley.guir.prefusex.force.Force#getForce(edu.berkeley.guir.prefusex.force.ForceItem)
     */
    public void getForce(ForceItem item) {
        float[] n = item.location;
        int ccw = Line2D.relativeCCW(x1,y1,x2,y2,n[0],n[1]);
        float r = (float)Line2D.ptSegDist(x1,y1,x2,y2,n[0],n[1]);
        if ( r == 0.0 ) r = (float)Math.random() / 100.0f;
        float v = params[GRAVITATIONAL_CONST]*item.mass / (r*r*r);
        if ( n[0] >= Math.min(x1,x2) && n[0] <= Math.max(x1,x2) )
            item.force[1] += ccw*v*dx;
        if ( n[1] >= Math.min(y1,y2) && n[1] <= Math.max(y1,y2) )
            item.force[0] += -1*ccw*v*dy;
    } //

} // end of class WallForce
