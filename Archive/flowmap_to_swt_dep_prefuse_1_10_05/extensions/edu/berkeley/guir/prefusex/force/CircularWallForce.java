package edu.berkeley.guir.prefusex.force;

/**
 * Uses a gravitational force model to act as a circular "wall". Can be used to
 * construct circles which either attract or repel items.
 *
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public class CircularWallForce extends AbstractForce {

    private static String[] pnames = new String[] { "GravitationalConstant" };
    
    public static final float DEFAULT_GRAV_CONSTANT = -0.1f;
    public static final int GRAVITATIONAL_CONST = 0;
    
    private float x, y, r;

    public CircularWallForce(float gravConst, 
        float x, float y, float r) 
    {
        params = new float[] { gravConst };
        this.x = x; this.y = y;
        this.r = r;
    } //
    
    public CircularWallForce(float x, float y, float r) {
        this(DEFAULT_GRAV_CONSTANT,x,y,r);
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
        float dx = x-n[0];
        float dy = y-n[1];
        float d = (float)Math.sqrt(dx*dx+dy*dy);
        float dr = r-d;
        float c = dr > 0 ? -1 : 1;
        float v = c*params[GRAVITATIONAL_CONST]*item.mass / (dr*dr);
        if ( d == 0.0 ) {
            dx = ((float)Math.random()-0.5f) / 50.0f;
            dy = ((float)Math.random()-0.5f) / 50.0f;
            d  = (float)Math.sqrt(dx*dx+dy*dy);
        }
        item.force[0] += v*dx/d;
        item.force[1] += v*dy/d;
        //System.out.println(dx/d+","+dy/d+","+dr+","+v);
    } //

} // end of class CircularWallForce
